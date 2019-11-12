package com.jt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.util.HttpClientService;
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestHttpClient {
    /**
     * 1.实例化工具API的对象
     * 2.确定请求url地址
     * 3.定义请求的方式 get/post
     * 4.发起http请求,并且获取响应的数据
     * 5.判断状态码status是否为200
     * 6.获取服务器返回值数据
     * @throws IOException 
     * @throws ClientProtocolException 
     * 
     */
//	@Test
//	public void testGet() throws ClientProtocolException, IOException {
//		CloseableHttpClient client = HttpClients.createDefault();
//		String url="https://www.baidu.com/";
//		HttpGet get = new HttpGet(url);
////		HttpPost post = new HttpPost(url);
//		CloseableHttpResponse response = client.execute(get);
//		if (response.getStatusLine().getStatusCode()==200) {
//			String data=EntityUtils.toString(response.getEntity(),"utf-8");
//          System.out.println(data);//html代码
//		
//		}
//	}
	
	@Test
	public void testHet() throws Exception {
		
		CloseableHttpClient client = HttpClients.createDefault();
		String url="http://www.baidu.com";
		HttpGet get = new HttpGet(url);
		HttpPost post = new HttpPost();
		CloseableHttpResponse response = client.execute(get);
		if (response.getStatusLine().getStatusCode()==200) {
			String data = EntityUtils.toString(response.getEntity(), "UTF-8");
			System.out.println(data);//html代码
			
		}
		
		
	}
	
	
	
	
	@Autowired
	private HttpClientService Service;
	
	@Test
	public void testDoGet() {
		
		
		String url="https://www.baidu.com/";
		Map<String, String> params = new HashMap<>();
		params.put("name", "小马哥");
		params.put("id", "33");
		String result = Service.doGet(url, params, null);
		System.out.println(result);
		
		
	}
	
	
	
	
	
	
	
	
	
}
