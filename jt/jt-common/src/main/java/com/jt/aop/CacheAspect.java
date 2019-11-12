package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.anno.Cache_Find;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

@Aspect//标识切面
@Component//将该类交给spring容器管理
public class CacheAspect {

	//当前切面位于common中,必须添加required=false
//	private Jedis jedis;
	@Autowired(required=false)
	private JedisCluster jedis;//集群
//	private Jedis jedis;//哨兵
	
	//private Jedis jedis;//哨兵机制
	//private ShardedJedis jedis; 分片机制
//	@Pointcut()
//	public void aspect() {}
	
	
	/**
	 * 通知的选择;
	 * 是否需要控制目标方法执行,使用环绕通知 如果要控制
	 * 步骤:
	 * 1.key查缓存 key 
	 * 动态生成key 包名.类名.方法名::parentId
	 * 
	 */
	
	
	
	@Around("@annotation(cacheFind)")//直接获取注解的对象
	public Object around(ProceedingJoinPoint jointPoint,Cache_Find cacheFind) {
		
		String key=getKey(jointPoint,cacheFind);
		String result = jedis.get(key);
		Object data=null;
    
			try {
				if (StringUtils.isEmpty(result)) {
			 //缓存没数据,目标方法执行查询数据库
					data=jointPoint.proceed();
					String value=ObjectMapperUtil.toJSON(data);
					if (cacheFind.seconds()==0) {
						jedis.set(key, value);
					}else {
						jedis.setex(key, cacheFind.seconds(), value);
					}
					System.out.println("AOP数据库查询");
					
				}else {
					//h缓存中有数据
					Class returnClass=getClass(jointPoint);
					data=ObjectMapperUtil.toObject(result, returnClass);
					System.out.println("AOP查询缓存");
				}
			} catch (Throwable e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		return data;
	}

//获取方法返回值类型
	private Class getClass(ProceedingJoinPoint jointPoint) {
		MethodSignature signature=(MethodSignature)jointPoint.getSignature();
		return signature.getReturnType();
		
	}


	private String getKey(ProceedingJoinPoint jointPoint, Cache_Find cacheFind) {
             //判断用户是否定义key值
		String key = cacheFind.key();
		if (!StringUtils.isEmpty(key)) {
         return key;//返回用户自己定义的key			
		}
		//包名.类名.方法名::parentId
		//表示需要自动生成key
		String className = jointPoint.getSignature().getDeclaringTypeName();//class name
        String methodName=jointPoint.getSignature().getName();		
        Object[] args=jointPoint.getArgs();
        key=className+"."+methodName+"::"+args[0];
		return key;
	}
	
}
