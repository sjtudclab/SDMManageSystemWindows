package org.dclab.controller;

import org.dclab.model.User;
import org.dclab.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	@RequestMapping(value="/login",method=RequestMethod.POST)
	public Object login(@RequestParam(value="username")String username,@RequestParam(value="password")String password){
		return loginService.login(username, password);
	}
	
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public Object register(User user){
		//System.out.println("phone"+user.getPhone());
		return loginService.register(user);
	}
	
	@RequestMapping(value="eclipse/login",method=RequestMethod.POST)
	public String eclipseLogin(@RequestParam(value="username")String username,@RequestParam(value="password")String password){
		return loginService.eclipseLogin(username, password);
	}
}
