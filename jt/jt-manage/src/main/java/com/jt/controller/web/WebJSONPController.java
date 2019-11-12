package com.jt.controller.web;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;

@RestController
public class WebJSONPController {

	/**
	 * 要求返回值结果callback(json)
	 * http://manage.jt.com/web/testJSONP?callback=jQuery111106503931970821475_1571108226262&_=1571108226263
	 */
//	@RequestMapping("/web/testJSONP")
//	public String jsonp(String callback) {
//		ItemDesc itemDesc = new ItemDesc();
//		
//		itemDesc.setItemId(10L)
//		.setItemDesc("跨域访问");
//		String json = ObjectMapperUtil.toJSON(itemDesc);
//		return callback+"("+json+")";
//	}
	
	@RequestMapping("/web/testJSONP")
	public JSONPObject jsonp(String callback) {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(1001L)
		.setItemDesc("跨域访问");
		return new JSONPObject(callback, itemDesc);
	}
	
	
	
	
	
	
}
