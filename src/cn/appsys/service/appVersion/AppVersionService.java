package cn.appsys.service.appVersion;

import java.util.List;
import cn.appsys.pojo.AppVersion;

public interface AppVersionService {
	public List<AppVersion> getAppVersionList(Integer appId) throws Exception;

	public boolean appsysadd(AppVersion appVersion) throws Exception;

	public AppVersion getAppVersionById(Integer id) throws Exception;

	public boolean modify(AppVersion appVersion) throws Exception;

	public boolean deleteApkFile(Integer id) throws Exception;
}
