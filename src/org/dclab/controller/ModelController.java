package org.dclab.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.dclab.model.Model;
import org.dclab.model.MyModel;
import org.dclab.model.ProjectList;
import org.dclab.service.ModelService;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ModelController {
	@Autowired
	private ModelService modelService;

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}
	

	@RequestMapping(value="/checkModel",method=RequestMethod.POST)
	public List<MyModel> checkModel(String username){
		return modelService.checkModel(username,0);
	}
	
	@RequestMapping(value="/checkModelCode",method=RequestMethod.POST)
	public List<MyModel> checkModelCode(String username){
		return modelService.checkModel(username,1);
	}
	
	@RequestMapping(value="/checkSDM",method=RequestMethod.POST)
	public List<Model> checkSDM(@RequestParam(value="bigClass")int bigClass,@RequestParam(value="middleClass")int middleClass,@RequestParam(value="smallClass")int smallClass){
		return modelService.checkSDM(bigClass, middleClass, smallClass);
	}
	
	@RequestMapping(value="/downloadSDM")
	public void downloadSDM(HttpServletResponse response) throws IOException, InterruptedException{
		modelService.downloadSDM(response);
	}
	
	@RequestMapping(value="/downloadModel")
	public void downloadModel(int elementID,HttpServletResponse response) throws IOException, InterruptedException{
		modelService.downloadModel(elementID,response);
	}
	
	@RequestMapping(value="/createSDM",method=RequestMethod.POST)
	public int createSDM(Model model) throws Exception{
		//System.out.println(model.getCreateTime());
		System.out.println("service");
		return modelService.createSDM(model);
	}
	
	@RequestMapping(value="/createModel",method=RequestMethod.POST)
	public int createModel(MyModel model) throws Exception{
		//System.out.println(model.getCreateTime());
		System.out.println("service");
		return modelService.createModel(model);
	}
	
	@RequestMapping(value="/uploadModel",method=RequestMethod.POST)
	public String uploadModel(@RequestParam(value="SDMFile")MultipartFile SDMFile,@RequestParam(value="elementID")int elementID) throws Exception{
		System.out.println("进入后台");
		return modelService.uploadSDM(SDMFile,elementID);
	}
	
	@RequestMapping(value="/exportCode")
	public void exportCode(int elementID,HttpServletResponse response) throws IOException, InterruptedException{
		modelService.getFile(elementID, response);
	}
	
	@RequestMapping(value="/oclValidate",method=RequestMethod.POST)
	public String oclValidate(int elementID) throws DocumentException, IOException{
		return modelService.oclValidate(elementID);
	}
	
	@RequestMapping(value="eclipse/projectlist",method=RequestMethod.POST)
	public List<ProjectList> projectlist(@RequestParam(value="userid")String username){
		return modelService.projectlist(username);
	}
	
	@RequestMapping(value="/eclipse/uploadModel",method=RequestMethod.POST)
	public void uploadModelEclipse(@RequestParam(value="ModelFile")MultipartFile ModelFile,@RequestParam(value="elementID")int elementID) throws Exception{
		System.out.println("enter eclipse uploadModel");
		modelService.uploadModel(ModelFile,elementID);
	}
}
