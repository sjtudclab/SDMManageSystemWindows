package org.dclab.service;

import java.util.HashMap;
import java.util.List;

import org.dclab.mapping.RoleMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

	@Autowired
	private UserMapperI userMapperI;
	@Autowired
	private RoleMapperI roleMapperI;
	public void setUserMapperI(UserMapperI userMapperI) {
		this.userMapperI = userMapperI;
	}

	public Object login(String id, String password) {
		User user = userMapperI.getUserById(id);
		if (user != null) {
			if (user.getPassword().equals(password)){
				int roleId=user.getRid();
				List<Integer> authority=roleMapperI.getAuthorityByRid(roleId);
				HashMap<String, Object> result=new HashMap<String, Object>();
				result.put("user", user);
				result.put("authority", authority);
				return result;
			}
			else
				return null;
		} else
			return null;
	}

	public Object register(User user) {
		if (userMapperI.insertUser(user) == 1)
			return user;
		else
			return null;
	}

}
