package org.dclab.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.dclab.mapping.ModelMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.model.Model;
import org.dclab.model.User;  //暂时注释zookeeper，不可以删除
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.dclab.zk.*;  //暂时注释zookeeper，不可以删除

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
		return modelMapperI.getElementByClass(bigClass, middleClass, smallClass);
	}

	public String downloadModel(int elementID) {
		return modelMapperI.getUrlByElementID(elementID);
	}

	public int createModel(Model model) throws Exception {
		if ( modelMapperI.insertModel(model)==1) {
			// zooKeeperService.zookeeperWatch(model);
			// zookeeper(model);
			// ZkDemo.hello();
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
		String path=System.getProperty("project.root")+"files"+File.separator+"import"+File.separator;
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
}
