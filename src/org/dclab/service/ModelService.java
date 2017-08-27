package org.dclab.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.dclab.mapping.ModelMapperI;
import org.dclab.mapping.MyModelMapperI;
import org.dclab.mapping.NameContentMapperI;
import org.dclab.model.Model;
import org.dclab.model.MyModel;
import org.dclab.model.ProjectList; 
import org.dclab.util.OclValidate;
import org.dclab.util.ZipTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.dom4j.DocumentException;

//import org.python.util.PythonInterpreter;
@Service
public class ModelService {
	@Autowired
	private ModelMapperI modelMapperI;
	@Autowired
	private MyModelMapperI myModelMapperI;
	@Autowired
	private NameContentMapperI nameContentMapperI;
	public static int CLIENT_PORT = 2181;
	public static int TIME_OUT = 2000;

	public void setModelMapperI(ModelMapperI modelMapperI) {
		this.modelMapperI = modelMapperI;
	}

	public List<MyModel> checkModel(String username,int validation) {
		if(validation==1)
			return myModelMapperI.getModelCodeByUserID(username);
		else 
			return myModelMapperI.getModelByUserID(username);
	}
	
	public List<Model> checkSDM(int bigClass, int middleClass, int smallClass) {
		System.out.println("samll:" + smallClass);
		return modelMapperI.getElementByClass(bigClass, middleClass, smallClass);
	}

	public void downloadModel(int elementID, HttpServletResponse response) throws InterruptedException, IOException {
		String path = myModelMapperI.getFileIDByEId(elementID);
		File file = new File(path);
		String filename = path.substring(path.lastIndexOf("\\")+1);
		response.setHeader("content-disposition", "attachment;filename="  
                + URLEncoder.encode(filename, "UTF-8"));
		
		if (!file.isDirectory()) {
			InputStream iStream = new FileInputStream(file);
			org.apache.commons.io.IOUtils.copy(iStream, response.getOutputStream());
			response.flushBuffer();
			iStream.close();
		}
	}
	public void downloadSDM(HttpServletResponse response) throws InterruptedException, IOException {
		String filename = "SDM.face";
		String path = System.getProperty("project.root") + "files" + File.separator + "SDMFile" + File.separator + filename;
		File file = new File(path);
		response.setHeader("content-disposition", "attachment;filename="  
                + URLEncoder.encode(filename, "UTF-8"));
		
		if (!file.isDirectory()) {
			InputStream iStream = new FileInputStream(file);
			org.apache.commons.io.IOUtils.copy(iStream, response.getOutputStream());
			response.flushBuffer();
			iStream.close();
		}
	}
	public int createSDM(Model model) throws Exception {
		System.out.println("here:" + model.getDescription());
		if(nameContentMapperI.getContentByName(model.getEnglishName())==null){
			return -2;
		}
		if (modelMapperI.insertModel(model) == 1) {
			System.out.println("sucess");
			return model.getElementID();
		} else {
			return -1;
		}
	}
	
	public int createModel(MyModel model) throws Exception {
		System.out.println("here:" + model.getDescription());
		if (myModelMapperI.insertModel(model) == 1) {
			System.out.println("sucess");
			System.out.println(model.getElementID());
			return model.getElementID();
		} else {
			return -1;
		}
	}
	public String uploadSDM(MultipartFile file, int elementID) throws Exception {
		String path = System.getProperty("project.root") + "files" + File.separator + "SDMFile" + File.separator;
		try {
			FileOutputStream fos = new FileOutputStream(path + file.getOriginalFilename());
			fos.write(file.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		modelMapperI.updatePath(elementID, path + file.getOriginalFilename());
		// map.put("info", "上传成功");
		System.out.println(path + file.getOriginalFilename());
		return path + file.getOriginalFilename();
	}
	public String uploadModel(MultipartFile file, int elementID) throws Exception {
		String path = System.getProperty("project.root") + "files" + File.separator + "ModelManage" + File.separator;
		try {
			FileOutputStream fos = new FileOutputStream(path + file.getOriginalFilename());
			fos.write(file.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		myModelMapperI.updatePath(elementID, path + file.getOriginalFilename());
		// map.put("info", "上传成功");
		System.out.println(path + file.getOriginalFilename());
		return path + file.getOriginalFilename();
	}

	public String oclValidate(int elementId) throws DocumentException, IOException {
		String path = myModelMapperI.getFileIDByEId(elementId);
		OclValidate oclValidate;
		oclValidate = new OclValidate();
		String returnCode = oclValidate.validateOcl(path);
		if (returnCode.length() == 0) {
			returnCode = "conform OCL";
			myModelMapperI.updateValidation(elementId,1);
		}
		System.out.println("returnCode:" + returnCode);
		return returnCode;
	}

	public void getFile(int elementID, HttpServletResponse response) throws IOException, InterruptedException {
		String path = myModelMapperI.getFileIDByEId(elementID);

		String dir = System.getProperty("project.root") + "files"+File.separator + "ModelManage";
		Process process = null;
		
		String modelFileName = path.substring(path.lastIndexOf("\\")+1);
		System.out.println("modelname:"+modelFileName);
		String command1 = "python " + "codegen.py "+ modelFileName;
		System.out.println("command1:" + command1);
		process = Runtime.getRuntime().exec(command1, null, new File(dir));
		process.waitFor();
		String fileName = dir + File.separator + "DM";
		File file = new File(fileName);

		String filename = "DM.zip";
		response.setHeader("content-disposition", "attachment;filename="  
                + URLEncoder.encode(filename, "UTF-8"));
		
		if (file.isDirectory()) {
			String src = dir + File.separator + "DM";
			String dst = dir + File.separator + "DM.zip";
			ZipTool.compress(src, dst);
			try {
				// get your file as InputStream
				InputStream is = new FileInputStream(new File(dst));
				org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
				response.flushBuffer();
				is.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			deleteDir(file);
		} else {
			String filename1 = dir + File.separator + "DM.zip";
			InputStream iStream = new FileInputStream(new File(filename1));
			org.apache.commons.io.IOUtils.copy(iStream, response.getOutputStream());
			response.flushBuffer();
			iStream.close();
		}

	}
	
	public List<ProjectList> projectlist(String username) {
		System.out.println("文件列表");
		return myModelMapperI.getProjectlist(username);
	}
	
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
