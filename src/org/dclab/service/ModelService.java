package org.dclab.service;

import java.awt.image.SampleModel;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.dclab.mapping.ModelMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.model.Model;
import org.dclab.model.User;  //暂时注释zookeeper，不可以删除
import org.dclab.util.OclValidate;
import org.dclab.util.ZipTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.dclab.zk.*;  //暂时注释zookeeper，不可以删除
import org.dom4j.DocumentException;

@Service
public class ModelService {
	@Autowired
	private ModelMapperI modelMapperI;
	@Autowired
	private UserMapperI userMapperI;  //暂时注释zookeeper，不可以删除
	// @Autowired
	// private ZooKeeperService zooKeeperService;
	public static int CLIENT_PORT = 2181;
	public static int TIME_OUT = 2000;

	public void setModelMapperI(ModelMapperI modelMapperI) {
		this.modelMapperI = modelMapperI;
	}

	public List<Model> checkModel(int bigClass, int middleClass, int smallClass) {
		System.out.println("samll:"+smallClass);
		return modelMapperI.getElementByClass(bigClass, middleClass, smallClass);
	}

	public String downloadModel(int elementID) {
		return modelMapperI.getUrlByElementID(elementID);
	}

	public int createModel(Model model) throws Exception {
		System.out.println("here:"+model.getDescription());
		if ( modelMapperI.insertModel(model)==1) {
			// zooKeeperService.zookeeperWatch(model);
			// zookeeper(model);
			// ZkDemo.hello();
			System.out.println("sucess");
			//System.out.println(model.getElementID());
			return model.getElementID();
			//暂时注释zookeeper，不可以删除
			//List<User> users = userMapperI.getUserByAuthority(model.getBigClass());
			//ZooKeeperService zookeeper = new ZooKeeperService();
			//return zookeeper.zookeeperWatch(model, users);
		} else {
			return -1;
		}
	}

	public String uploadModel(MultipartFile file,int elementID) throws Exception {
		//String fileName = file.getOriginalFilename(); // 获取文件名
		String path=System.getProperty("project.root")+"files"+File.separator;
		//System.out.println(path);
		//HashMap<String, String> map = new HashMap<String, String>();
		try {
			FileOutputStream fos = new FileOutputStream(path + file.getOriginalFilename());
			fos.write(file.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			//map.put("info", "上传失败");
			//return map;
			return null;
		}
		modelMapperI.updatePath(elementID, path+ file.getOriginalFilename());
		//map.put("info", "上传成功");
		System.out.println(path + file.getOriginalFilename());
		return path + file.getOriginalFilename();
	}
	
	
	public String oclValidate(int elementId) throws DocumentException{
		String path = modelMapperI.getFileIDByEId(elementId);
		OclValidate oclValidate1 = new OclValidate();
		String returnCode = oclValidate1.validateOcl(path);
		if(returnCode.length()==0){
			returnCode = "符合OCL";
		}
		System.out.println("returnCode:"+returnCode);
		return returnCode;
	}
	
	public void getFile(@RequestParam int elementID, HttpServletResponse response) throws IOException, InterruptedException{
		String path = modelMapperI.getFileIDByEId(elementID);
		
		String dir = System.getProperty("project.root")+"files";
		Process process = null;
		String command1 = "python "+ "codegen.py";
		System.out.println("command1:"+command1);
		process = Runtime.getRuntime().exec(command1, null, new File(dir));
		process.waitFor();
		
		String src = dir+File.separator+"DM";
		String dst = dir+File.separator+"DM.zip";
		ZipTool.compress(src, dst);
		
		try {
			// get your file as InputStream
			InputStream is = new FileInputStream(new File(dst));
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}
}
