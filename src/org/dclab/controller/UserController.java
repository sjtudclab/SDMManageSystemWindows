package org.dclab.controller;

import java.util.List;

import org.dclab.model.Role;
import org.dclab.model.User;
import org.dclab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@RequestMapping(value="/setRole",method=RequestMethod.POST)
	public int setRole(@RequestParam(value="username")String username,@RequestParam(value="rid")int rid){
		return userService.setRole(username, rid);
	}
	@RequestMapping(value="/deleteUser",method=RequestMethod.POST)
	public int deleteUser(@RequestParam(value="username")String username){
		return userService.deleteUser(username);
	}
	@RequestMapping(value="/pwdChange",method=RequestMethod.POST)
	public int pwdChange(@RequestParam(value="username")String username,@RequestParam(value="password")String password){
		return userService.passwordChange(username, password);
	}
	
	@RequestMapping(value="/infoChange",method=RequestMethod.POST)
	public int infoChange(@RequestParam(value="username")String username,@RequestParam(value="phone")String phone,@RequestParam(value="email")String email){
		return userService.infoChange(username, phone, email);
	}
	@RequestMapping(value="/getUserList",method=RequestMethod.POST)
	public List<User> getUserList(){
		return userService.getUserList();
	}
	@RequestMapping(value="/getRoleList",method=RequestMethod.POST)
	public List<Role> getRoleList(){
		return userService.getRoleList();
	}

}
