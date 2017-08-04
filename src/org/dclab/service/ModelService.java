package org.dclab.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.dclab.mapping.ModelMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.model.Model;
import org.dclab.model.ProjectList;
import org.dclab.model.User; 
import org.dclab.util.OclValidate;
import org.dclab.util.ZipTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.dclab.zk.*;
import org.dom4j.DocumentException;

//import org.python.util.PythonInterpreter;
@Service
public class ModelService {
	@Autowired
	private ModelMapperI modelMapperI;
	@Autowired
	private UserMapperI userMapperI;
	// @Autowired
	// private ZooKeeperService zooKeeperService;
	public static int CLIENT_PORT = 2181;
	public static int TIME_OUT = 2000;

	public void setModelMapperI(ModelMapperI modelMapperI) {
		this.modelMapperI = modelMapperI;
	}

	public List<Model> checkModel(int bigClass, int middleClass, int smallClass) {
		System.out.println("samll:" + smallClass);
		return modelMapperI.getElementByClass(bigClass, middleClass, smallClass);
	}

	public void downloadModel(int elementID, HttpServletResponse response) throws InterruptedException, IOException {
		String path = modelMapperI.getFileIDByEId(elementID);
		GitLabService gitLabService = new GitLabService();
		gitLabService.download(path);
		File file = new File(path);
		if (!file.isDirectory()) {
			InputStream iStream = new FileInputStream(file);
			org.apache.commons.io.IOUtils.copy(iStream, response.getOutputStream());
			response.flushBuffer();
			iStream.close();
		}
	}

	public int createModel(Model model) throws Exception {
		System.out.println("here:" + model.getDescription());
		if (modelMapperI.insertModel(model) == 1) {
			// zooKeeperService.zookeeperWatch(model);
			// zookeeper(model);
			// ZkDemo.hello();
			System.out.println("sucess");
			// System.out.println(model.getElementID());

			// 调用zookeeper通知成员审核元素
			List<User> users = userMapperI.getUserByAuthority(model.getBigClass());
			ZooKeeperService zookeeper = new ZooKeeperService();
			zookeeper.zookeeperWatch(model, users);
			return model.getElementID();
		} else {
			return -1;
		}
	}

	public String uploadModel(MultipartFile file, int elementID) throws Exception {
		// String fileName = file.getOriginalFilename(); // 获取文件名
		String path = System.getProperty("project.root") + "files" + File.separator + "ModelManage" + File.separator;
		// System.out.println(path);
		// HashMap<String, String> map = new HashMap<String, String>();
		try {
			FileOutputStream fos = new FileOutputStream(path + file.getOriginalFilename());
			fos.write(file.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			// map.put("info", "上传失败");
			// return map;
			return null;
		}
		modelMapperI.updatePath(elementID, path + file.getOriginalFilename());
		// map.put("info", "上传成功");
		System.out.println(path + file.getOriginalFilename());
		return path + file.getOriginalFilename();
	}

	public String oclValidate(int elementId) throws DocumentException, IOException {
		String path = modelMapperI.getFileIDByEId(elementId);
		OclValidate oclValidate;
		oclValidate = new OclValidate();
		String returnCode = oclValidate.validateOcl(path);
		if (returnCode.length() == 0) {
			returnCode = "conform OCL";
		}
		System.out.println("returnCode:" + returnCode);
		return returnCode;
	}

	public void getFile(int elementID, HttpServletResponse response) throws IOException, InterruptedException {
		String path = modelMapperI.getFileIDByEId(elementID);

		String dir = System.getProperty("project.root") + "files";
		Process process = null;
		String command1 = "python " + "codegen.py";
		System.out.println("command1:" + command1);
		process = Runtime.getRuntime().exec(command1, null, new File(dir));
		process.waitFor();
		String fileName = dir + File.separator + "DM";
		File file = new File(fileName);
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
		} else {
			String filename1 = dir + File.separator + "DM.tar.gz";
			InputStream iStream = new FileInputStream(new File(filename1));
			org.apache.commons.io.IOUtils.copy(iStream, response.getOutputStream());
			response.flushBuffer();
			iStream.close();
		}

	}
	
	public List<ProjectList> projectlist(String username) {
		System.out.println("文件列表");
		return modelMapperI.getProjectlist(username);
	}
}
