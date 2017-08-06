package org.dclab.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.dclab.zk.GitLabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComponentController {
	@Autowired
	private GitLabService gitLabService;
	
	@RequestMapping(value="/eclipse/getIntegration")
	public void getIntegration(@RequestParam(value="TSStype")String TSStype,@RequestParam(value="MessageType")String MessageType,@RequestParam(value="SourceComponent")String SourceComponent,@RequestParam(value="DestinationConponent")String DestinationConponent,
			@RequestParam(value="SourceComponentType")String SourceComponentType,@RequestParam(value="DestinationConponentType")String DestinationConponentType,HttpServletResponse response) throws IOException, InterruptedException{
		gitLabService.getIntegration(TSStype,MessageType,SourceComponent,DestinationConponent,SourceComponentType,DestinationConponentType, response);
	}
	public ComponentController() {
		// TODO Auto-generated constructor stub
	}

}
