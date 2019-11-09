package cn.appsys.service.appCategory;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.appCategory.AppCategoryMapper;
import cn.appsys.pojo.AppCategory;

@Service("appCategoryService")
public class AppCategoryServiceImpl implements AppCategoryService {

	@Resource
	private AppCategoryMapper appCategoryMapper;

	@Override
	public List<AppCategory> getAppCategoryByParentId(Integer parentId) {
		List<AppCategory> list = null;
		try {
			list = appCategoryMapper.getAppCategoryByParentId(parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
