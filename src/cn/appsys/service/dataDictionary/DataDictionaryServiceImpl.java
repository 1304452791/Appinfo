package cn.appsys.service.dataDictionary;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.dataDictionary.DataDictionaryMapper;
import cn.appsys.pojo.DataDictionary;

@Service("dataDictionaryService")
public class DataDictionaryServiceImpl implements DataDictionaryService {

	@Resource
	private DataDictionaryMapper dataDictionaryMapper;

	@Override
	public List<DataDictionary> getDataDictionaryByTypeCode(String typeCode) {
		List<DataDictionary> list = null;
		try {
			list = dataDictionaryMapper.getDataDictionaryByTypeCode(typeCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
