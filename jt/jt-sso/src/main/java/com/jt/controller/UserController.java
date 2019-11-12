package com.jt.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.rpc.Result;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.IPUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 根据用户信息实现数据的校验 返回值结果: true:已经存在, false:用户可以使用该数据
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param, @PathVariable Integer type, String callback) {

		boolean data = userService.checkUser(param, type);
		return new JSONPObject(callback, SysResult.success(data));
	}
	
	
	//JT_TICKET  
	/**
	 * 根据ticket信息,查询用户JSON信息,之后将数据回传给客户端
	 */
	@Autowired
	private JedisCluster jedisCluster;
	
//	@RequestMapping("/query/{ticket}")
//	public JSONPObject findByticket(@PathVariable String ticket,String callback) {
//		
//		String userJSON = jedisCluster.get(ticket);
//		
//		if (StringUtils.isEmpty(userJSON)) {
//			//用户使用的ticket有问题
//			return new JSONPObject(callback, SysResult.fail());
//		}
//		return new JSONPObject(callback, SysResult.success(userJSON));
//	}
	
	
	@RequestMapping("/query/{ticket}/{username}")
	public JSONPObject findByticket(@PathVariable String ticket,@PathVariable String username,String callback,HttpServletRequest request) {
		
		//1.校验用户的ip地址
		String ip=IPUtil.getIpAddr(request);
		
//		/当前用户正确的数据
		String localIP = jedisCluster.hget(username, "JT_IP");
		if (!ip.equalsIgnoreCase(localIP)) {
			return new JSONPObject(callback, SysResult.fail());
		}
		
		//2.校验ticket信息
		String localTicket=jedisCluster.hget(username, "JT_TICKET");
		if (!ticket.equals(localTicket)) {
			return new JSONPObject(callback, SysResult.fail());
		}
		
		//3.短信验证
		
		//4.人脸识别功能/指纹
		//3.说明用户信息正确
		String userJSON=jedisCluster.hget(username, "JT_USERJSON");
		
		return new JSONPObject(callback, SysResult.success(userJSON));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
