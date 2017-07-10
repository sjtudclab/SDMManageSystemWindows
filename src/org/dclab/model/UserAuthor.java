package org.dclab.model;

import java.util.List;
import java.util.Map;

public class UserAuthor {
	private int roleId;
	private String roleName;
	private Map<String, Integer> authority;
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Map<String, Integer> getAuthority() {
		return authority;
	}
	public void setAuthority(Map<String, Integer> authority) {
		this.authority = authority;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	

}
