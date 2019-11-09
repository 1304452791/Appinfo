package cn.appsys.dao.dataDictionary;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DataDictionary;

public interface DataDictionaryMapper {
	public List<DataDictionary> getDataDictionaryByTypeCode(@Param("typeCode") String typeCode);
}
