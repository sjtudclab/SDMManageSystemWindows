package org.dclab.controller;

import java.util.List;

import org.dclab.model.Model;
import org.dclab.service.ModelService;
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
	public List<Model> checkModel(@RequestParam(value="bigClass")int bigClass,@RequestParam(value="middleClass")int middleClass,@RequestParam(value="smallClass")int smallClass){
		return modelService.checkModel(bigClass, middleClass, smallClass);
	}
	
	@RequestMapping(value="/downloadModel",method=RequestMethod.POST)
	public String downloadModel(int elementID){
		return modelService.downloadModel(elementID);
	}
	
	@RequestMapping(value="/createModel",method=RequestMethod.POST)
	public int createModel(Model model) throws Exception{
		//System.out.println(model.getCreateTime());
		return modelService.createModel(model);
	}
	@RequestMapping(value="/uploadModel",method=RequestMethod.POST)
	public String uploadModel(@RequestParam(value="SDMFile")MultipartFile SDMFile,@RequestParam(value="elementID")int elementID) throws Exception{
		//System.out.println("进入后台");
		return modelService.uploadModel(SDMFile,elementID);
	}
}
