package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dclab.model.Model;
import org.dclab.model.MyModel;
import org.dclab.model.ProjectList;

public interface MyModelMapperI {
	
	@Insert("INSERT INTO `model`(EnglishName,ChineseName,description,fileUrl,creator,createTime,validation) VALUES (#{EnglishName},#{ChineseName},#{description},#{fileUrl},#{creator},#{createTime},0)")
	@Options(useGeneratedKeys = true, keyProperty = "elementID",keyColumn = "elementID")
	public int insertModel(MyModel model);
	
	@Select("SELECT * FROM model WHERE creator=#{username}")
	public List<MyModel> getModelByUserID(String username);
	
	@Select("SELECT * FROM model WHERE creator=#{username} AND validation=1")
	public List<MyModel> getModelCodeByUserID(String username);
	
	@Select("SELECT * FROM model WHERE elementID=#{elementID}")
	public MyModel getModelByEleID(int elementID);
	
	@Update("UPDATE model SET validation=#{validation} where elementID=#{elementID}")
	public int updateValidation(@Param(value="elementID")int elementID,@Param(value="validation")int validation);

	@Update("UPDATE model SET fileUrl=#{path} where elementID=#{elementID}")
	public int updatePath(@Param(value="elementID")int elementID,@Param(value="path")String path);
	
	@Select("SELECT fileUrl FROM `model` WHERE elementID=#{elementId}")
	public String getFileIDByEId(int elementId);
	
	@Select("SELECT EnglishName,fileID,elementID FROM `model` WHERE creator=#{username}")
	public List<ProjectList> getProjectlist(String username);
}

