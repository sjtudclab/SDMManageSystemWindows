package org.dclab.model;

import java.util.List;

public class Role {

	private int rid;
	private String roleName;
	private List<Integer> authority;
	
	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public List<Integer> getAuthority() {
		return authority;
	}

	public void setAuthority(List<Integer> authority) {
		this.authority = authority;
	}
	public Role() {
		this.authority=null;
		// TODO Auto-generated constructor stub
	}

}
