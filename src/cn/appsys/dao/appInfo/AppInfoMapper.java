package cn.appsys.dao.appInfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface AppInfoMapper {
	public List<AppInfo> getAppInfoList(@Param("softwareName") String softwareName, @Param("status") Integer status,
			@Param("flatformId") Integer flatformId, @Param("categoryLevel1") Integer categoryLevel1,
			@Param("categoryLevel2") Integer categoryLevel2, @Param("categoryLevel3") Integer categoryLevel3,
			@Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);

	public AppInfo getAppInfo(@Param(value = "id") Integer id, @Param(value = "APKName") String APKName)
			throws Exception;

	public Integer getAppInfoCount(@Param("softwareName") String softwareName, @Param("status") Integer status,
			@Param("flatformId") Integer flatformId, @Param("categoryLevel1") Integer categoryLevel1,
			@Param("categoryLevel2") Integer categoryLevel2, @Param("categoryLevel3") Integer categoryLevel3);

	public Integer addAppInfo(AppInfo appInfo);

	public int deleteAppInfoById(@Param(value = "id") Integer delId) throws Exception;

	public AppInfo ApkExist(@Param("APKName") String APKName);

	public int deleteAppLogo(@Param(value = "id") Integer id) throws Exception;

	public int updateVersionId(@Param(value = "versionId") Integer versionId, @Param(value = "id") Integer appId)
			throws Exception;

	public int updateSatus(@Param(value = "status") Integer status, @Param(value = "id") Integer id) throws Exception;

	public int modify(AppInfo appInfo) throws Exception;
}
