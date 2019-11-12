package com.jt.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jt.pojo.User;

public class CookieUtil {

	/**
	 * 编辑工具API方法,通过cookie的名称,获取cookie 的值
	 * 
	 */
	public static String GetCookieValue(HttpServletRequest request,String cookieName) {
		
//		Cookie[] cookies = request.getCookies();
//		if (cookies==null||cookies.length==0) {
//			//如果没有cookie ,则直接返回null
//			return null;
//			
//		}
//		String value =null;
//		for (Cookie cookie : cookies) {
//			if (cookie.getName().equals(cookieName)) {
//				 value = cookie.getValue();
//				 break;
//			}
//		}
//		return value;
		Cookie[] cookies = request.getCookies();
		if (cookies==null||cookies.length==0) {
			return null;
		}
		String value=null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieName)) {
				 value= cookie.getValue();
				break;
			}
			
		}
		return value;
	}
	
	/**
	 * 	Cookie cookie = new Cookie("JT_TICKET", ticket);
		cookie.setMaxAge(7*24*3600);
		cookie.setPath("/");
		cookie.setDomain("jt.com");
		response.addCookie(cookie);
	 */
	public static void addCook(HttpServletRequest request, HttpServletResponse response,String cookieName,String cookieValue,int seconds,String domain) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(seconds);
		cookie.setPath("/");
		cookie.setDomain(domain);
		response.addCookie(cookie);
	}
	
	
	
	
}
