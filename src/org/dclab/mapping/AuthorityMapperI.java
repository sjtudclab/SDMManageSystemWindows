package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.dclab.model.AuthorityBean;

public interface AuthorityMapperI {
	@Select("select name,url from authority where authority=#{rid}")
	public List<AuthorityBean> getListByRid(int rid);//用rid获得包含权限名称和url的对象的list
	
	@Select("select name,url from authority where authority=#{rid} OR authority=0")
	public List<AuthorityBean> getListByRidS(int rid);
}
