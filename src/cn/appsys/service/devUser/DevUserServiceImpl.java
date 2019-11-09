package cn.appsys.service.devUser;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.devUser.DevUserMapper;
import cn.appsys.pojo.DevUser;

@Service("devUserService")
public class DevUserServiceImpl implements DevUserService {

	@Resource
	private DevUserMapper devUserMapper;

	@Override
	public DevUser login(String devCode) {
		DevUser devUser = null;
		try {
			devUser = devUserMapper.login(devCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return devUser;
	}

}
