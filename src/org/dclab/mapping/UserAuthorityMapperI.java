package org.dclab.mapping;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;
import org.dclab.model.UserAuthority;

public interface UserAuthorityMapperI {

	@Select("SELECT authorityID FROM `userauthority` WHERE username=#{username}")
	public int getAuthority(String username);	
	
	@Insert("INSERT INTO `userauthority` VALUES (#{authorityID},#{username})")
	public int insertUserAuthority(UserAuthority userauthority);
	
	@Delete("Delete From `userauthority` WHERE authorityID=#{authorityID} AND username=#{username}")
	public int deleteUserAuthority(UserAuthority userauthority);
}
