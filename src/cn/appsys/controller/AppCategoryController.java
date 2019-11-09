package cn.appsys.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import cn.appsys.pojo.AppCategory;
import cn.appsys.service.appCategory.AppCategoryService;

@Controller
@RequestMapping(value = "/dev")
public class AppCategoryController {

	@Resource
	private AppCategoryService appCategoryService;

	@RequestMapping(value = "/categorylevellist", method = RequestMethod.GET)
	@ResponseBody
	public Object categorylevellist(@RequestParam String pid) {
		List<AppCategory> data = null;
		try {
			if (pid != null && pid != "") {
				data = appCategoryService.getAppCategoryByParentId(Integer.parseInt(pid));
			} else {
				data = appCategoryService.getAppCategoryByParentId(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONArray.toJSONString(data);
	}
}
