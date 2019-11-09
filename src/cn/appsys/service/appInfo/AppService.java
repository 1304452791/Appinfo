package cn.appsys.service.appInfo;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import cn.appsys.pojo.AppInfo;

public interface AppService {

	public List<AppInfo> getAppInfoList(String softwareName, Integer flatformId, Integer categoryLevel1,
			Integer categoryLevel2, Integer categoryLevel3, Integer pageIndex, Integer pageSize);

	public Integer getAppInfoCount(String softwareName, Integer flatformId, Integer categoryLevel1,
			Integer categoryLevel2, Integer categoryLevel3);

	public AppInfo getAppInfo(@Param(value = "id") Integer id) throws Exception;

	public boolean updateSatus(@Param(value = "status") Integer status, @Param(value = "id") Integer id)
			throws Exception;

}
