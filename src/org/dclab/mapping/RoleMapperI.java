package org.dclab.mapping;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import org.dclab.model.Role;

public interface RoleMapperI {
	@Select("SELECT rid,roleName FROM `role` ORDER BY rid")
	public List<Role> getRoleList();
	
	@Select("SELECT DISTINCT authority FROM `authority` where rid=#{rid}")
	public List<Integer> getAuthorityByRid(int rid);
}
