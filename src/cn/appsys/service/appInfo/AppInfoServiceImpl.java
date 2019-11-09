package cn.appsys.service.appInfo;

import java.io.File;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import cn.appsys.dao.appInfo.AppInfoMapper;
import cn.appsys.dao.appVersion.AppVersionMapper;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;

@Service("appInfoService")
public class AppInfoServiceImpl implements AppInfoService {

	@Resource
	private AppInfoMapper appInfoMapper;

	@Resource
	AppVersionMapper appVersionMapper;

	@Override
	public List<AppInfo> getAppInfoList(String softwareName, Integer status, Integer flatformId, Integer categoryLevel1,
			Integer categoryLevel2, Integer categoryLevel3, Integer pageIndex, Integer pageSize) {
		List<AppInfo> list = null;
		try {
			list = appInfoMapper.getAppInfoList(softwareName, status, flatformId, categoryLevel1, categoryLevel2,
					categoryLevel3, pageIndex, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Integer getAppInfoCount(String softwareName, Integer status, Integer flatformId, Integer categoryLevel1,
			Integer categoryLevel2, Integer categoryLevel3) {
		int count = 0;
		try {
			count = appInfoMapper.getAppInfoCount(softwareName, status, flatformId, categoryLevel1, categoryLevel2,
					categoryLevel3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public Integer addAppInfo(AppInfo appInfo) {
		int num = 0;
		try {
			num = appInfoMapper.addAppInfo(appInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}

	@Override
	public AppInfo ApkExist(String APKName) {
		AppInfo appInfo = null;
		try {
			appInfo = appInfoMapper.ApkExist(APKName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appInfo;
	}

	@Override
	public AppInfo getAppInfo(Integer id, String APKName) throws Exception {
		return appInfoMapper.getAppInfo(id, APKName);
	}

	@Override
	public boolean deleteAppLogo(Integer id) throws Exception {
		boolean flag = false;
		if (appInfoMapper.deleteAppLogo(id) > 0) {
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean appsysdeleteAppById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		boolean flag = false;
		int versionCount = appVersionMapper.getVersionCountByAppId(id);
		List<AppVersion> appVersionList = null;
		if (versionCount > 0) {// 1 ��ɾ�汾��Ϣ
			// <1> ɾ���ϴ���apk�ļ�
			appVersionList = appVersionMapper.getAppVersionList(id);
			for (AppVersion appVersion : appVersionList) {
				if (appVersion.getApkLocPath() != null && !appVersion.getApkLocPath().equals("")) {
					File file = new File(appVersion.getApkLocPath());
					if (file.exists()) {
						if (!file.delete())
							throw new Exception();
					}
				}
			}
			// <2> ɾ��app_version������
			appVersionMapper.deleteVersionByAppId(id);
		}
		// 2 ��ɾapp������Ϣ
		// <1> ɾ���ϴ���logoͼƬ
		AppInfo appInfo = appInfoMapper.getAppInfo(id, null);
		if (appInfo.getLogoLocPath() != null && !appInfo.getLogoLocPath().equals("")) {
			File file = new File(appInfo.getLogoLocPath());
			if (file.exists()) {
				if (!file.delete())
					throw new Exception();
			}
		}
		// <2> ɾ��app_info������
		if (appInfoMapper.deleteAppInfoById(id) > 0) {
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean appsysUpdateSaleStatusByAppId(AppInfo appInfoObj) throws Exception {
		/*
		 * �ϼܣ� 1 ����status�ɡ�2 or 5�� to 4 �� �ϼ�ʱ�� 2 ����versionid ���� publishStauts Ϊ 2
		 * 
		 * �¼ܣ� ����status ��4��Ϊ5
		 */

		Integer operator = appInfoObj.getModifyBy();
		if (operator < 0 || appInfoObj.getId() < 0) {
			throw new Exception();
		}

		AppInfo appInfo = appInfoMapper.getAppInfo(appInfoObj.getId(), null);
		if (null == appInfo) {
			return false;
		} else {
			switch (appInfo.getStatus()) {
			case 2: // ��״̬Ϊ���ͨ��ʱ�����Խ����ϼܲ���
				onSale(appInfo, operator, 4, 2);
				break;
			case 5:// ��״̬Ϊ�¼�ʱ�����Խ����ϼܲ���
				onSale(appInfo, operator, 4, 2);
				break;
			case 4:// ��״̬Ϊ�ϼ�ʱ�����Խ����¼ܲ���
				offSale(appInfo, operator, 5);
				break;

			default:
				return false;
			}
		}
		return true;
	}

	private void onSale(AppInfo appInfo, Integer operator, Integer appInfStatus, Integer versionStatus)
			throws Exception {
		offSale(appInfo, operator, appInfStatus);
		setSaleSwitchToAppVersion(appInfo, operator, versionStatus);
	}

	private boolean offSale(AppInfo appInfo, Integer operator, Integer appInfStatus) throws Exception {
		AppInfo _appInfo = new AppInfo();
		_appInfo.setId(appInfo.getId());
		_appInfo.setStatus(appInfStatus);
		_appInfo.setModifyBy(operator);
		_appInfo.setOffSaleDate(new Date(System.currentTimeMillis()));
		appInfoMapper.modify(_appInfo);
		return true;
	}

	private boolean setSaleSwitchToAppVersion(AppInfo appInfo, Integer operator, Integer saleStatus) throws Exception {
		AppVersion appVersion = new AppVersion();
		appVersion.setId(appInfo.getVersionId());
		appVersion.setPublishStatus(saleStatus);
		appVersion.setModifyBy(operator);
		appVersion.setModifyDate(new Date(System.currentTimeMillis()));
		appVersionMapper.modify(appVersion);
		return false;
	}

	@Override
	public boolean modify(AppInfo appInfo) throws Exception {
		boolean flag = false;
		if (appInfoMapper.modify(appInfo) > 0) {
			flag = true;
		}
		return flag;
	}

}
