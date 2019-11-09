package cn.appsys.dao.backenduser;

import org.apache.ibatis.annotations.Param;
import cn.appsys.pojo.BackendUser;

public interface BackendUserMapper {

	public BackendUser getLoginUser(@Param("userCode") String userCode) throws Exception;

}
