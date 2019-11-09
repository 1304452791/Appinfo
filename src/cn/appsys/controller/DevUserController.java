package cn.appsys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.DevUser;
import cn.appsys.service.devUser.DevUserService;

@Controller
public class DevUserController {

	@Resource
	private DevUserService devUserService;

	@RequestMapping(value = "/devlogin.html")
	public String devlogin() {
		return "devlogin";
	}

	@RequestMapping(value = "/dologin.html")
	public String dologin(@RequestParam String devCode, @RequestParam String devPassword, HttpSession session,
			HttpServletRequest request) {
		DevUser devUser = devUserService.login(devCode);
		if (devUser != null && devPassword.equals(devUser.getDevPassword())) {
			session.setAttribute("devUserSession", devUser);
			return "/developer/main";
		} else {
			request.setAttribute("error", "用户名或密码错误");
			return "devlogin";
		}
	}

	@RequestMapping(value = "/dev/logout.html")
	public String logOut(HttpServletRequest request) {
		request.getSession().removeAttribute("devUserSession");
		return "redirect:/devlogin.html";
	}
	
}
