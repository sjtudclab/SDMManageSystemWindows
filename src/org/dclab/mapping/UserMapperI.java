package org.dclab.mapping;


import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dclab.model.User;

public interface UserMapperI {
	@Select("SELECT * FROM `user` WHERE username=#{username}")
	public User getUserById(String username);
	
	@Select("SELECT * FROM `user` WHERE authority=#{authority}")
	public List<User> getUserByAuthority(int authority);
	
	@Insert("INSERT INTO `user` VALUES (#{username},#{email},#{phone},#{password},2,0)")
	public int insertUser(User user);
	
	@Update("UPDATE `user` SET `password`=#{password} WHERE username=#{username}")
	public int updatePassword(@Param(value="password")String password,@Param(value="username")String username);
	
	@Update("UPDATE `user` SET email=#{email}, phone=#{phone} WHERE username=#{username}")
	public int updateInfo(@Param(value="email")String email,@Param(value="phone")String phone,@Param(value="username")String username);
	
	@Select("SELECT rid FROM `user` WHERE username=#{username}")
	public int getAuthorityByUserName(String username);

	@Select("SELECT count(*) FROM `user` WHERE authority=#{authority}")
	public int getUserNum(int authority);
	
	@Update("UPDATE `user` SET `rid`=#{rid} WHERE username=#{username}")
	public int updateRole(@Param(value="rid")int rid,@Param(value="username")String username);
	
	@Delete("Delete FROM `user` WHERE username=#{username}")
	public int deleteUser(String username);
	
	@Select("SELECT username,email,phone,rid FROM `user`")
	public List<User> getUserList();
}
