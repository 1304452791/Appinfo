package cn.appsys.service.dataDictionary;

import java.util.List;
import cn.appsys.pojo.DataDictionary;

public interface DataDictionaryService {
	public List<DataDictionary> getDataDictionaryByTypeCode(String typeCode);
}
