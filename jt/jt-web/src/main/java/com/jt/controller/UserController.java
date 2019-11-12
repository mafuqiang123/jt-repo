package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.remoting.exchange.Response;
import com.jt.dubbo.service.DubboUserService;
import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.IPUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {

	@Reference(check = false) // 不需要校验
	private DubboUserService userService;

	private static final String TICKET = "JT_TICKET";

	// 注册:http://www.jt.com/user/register.html

	// 登陆:http://www.jt.com/user/login.html

	@RequestMapping("/{moduleName}")
	public String module(@PathVariable String moduleName) {

		return moduleName;
	}

	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user) {

		userService.saveUser(user);
		return SysResult.success();
	}

	// http://www.jt.com/user/doLogin?r=0.511832985833224

	/**
	 * ticketCookie.setMaxAge(>0)存活的生命周期 单位秒 setMaxAge(0);要求cookie立即删除
	 * setMaxAge(-1);当会话关闭时,删除cookie
	 * 
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("doLogin")
	@ResponseBody
	public SysResult doLogin(HttpServletRequest request,User user, HttpServletResponse response) {
		//1.获取用户ip信息
		String ip = IPUtil.getIpAddr(request);	
		
		//2.完成用户信息的校验
		String ticket = userService.doLogin(user,ip);
		if (StringUtils.isEmpty(ticket)) {
			return SysResult.fail();
		}
		
		//3.将用户信息写入cookie
		CookieUtil.addCook(request, response, "JT_TICKET", ticket, 7*24*3600, "jt.com");
		CookieUtil.addCook(request, response, "JT_USERNAME", user.getUsername(), 7*24*3600, "jt.com");
		return SysResult.success();
		
	}
//	public SysResult doLogin(User user, HttpServletResponse response) {
//		String ticket = userService.doLogin(user);
//		System.out.println("ticket:" + ticket);
//		if (StringUtils.isEmpty(ticket)) {
//			// 表示返回值不正确,给用户提示信息
//			return SysResult.fail("用户名或密码错误");
//		}
//		// 将ticket保存到客户端的cookie();
//
//		Cookie ticketCookie = new Cookie(TICKET, ticket);
//		ticketCookie.setMaxAge(7 * 24 * 3600);// 表示七天有效
//		// cookie的权限设定(路径)
//		ticketCookie.setPath("/");
//		// cookie实现共享
//		ticketCookie.setDomain("jt.com");// 只要网站名称为jt.com 就可以访问这个cookie 实现共享
//		response.addCookie(ticketCookie);
//		return SysResult.success();
//	}

	/**
	 * 前提:获取cookies 0.首先应该获取JT_TICKET的cookie的值ticket
	 * 
	 * 1.删除cookie 名称为:JT_TICKET
	 * 
	 * 2.删除redis 根据ticket值
	 * 
	 * @return
	 */

	@Autowired
	private JedisCluster jedisCluster;

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response) {

		Cookie[] cookies = request.getCookies();
		String ticket = null;
		String username=null;
		if (cookies.length>0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("JT_USERNAME")) {
					username = cookie.getValue();
				}
				if (cookie.getName().equals("JT_TICKET")) {
					ticket = cookie.getValue();
				}
			}
			
			
		}
		if (!StringUtils.isEmpty(ticket)) {
			jedisCluster.del(ticket);
			CookieUtil.addCook(request, response, "JT_TICKET", "", 0, "jt.com");
		}
		
		if (!StringUtils.isEmpty(username)) {
			jedisCluster.del(username);
			CookieUtil.addCook(request, response, "JT_USERNAME", "", 0, "jt.com");
		}

		return "redirect:/";
	}

}
