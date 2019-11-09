package cn.appsys.service.backenduser;

import cn.appsys.pojo.BackendUser;

public interface BackendUserService {
	public BackendUser login(String userCode, String userPassword) throws Exception;
}
