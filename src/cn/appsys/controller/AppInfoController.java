package cn.appsys.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DevUser;
import cn.appsys.pojo.Page;
import cn.appsys.service.appCategory.AppCategoryService;
import cn.appsys.service.appInfo.AppInfoService;
import cn.appsys.service.appVersion.AppVersionService;
import cn.appsys.service.dataDictionary.DataDictionaryService;

@Controller
@RequestMapping(value = "/dev")
public class AppInfoController {

	private Logger logger = Logger.getLogger(DevUserController.class);

	@Resource
	private DataDictionaryService dataDictionaryService;

	@Resource
	private AppCategoryService appCategoryService;

	@Resource
	private AppInfoService appInfoService;

	@Resource
	private AppVersionService appVersionService;

	/**
	 * 加载数据
	 * 
	 * @param model
	 * @param querySoftwareName
	 * @param queryStatus
	 * @param queryFlatformId
	 * @param queryCategoryLevel1
	 * @param queryCategoryLevel2
	 * @param queryCategoryLevel3
	 * @param pageIndex
	 * @return
	 */
	@RequestMapping(value = "/appinfolist.html")
	public String applist(Model model,
			@RequestParam(value = "querySoftwareName", required = false) String querySoftwareName,
			@RequestParam(value = "queryStatus", required = false) String queryStatus,
			@RequestParam(value = "queryFlatformId", required = false) String queryFlatformId,
			@RequestParam(value = "queryCategoryLevel1", required = false) String queryCategoryLevel1,
			@RequestParam(value = "queryCategoryLevel2", required = false) String queryCategoryLevel2,
			@RequestParam(value = "queryCategoryLevel3", required = false) String queryCategoryLevel3,
			@RequestParam(value = "pageIndex", required = false) String pageIndex) {
		// APP状态
		Integer status = null;
		// 所属平台
		Integer flatformId = null;
		// 一级菜单
		Integer categoryLevel1 = null;
		// 二级菜单
		Integer categoryLevel2 = null;
		// 三级菜单
		Integer categoryLevel3 = null;
		// 设置页面的大小
		Integer pageSize = 5;
		// 当前页码
		Integer currentPageNo = 1;

		List<DataDictionary> statusList = null;
		List<DataDictionary> flatFormList = null;
		List<AppCategory> categoryLevel1List = null;
		List<AppCategory> categoryLevel2List = null;
		List<AppCategory> categoryLevel3List = null;
		List<AppInfo> appInfoList = null;

		if (queryStatus != null && queryStatus != ("")) {
			status = Integer.parseInt(queryStatus);
		}
		if (queryFlatformId != null && queryFlatformId != ("")) {
			flatformId = Integer.parseInt(queryFlatformId);
		}
		if (queryCategoryLevel1 != null && queryCategoryLevel1 != ("")) {
			categoryLevel1 = Integer.parseInt(queryCategoryLevel1);
		}
		if (queryCategoryLevel2 != null && queryCategoryLevel2 != ("")) {
			categoryLevel2 = Integer.parseInt(queryCategoryLevel2);
		}
		if (queryCategoryLevel3 != null && queryCategoryLevel3 != ("")) {
			categoryLevel3 = Integer.parseInt(queryCategoryLevel3);
		}
		if (pageIndex != null && pageIndex != ("")) {
			currentPageNo = Integer.parseInt(pageIndex);
		}

		int totalCount = appInfoService.getAppInfoCount(querySoftwareName, status, flatformId, categoryLevel1,
				categoryLevel2, categoryLevel3);

		// 封装
		Page pages = new Page();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();

		// 控制首页和尾页
		if (currentPageNo < 1) {
			currentPageNo = 1;
		} else if (currentPageNo > totalPageCount) {
			currentPageNo = totalPageCount;
		}

		// 查询数据
		statusList = dataDictionaryService.getDataDictionaryByTypeCode("APP_STATUS");
		flatFormList = dataDictionaryService.getDataDictionaryByTypeCode("APP_FLATFORM");
		categoryLevel1List = appCategoryService.getAppCategoryByParentId(null);
		if (queryCategoryLevel1 != null && queryCategoryLevel1 != "") {
			categoryLevel2List = appCategoryService.getAppCategoryByParentId(Integer.parseInt(queryCategoryLevel1));
		}
		if (queryCategoryLevel2 != null && queryCategoryLevel2 != "") {
			categoryLevel3List = appCategoryService.getAppCategoryByParentId(Integer.parseInt(queryCategoryLevel2));
		}
		appInfoList = appInfoService.getAppInfoList(querySoftwareName, status, flatformId, categoryLevel1,
				categoryLevel2, categoryLevel3, (currentPageNo - 1) * pageSize, pageSize);

		model.addAttribute("pages", pages);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("queryFlatformId", queryFlatformId);
		model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
		model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
		model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
		model.addAttribute("statusList", statusList);
		model.addAttribute("flatFormList", flatFormList);
		model.addAttribute("categoryLevel1List", categoryLevel1List);
		model.addAttribute("categoryLevel2List", categoryLevel2List);
		model.addAttribute("categoryLevel3List", categoryLevel3List);
		model.addAttribute("appInfoList", appInfoList);

		return "/developer/appinfolist";
	}

