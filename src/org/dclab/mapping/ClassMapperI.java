package org.dclab.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.dclab.model.SDMclass;

public interface ClassMapperI {
	@Select("SELECT * FROM `sdmclass`")
	public List<SDMclass> getSDMClass();
}
