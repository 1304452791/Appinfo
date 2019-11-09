package cn.appsys.service.backenduser;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import cn.appsys.dao.backenduser.BackendUserMapper;
import cn.appsys.pojo.BackendUser;

@Service
public class BackendUserServiceImpl implements BackendUserService {
	@Resource
	private BackendUserMapper mapper;

	@Override
	public BackendUser login(String userCode, String userPassword) throws Exception {
		BackendUser user = null;
		user = mapper.getLoginUser(userCode);
		if (null != user) {
			if (!user.getUserPassword().equals(userPassword))
				user = null;
		}
		return user;
	}

}
