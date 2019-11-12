package com.jt.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {

	private static final ObjectMapper MAPPER=new ObjectMapper();
	/**
	 * 对象转化为json串
	 * 
	 */
	public static String toJSON(Object target) {
		String result=null;
		try {
			result=MAPPER.writeValueAsString(target);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}	
	
	
	public static <T> T toObject(String json,Class<T> targetClass) {
		T object=null;
		try {
			object=MAPPER.readValue(json,targetClass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return object;
	}
	
	
	
	
	
	
}
