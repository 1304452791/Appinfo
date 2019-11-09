package cn.appsys.dao.devUser;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DevUser;

public interface DevUserMapper {
	public DevUser login(@Param("devCode") String devCode);
}
