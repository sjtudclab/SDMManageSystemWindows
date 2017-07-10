package org.dclab.controller;

import java.util.List;

import org.dclab.model.CR;
import org.dclab.model.Model;
import org.dclab.service.CRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CRController {
	@Autowired
	private CRService crService;
	
	@RequestMapping(value="/checkCR",method=RequestMethod.POST)
    public List<Model> checkCR(@RequestParam(value="username")String userName){
    	return crService.checkCR(userName);
    }

	@RequestMapping(value="/checkCRCCB",method=RequestMethod.POST)
    public List<Model> checkCRCCB(@RequestParam(value="username")String userName){
    	return crService.checkCRCCB(userName);
    }
	
	@RequestMapping(value="/checkCRByElementID",method=RequestMethod.POST)
    public Model checkCRByElementID(@RequestParam(value="elementID")int elementID){
    	return crService.checkCRByElementID(elementID);
    }
	
	public void setCrService(CRService crService) {
		this.crService = crService;
	}

	@RequestMapping(value="/vote",method=RequestMethod.POST)
    public int vote(CR cr){
    	return crService.vote(cr);
    }
}
