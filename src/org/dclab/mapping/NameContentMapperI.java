package org.dclab.mapping;


import org.apache.ibatis.annotations.Select;

public interface NameContentMapperI {
	@Select("SELECT content FROM `nameContent` WHERE EnglishName=#{EnglishName}")
	public String getContentByName(String EnglishName);
}
