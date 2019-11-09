package cn.appsys.service.appInfo;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import cn.appsys.dao.appInfo.AppInfoMapper;
import cn.appsys.pojo.AppInfo;

@Service
public class AppServiceImpl implements AppService {
	@Resource
	private AppInfoMapper mapper;

	@Override
	public AppInfo getAppInfo(Integer id) throws Exception {
		return mapper.getAppInfo(id, null);
	}

	@Override
	public boolean updateSatus(Integer status, Integer id) throws Exception {
		boolean flag = false;
		if (mapper.updateSatus(status, id) > 0) {
			flag = true;
		}
		return flag;
	}

	@Override
	public List<AppInfo> getAppInfoList(String softwareName, Integer flatformId, Integer categoryLevel1,
			Integer categoryLevel2, Integer categoryLevel3, Integer pageIndex, Integer pageSize) {
		return mapper.getAppInfoList(softwareName, 1, flatformId, categoryLevel1, categoryLevel2, categoryLevel3, (pageIndex - 1) * pageSize, pageSize);
	}

	@Override
	public Integer getAppInfoCount(String softwareName, Integer flatformId, Integer categoryLevel1,
			Integer categoryLevel2, Integer categoryLevel3) {
		return mapper.getAppInfoCount(softwareName, 1, flatformId, categoryLevel1, categoryLevel2, categoryLevel3);
	}
}
