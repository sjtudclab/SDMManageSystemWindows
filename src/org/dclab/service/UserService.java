package org.dclab.service;

import java.util.List;

import org.dclab.mapping.AuthorityMapperI;
import org.dclab.mapping.RoleMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.model.AuthorityBean;
import org.dclab.model.Role;
import org.dclab.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	private UserMapperI userMapperI;
	@Autowired
	private RoleMapperI roleMapperI;
	@Autowired
	private AuthorityMapperI authorityMapperI;
	
	public void setUserMapperI(UserMapperI userMapperI) {
		this.userMapperI = userMapperI;
	}
	public int setRole(String username,int rid){
		if(userMapperI.updateRole(rid,username)==1)
			return 1;
		else
			return -1;
	}
	public int passwordChange(String username,String password){
		if(userMapperI.updatePassword(password, username)==1)
			return 1;
		else
			return -1;
	}
	
	public int infoChange(String username,String phone,String email){
		if(userMapperI.updateInfo(email, phone, username)==1)
			return 1;
		else {
			return -1;
		}
	}

	public int deleteUser(String username){
		if(userMapperI.deleteUser(username)==1)
			return 1;
		else
			return -1;
	}
	public List<User> getUserList(){
		return userMapperI.getUserList();
	}
	
	public List<Role> getRoleList(){
		return roleMapperI.getRoleList();
	}
	
	public void setAuthorityMapperI(AuthorityMapperI authorityMapperI) {
		this.authorityMapperI = authorityMapperI;
	}


	public Object initial(String username){
		//name get rid
		int rid = userMapperI.getAuthorityByUserName(username);
		List<AuthorityBean> list = null;
		switch (rid) {
		case 0:
			list = authorityMapperI.getListByRid(rid);
			break;
		case 1:
			list = authorityMapperI.getListByRidS(rid);
			break;
		case 2:
			list= authorityMapperI.getListByRidS(rid);
			break;
		case 3:
			list = authorityMapperI.getListByRid(rid);
			break;
		}
		return list;
		
	}
}
