package cn.appsys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.BackendUser;
import cn.appsys.service.backenduser.BackendUserService;

@Controller
@RequestMapping(value = "/manager")
public class UserLoginController {
	private Logger logger = Logger.getLogger(UserLoginController.class);

	@Resource
	private BackendUserService backendUserService;

	@RequestMapping(value = "/backendlogin.html")
	public String login() {
		logger.debug("LoginController welcome AppInfoSystem backend==================");
		return "backendlogin";
	}

	@RequestMapping(value = "/dologin", method = RequestMethod.POST)
	public String doLogin(@RequestParam String userCode, @RequestParam String userPassword, HttpServletRequest request,
			HttpSession session) {
		logger.debug("doLogin====================================");
		BackendUser user = null;
		try {
			user = backendUserService.login(userCode, userPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != user) {
			session.setAttribute("userSession", user);
			return "redirect:/manager/backend/main";
		} else {
			request.setAttribute("error", "");
			return "backendlogin";
		}
	}

	@RequestMapping(value = "/backend/main")
	public String main(HttpSession session) {
		if (session.getAttribute("userSession") == null) {
			return "redirect:/manager/login";
		}
		return "backend/main";
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("userSession");
		return "backendlogin";
	}
}
