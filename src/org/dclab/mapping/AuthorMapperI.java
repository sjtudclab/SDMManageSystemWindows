package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dclab.model.UserRole;

public interface AuthorMapperI {
	@Select("SELECT authority FROM `authority`")
	public List<String> getAuthor();
	
	@Select("SELECT distinct roleName FROM `role`;")
	public List<String> getRole();
	
	@Select("SELECT authority FROM `authority` WHERE rid=#{rid};")
	public List<String> getUserAuthor(int rid);
	
	@Select("SELECT authority FROM `authority` WHERE rid=#{rid} OR rid=0;")
	public List<String> getUserAuthorM(int rid);
	
	@Select("SELECT rid FROM `role` WHERE roleName=#{roleName}")
	public int getRidByRoleName(String roleName);
	
	@Select("SELECT username,roleName FROM `user` JOIN role WHERE user.rid=role.rid")
	public List<UserRole> getUserRole();
	
	@Delete("DELETE FROM `authority` WHERE rid=#{rid}")
	public int deleteauthor(int rid);
	
	@Insert("INSERT INTO authority (authority,rid) VALUES(#{author},#{roleId})")
	public int insert(@Param(value="author")String author,@Param(value="roleId")int roleId);
}
