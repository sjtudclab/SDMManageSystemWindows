package org.dclab.controller;

import org.dclab.model.ModifyReceive;
import org.dclab.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/author")
public class AuthorController {
	
	@Autowired
	private AuthorService authorService;

	@RequestMapping("/initial")
	public Object initial(){
		System.out.println("here");
		return authorService.initial();	
	}
	
	@RequestMapping("/role")
	public Object role(){
		return authorService.role();
	}
	
	@RequestMapping("/authorModify")
	public void authorM(ModifyReceive mReceive){
		System.out.println("modify");
		authorService.authorModify(mReceive);
	}
}
