package cn.appsys.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.dataDictionary.DataDictionaryService;

@Controller
@RequestMapping(value = "/dev")
public class DataDictionaryController {
		
	@Resource
	private DataDictionaryService dataDictionaryService;
	
	@RequestMapping(value = "/datadictionarylist", method = RequestMethod.GET)
	@ResponseBody
	public Object DataDictionaryList(@RequestParam String tcode) {
		List<DataDictionary> data = null;
		try {
			data = dataDictionaryService.getDataDictionaryByTypeCode(tcode);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return JSONArray.toJSONString(data);
	}
	
	
}
