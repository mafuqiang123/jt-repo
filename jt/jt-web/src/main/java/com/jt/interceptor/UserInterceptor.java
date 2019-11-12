package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Jedis;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.ThreadLocalUtil;

import redis.clients.jedis.JedisCluster;

@Component // 将对象交给spring容器管理
public class UserInterceptor implements HandlerInterceptor {

	/**
	 * 方法执行之前 boolean:是否放行 放行:true
	 */
	/**
	 * 实现用户不登陆,不能访问购物车 实现思路: 1.获取cookie 2.获取redis
	 * 
	 */
	private static final String USER = "JT-USER";

	@Autowired
	private JedisCluster JedisCluster;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String ticket = CookieUtil.GetCookieValue(request, "JT_TICKET");

		if (!StringUtils.isEmpty(ticket)) {

			String userJSON = JedisCluster.get(ticket);
			if (!StringUtils.isEmpty(userJSON)) {
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
				// user对象怎么传递给controller后台服务器
//				request.setAttribute(USER, user);
				ThreadLocalUtil.set(user);
				System.out.println("用户信息保存到域中");
				return true;// 表示程序放行
			}
		}

//		System.out.println("方法执行之前执行");
		// 一般拦截器中的false和重定向联用
		// 应该重定向到登陆的页面/user/login.html
		response.sendRedirect("/user/login.html");
		return false;// 表示拦截
	}

	/**
	 * 方法执行之后执行 类似于afterreturning
	 */

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		System.out.println("方法执行之后:post");
	}

	/**
	 * 方法完成的最后阶段
	 */

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		ThreadLocalUtil.remove();
		// request.removeAttribute(USER);
		System.out.println("用户数据删除");
	}

}
