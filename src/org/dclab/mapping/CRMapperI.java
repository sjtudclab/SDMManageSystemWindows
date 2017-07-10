package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dclab.model.CR;
import org.dclab.model.Model;

public interface CRMapperI {
	@Insert("INSERT INTO cr(eleID,vote,state,votor,reason) VALUES (#{eleID},#{vote},#{state},#{votor},#{reason})")
	public int insertCR(CR cr);
	
	@Select("SELECT * FROM `cr` WHERE eleID=#{eleID}")
	public List<CR> getCRByEleID(int eleID);
	
	@Select("SELECT count(*) FROM `cr` WHERE eleID=#{eleID} AND state=1 AND vote=1")
	public int getApprovalNum(int eleID);
	
	@Select("SELECT count(*) FROM `cr` WHERE eleID=#{eleID} AND state=1 AND vote=-1")
	public int getDisApprovalNum(int eleID);
	
	@Select("SELECT count(*) FROM `cr` WHERE eleID=#{eleID} AND state=1 AND vote=0")
	public int getAbstainNum(int eleID);
	
	@Select("SELECT sum(vote) FROM `cr` WHERE eleID=#{eleID} AND state=1")
	public int getResult(int eleID);
	
	@Select("SELECT count(*) FROM `cr` WHERE eleID=#{eleID} AND state=1")
	public int getCheckNum(int eleID);
	
	@Select("SELECT count(*) FROM `cr` WHERE eleID=#{eleID} AND votor=#{username}")
	public int getUserState(@Param(value="eleID")int eleID,@Param(value="username")String username);
	
	@Select("SELECT vote FROM `cr` WHERE eleID=#{eleID} AND votor=#{username}")
	public int getVote(@Param(value="eleID")int eleID,@Param(value="username")String username);
	
	
}
