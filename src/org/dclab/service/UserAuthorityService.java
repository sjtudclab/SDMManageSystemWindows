package org.dclab.service;

import org.dclab.mapping.UserAuthorityMapperI;
import org.dclab.model.UserAuthority;
import org.springframework.beans.factory.annotation.Autowired;

public class UserAuthorityService {
	@Autowired
	private UserAuthorityMapperI userAuthorityMapperI;
	
	public void setUserAuthorityMapperI(UserAuthorityMapperI userAuthorityMapperI) {
		this.userAuthorityMapperI = userAuthorityMapperI;
	}

	public String addAuthority(UserAuthority userAuthority){
		if(userAuthorityMapperI.insertUserAuthority(userAuthority)==1){
			return "修改成功";
		}else {
			return "无此权限";
		}
	}
	
	public int deleteAuthority(UserAuthority userAuthority){
		if(userAuthorityMapperI.deleteUserAuthority(userAuthority)==1){
			return 1;
		}else {
			return -1;
		}
		
	}
	
	public int getAuthority(String username){
		int authority=userAuthorityMapperI.getAuthority(username);
		return authority;
	}
}
