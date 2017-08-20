package org.dclab.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.dclab.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComponentController {
	@Autowired
	private ComponentService gitLabService;
	public void setGitLabService(ComponentService gitLabService) {
		this.gitLabService = gitLabService;
	}
	@RequestMapping(value="/eclipse/getIntegration")
	public void getIntegration(@RequestParam(value="TSStype")String TSStype,@RequestParam(value="MessageType")String MessageType,@RequestParam(value="SourceComponent")String SourceComponent,@RequestParam(value="DestinationConponent")String DestinationConponent,
			@RequestParam(value="SourceComponentType")String SourceComponentType,@RequestParam(value="IOname")String IOname,
			@RequestParam(value="IOtype")String IOtype,@RequestParam(value="DestinationConponentType")String DestinationConponentType,HttpServletResponse response) throws IOException, InterruptedException{
		gitLabService.getIntegration(TSStype,MessageType,SourceComponent,DestinationConponent,SourceComponentType,DestinationConponentType,IOname,IOtype, response);
	}
	public ComponentController() {
		// TODO Auto-generated constructor stub
	}

}