	/**
	 * 跳转到添加AppInfo
	 * 
	 * @param appInfo
	 * @return
	 */
	@RequestMapping("/appinfoadd.html")
	public String add(@ModelAttribute("appInfo") AppInfo appInfo) {
		return "developer/appinfoadd";
	}

	/**
	 * 判断ApkName是否存在
	 * 
	 * @param APKName
	 * @return
	 */
	@RequestMapping(value = "apkexist", method = RequestMethod.GET)
	@ResponseBody
	public Object ApkExist(@RequestParam String APKName) {
		Map<String, String> data = new HashMap<String, String>();
		if (APKName == null || APKName == "") {
			data.put("APKName", "empty");
		} else {
			if (appInfoService.ApkExist(APKName) != null) {
				data.put("APKName", "exist");
			} else {
				data.put("APKName", "noexist");
			}
		}
		return JSONArray.toJSONString(data);
	}

	/**
	 * AppInfo保存
	 * 
	 * @param appInfo
	 * @param session
	 * @param request
	 * @param attach
	 * @return
	 */
	@RequestMapping(value = "/appinfoaddsave", method = RequestMethod.POST)
	public String addSave(AppInfo appInfo, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "a_logoPicPath", required = false) MultipartFile attach) {
		System.out.println("================================" + appInfo.getSoftwareName());
		String logoPicPath = null;
		String logoLocPath = null;
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + java.io.File.separator + "uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();
			String prefix = FilenameUtils.getExtension(oldFileName);
			int filesize = 500000;
			if (attach.getSize() > filesize) {
				request.setAttribute("fileUploadError", " * 上传文件过大！");
				return "developer/appinfoadd";
			} else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")) {
				String fileName = appInfo.getAPKName() + ".jpg";
				File targetFile = new File(path, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError", " * 上传失败！");
					return "developer/appinfoadd";
				}
				logoPicPath = request.getContextPath() + "/statics/uploadfiles/" + fileName;
				logoLocPath = path + File.separator + fileName;
			} else {
				request.setAttribute("fileUploadError", " * 上传文件格式不正确！");
				return "developer/appinfoadd";
			}
		}

		appInfo.setCreatedBy(((DevUser) session.getAttribute("devUserSession")).getId());
		appInfo.setCreationDate(new Date());
		appInfo.setLogoPicPath(logoPicPath);
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setDevId(((DevUser) session.getAttribute("devUserSession")).getId());
		appInfo.setStatus(1);
		try {
			if (appInfoService.addAppInfo(appInfo) > 0) {
				return "redirect:/dev/appinfolist.html";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/dev/appinfoadd.html";
	}

	/**
	 * 跳转到修改AppInfo页面
	 * 
	 * @param id
	 * @param fileUploadError
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/appinfomodify", method = RequestMethod.GET)
	public String AppInfoModify(@RequestParam("id") String id,
			@RequestParam(value = "error", required = false) String fileUploadError, Model model) {
		AppInfo appInfo = null;
		logger.debug("modifyAppInfo --------- id: " + id);
		if (null != fileUploadError && fileUploadError.equals("error1")) {
			fileUploadError = " * APK信息不完整！";
		} else if (null != fileUploadError && fileUploadError.equals("error2")) {
			fileUploadError = " * 上传失败！";
		} else if (null != fileUploadError && fileUploadError.equals("error3")) {
			fileUploadError = " * 上传文件格式不正确！";
		} else if (null != fileUploadError && fileUploadError.equals("error4")) {
			fileUploadError = " * 上传文件过大！";
		}
		try {
			appInfo = appInfoService.getAppInfo(Integer.parseInt(id), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute(appInfo);
		model.addAttribute("fileUploadError", fileUploadError);
		return "developer/appinfomodify";
	}

	/**
	 * 修改保存
	 * 
	 * @param appInfo
	 * @param session
	 * @param request
	 * @param attach
	 * @return
	 */
	@RequestMapping(value = "/appinfomodifysave", method = RequestMethod.POST)
	public String modifySave(AppInfo appInfo, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "attach", required = false) MultipartFile attach) {
		String logoPicPath = null;
		String logoLocPath = null;
		String APKName = appInfo.getAPKName();
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();// 原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
			int filesize = 500000;
			if (attach.getSize() > filesize) {// 上传大小不得超过 50k
				return "redirect:/dev/flatform/app/appinfomodify?id=" + appInfo.getId() + "&error=error4";
			} else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")) {// 上传图片格式
				String fileName = APKName + ".jpg";// 上传LOGO图片命名:apk名称.apk
				File targetFile = new File(path, fileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appinfomodify?id=" + appInfo.getId() + "&error=error2";
				}
				logoPicPath = request.getContextPath() + "/statics/uploadfiles/" + fileName;
				logoLocPath = path + File.separator + fileName;
			} else {
				return "redirect:/dev/flatform/app/appinfomodify?id=" + appInfo.getId() + "&error=error3";
			}
		}
		appInfo.setModifyBy(((DevUser) session.getAttribute("devUserSession")).getId());
		appInfo.setModifyDate(new Date());
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setLogoPicPath(logoPicPath);
		try {
			if (appInfoService.modify(appInfo)) {
				return "redirect:/dev/appinfolist.html";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "developer/appinfomodify";
	}

	/**
	 * 修改时删除logo
	 * 
	 * @param flag
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delfile", method = RequestMethod.GET)
	@ResponseBody
	public Object delFile(@RequestParam(value = "flag", required = false) String flag,
			@RequestParam(value = "id", required = false) String id) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		String fileLocPath = null;
		if (flag == null || flag.equals("") || id == null || id.equals("")) {
			resultMap.put("result", "failed");
		} else if (flag.equals("logo")) {// 删除logo图片（操作app_info）
			try {
				fileLocPath = (appInfoService.getAppInfo(Integer.parseInt(id), null).getLogoLocPath());
				File file = new File(fileLocPath);
				if (file.exists())
					if (file.delete()) {// 删除服务器存储的物理文件
						if (appInfoService.deleteAppLogo(Integer.parseInt(id))) {// 更新表
							resultMap.put("result", "success");
						}
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (flag.equals("apk")) {// 删除apk文件（操作app_version）
			try {
				fileLocPath = (appVersionService.getAppVersionById(Integer.parseInt(id))).getApkLocPath();
				File file = new File(fileLocPath);
				if (file.exists())
					if (file.delete()) {// 删除服务器存储的物理文件
						if (appVersionService.deleteApkFile(Integer.parseInt(id))) {// 更新表
							resultMap.put("result", "success");
						}
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return JSONArray.toJSONString(resultMap);
	}

	/**
	 * 跳转到添加App版本信息页面
	 * 
	 * @param appId
	 * @param fileUploadError
	 * @param appVersion
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/appversionadd", method = RequestMethod.GET)
	public String addVersion(@RequestParam(value = "id") String appId,
			@RequestParam(value = "error", required = false) String fileUploadError, AppVersion appVersion,
			Model model) {
		logger.debug("fileUploadError============> " + fileUploadError);
		if (null != fileUploadError && fileUploadError.equals("error1")) {
			fileUploadError = " * APK信息不完整！";
		} else if (null != fileUploadError && fileUploadError.equals("error2")) {
			fileUploadError = " * 上传失败！";
		} else if (null != fileUploadError && fileUploadError.equals("error3")) {
			fileUploadError = " * 上传文件格式不正确！";
		}
		appVersion.setAppId(Integer.parseInt(appId));
		List<AppVersion> appVersionList = null;
		try {
			appVersionList = appVersionService.getAppVersionList(Integer.parseInt(appId));
			appVersion.setAppName((appInfoService.getAppInfo(Integer.parseInt(appId), null)).getSoftwareName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("appVersionList", appVersionList);
		model.addAttribute(appVersion);
		model.addAttribute("fileUploadError", fileUploadError);
		return "developer/appversionadd";
	}

	/**
	 * 保存App版本信息
	 * 
	 * @param appVersion
	 * @param session
	 * @param request
	 * @param attach
	 * @return
	 */
	@RequestMapping(value = "/addversionsave", method = RequestMethod.POST)
	public String addVersionSave(AppVersion appVersion, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "a_downloadLink", required = false) MultipartFile attach) {
		String downloadLink = null;
		String apkLocPath = null;
		String apkFileName = null;
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();// 原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
			if (prefix.equalsIgnoreCase("apk")) {// apk文件命名：apk名称+版本号+.apk
				String apkName = null;
				try {
					apkName = appInfoService.getAppInfo(appVersion.getAppId(), null).getAPKName();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if (apkName == null || "".equals(apkName)) {
					return "redirect:/dev/flatform/app/appversionadd?id=" + appVersion.getAppId() + "&error=error1";
				}
				apkFileName = apkName + "-" + appVersion.getVersionNo() + ".apk";
				File targetFile = new File(path, apkFileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appversionadd?id=" + appVersion.getAppId() + "&error=error2";
				}
				downloadLink = request.getContextPath() + "/statics/uploadfiles/" + apkFileName;
				apkLocPath = path + File.separator + apkFileName;
			} else {
				return "redirect:/dev/flatform/app/appversionadd?id=" + appVersion.getAppId() + "&error=error3";
			}
		}
		appVersion.setCreatedBy(((DevUser) session.getAttribute("devUserSession")).getId());
		appVersion.setCreationDate(new Date());
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(apkFileName);
		try {
			if (appVersionService.appsysadd(appVersion)) {
				return "redirect:/dev/appinfolist.html";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/dev/flatform/app/appversionadd?id=" + appVersion.getAppId();
	}

	/**
	 * 跳转到修改App版本信息页面
	 * 
	 * @param versionId
	 * @param appId
	 * @param fileUploadError
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/appversionmodify", method = RequestMethod.GET)
	public String modifyAppVersion(@RequestParam("vid") String versionId, @RequestParam("aid") String appId,
			@RequestParam(value = "error", required = false) String fileUploadError, Model model) {
		AppVersion appVersion = null;
		List<AppVersion> appVersionList = null;
		if (null != fileUploadError && fileUploadError.equals("error1")) {
			fileUploadError = " * APK信息不完整！";
		} else if (null != fileUploadError && fileUploadError.equals("error2")) {
			fileUploadError = " * 上传失败！";
		} else if (null != fileUploadError && fileUploadError.equals("error3")) {
			fileUploadError = " * 上传文件格式不正确！";
		}
		try {
			appVersion = appVersionService.getAppVersionById(Integer.parseInt(versionId));
			appVersionList = appVersionService.getAppVersionList(Integer.parseInt(appId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute(appVersion);
		model.addAttribute("appVersionList", appVersionList);
		model.addAttribute("fileUploadError", fileUploadError);
		return "developer/appversionmodify";
	}

	/**
	 * App版本信息保存
	 * 
	 * @param appVersion
	 * @param session
	 * @param request
	 * @param attach
	 * @return
	 */
	@RequestMapping(value = "/appversionmodifysave", method = RequestMethod.POST)
	public String modifyAppVersionSave(AppVersion appVersion, HttpSession session, HttpServletRequest request,
			@RequestParam(value = "attach", required = false) MultipartFile attach) {

		String downloadLink = null;
		String apkLocPath = null;
		String apkFileName = null;
		if (!attach.isEmpty()) {
			String path = request.getSession().getServletContext()
					.getRealPath("statics" + File.separator + "uploadfiles");
			logger.info("uploadFile path: " + path);
			String oldFileName = attach.getOriginalFilename();// 原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
			if (prefix.equalsIgnoreCase("apk")) {// apk文件命名：apk名称+版本号+.apk
				String apkName = null;
				try {
					apkName = appInfoService.getAppInfo(appVersion.getAppId(), null).getAPKName();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if (apkName == null || "".equals(apkName)) {
					return "redirect:/dev/flatform/app/appversionmodify?vid=" + appVersion.getId() + "&aid="
							+ appVersion.getAppId() + "&error=error1";
				}
				apkFileName = apkName + "-" + appVersion.getVersionNo() + ".apk";
				File targetFile = new File(path, apkFileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/dev/flatform/app/appversionmodify?vid=" + appVersion.getId() + "&aid="
							+ appVersion.getAppId() + "&error=error2";
				}
				downloadLink = request.getContextPath() + "/statics/uploadfiles/" + apkFileName;
				apkLocPath = path + File.separator + apkFileName;
			} else {
				return "redirect:/dev/flatform/app/appversionmodify?vid=" + appVersion.getId() + "&aid="
						+ appVersion.getAppId() + "&error=error3";
			}
		}
		appVersion.setModifyBy(((DevUser) session.getAttribute("devUserSession")).getId());
		appVersion.setModifyDate(new Date());
		appVersion.setDownloadLink(downloadLink);
		appVersion.setApkLocPath(apkLocPath);
		appVersion.setApkFileName(apkFileName);
		try {
			if (appVersionService.modify(appVersion)) {
				return "redirect:/dev/appinfolist.html";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "developer/appversionmodify";
	}

	/**
	 * 删除App信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delapp.json")
	@ResponseBody
	public Object delApp(@RequestParam String id) {
		logger.debug("delApp appId===================== " + id);
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if (StringUtils.isNullOrEmpty(id)) {
			resultMap.put("delResult", "notexist");
		} else {
			try {
				if (appInfoService.appsysdeleteAppById(Integer.parseInt(id)))
					resultMap.put("delResult", "true");
				else
					resultMap.put("delResult", "false");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return JSONArray.toJSONString(resultMap);
	}

	/**
	 * 查看app信息，包括app基本信息和版本信息列表（跳转到查看页面）
	 * 
	 * @param appInfo
	 * @return
	 */
	@RequestMapping(value = "/appview/{id}", method = RequestMethod.GET)
	public String view(@PathVariable String id, Model model) {
		AppInfo appInfo = null;
		List<AppVersion> appVersionList = null;
		try {
			appInfo = appInfoService.getAppInfo(Integer.parseInt(id), null);
			appVersionList = appVersionService.getAppVersionList(Integer.parseInt(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("appVersionList", appVersionList);
		model.addAttribute(appInfo);
		return "developer/appinfoview";
	}

	@RequestMapping(value = "/{appid}/sale", method = RequestMethod.PUT)
	@ResponseBody
	public Object sale(@PathVariable String appid, HttpSession session) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		Integer appIdInteger = 0;
		try {
			appIdInteger = Integer.parseInt(appid);
		} catch (Exception e) {
			appIdInteger = 0;
		}
		resultMap.put("errorCode", "0");
		resultMap.put("appId", appid);
		if (appIdInteger > 0) {
			try {
				DevUser devUser = (DevUser) session.getAttribute("devUserSession");
				AppInfo appInfo = new AppInfo();
				appInfo.setId(appIdInteger);
				appInfo.setModifyBy(devUser.getId());
				if (appInfoService.appsysUpdateSaleStatusByAppId(appInfo)) {
					resultMap.put("resultMsg", "success");
				} else {
					resultMap.put("resultMsg", "success");
				}
			} catch (Exception e) {
				resultMap.put("errorCode", "exception000001");
			}
		} else {
			resultMap.put("errorCode", "param000001");
		}
		return resultMap;
	}

}
