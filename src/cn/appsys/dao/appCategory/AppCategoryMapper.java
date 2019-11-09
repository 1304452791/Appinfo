package cn.appsys.dao.appCategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;

public interface AppCategoryMapper {
	public List<AppCategory> getAppCategoryByParentId(@Param("parentId") Integer parentId);
}
