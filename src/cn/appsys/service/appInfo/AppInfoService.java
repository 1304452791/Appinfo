package cn.appsys.service.appInfo;

import java.util.List;

import cn.appsys.pojo.AppInfo;

public interface AppInfoService {
	public List<AppInfo> getAppInfoList(String softwareName, Integer status, Integer flatformId, Integer categoryLevel1,
			Integer categoryLevel2, Integer categoryLevel3, Integer pageIndex, Integer pageSize);

	public Integer getAppInfoCount(String softwareName, Integer status, Integer flatformId, Integer categoryLevel1,
			Integer categoryLevel2, Integer categoryLevel3);

	public Integer addAppInfo(AppInfo appInfo);

	public AppInfo ApkExist(String APKName);

	public boolean modify(AppInfo appInfo) throws Exception;

	public AppInfo getAppInfo(Integer id, String APKName) throws Exception;

	public boolean deleteAppLogo(Integer id) throws Exception;

	public boolean appsysdeleteAppById(Integer id) throws Exception;

	public boolean appsysUpdateSaleStatusByAppId(AppInfo appInfo) throws Exception;
}
