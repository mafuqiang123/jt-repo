package com.jt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;

public class TestRedis {
	
	private Jedis jedis;
	
	
//	private Jedis jedis;
	
//	@Before//测试 test方法之前执行
//	public void Init() {
//		 jedis=new Jedis("192.168.38.129",6379);
//	}
//	@Before
//	public void Init() {
//		jedis=new Jedis("192.168.38.129", 6379);
//	}
	/**
	 * 1.ip端口检查
	 * 2.linux防火墙是否关闭
	 * 3.redistribution的配置文件三处
	 * 4.redis启动方式 redis-server redis-conf
	 * 
	 * 分布式锁：redis创建一个锁
	 * jedis.setnx
	 * 
	 * jedis.setnx()给锁一个时间 防止死锁
	 * 
	 * 
	 * @throws InterruptedException 
	 */
	@Test
	public void testString() throws InterruptedException {
		//Jedis jedis=new Jedis("192.168.38.129",6379);

		jedis.set("1906", "redis入门案例");
		String value = jedis.get("1906");
		System.out.println(value);
		
		
		//2.测试key相同时,value是否覆盖
		
	//	jedis.set("1906","redis测试");
		//System.out.println(jedis.get("1906"));
		
		jedis.set("1906", "ceshi");
		System.out.println(jedis.get("1906"));
		
		
		
		//3.如果值已经存在则不允许覆盖
		/*
		 * jedis.setnx("1906", "NBA不转播了!!!"); System.out.println(jedis.get("1906"));
		 */
		jedis.setnx("1906", "NBA不转播了");
		System.out.println(jedis.get("1906"));
		
		
		//4.为数据设定超时时间
		//jedis.set("time", "超时测试");
		//jedis.expire("time", 60);
		jedis.set("time", "超时时间");
		jedis.expire("time", 60);
		
		
		//保证数据操作的有效性(原子性)
		/*
		 * jedis.setex("time", 100, "超时测试"); Thread.sleep(3000);
		 * 
		 * Long time = jedis.ttl("time"); System.out.println("当前数据还能存活"+time);
		 */
		jedis.setex("time", 100, "超时测试");
		//Thread.sleep(3000);
		//Long time = jedis.ttl("time");
		//System.out.println("还能存活time:"+time);
//		for (; ; ) {
			Thread.sleep(3000);
			Long time = jedis.ttl("time");
			System.out.println("还能存活time:"+time);
//		}
		
		
	   //5.要求key存在时不允许操作，并且设定超时时间
		//nx:不允许覆盖 xx:可以覆盖
		//ex:单位秒  px:单位毫秒
		
		  jedis.set("时间", "测试是否有效", "NX", "EX", 100);
		   System.out.println(jedis.get("时间"));
		 
	}
	
	/**
	 * 2.测试hash
	 */
	@Test
	public void testHash() {
//		jedis.hset("person","id","100");
//		jedis.hset("person", "name","超人");
//		System.out.println(jedis.hgetAll("person"));
		jedis.hset("person", "id", "100");
		jedis.hset("person", "name","超人");
		System.out.println(jedis.hgetAll("person"));
	}	
	
	/**
	 * 3.测试list集合
	 * 队列
	 */
	@Test
	public void testList() {
//		jedis.rpush("list", "1,2,3,4");
//		System.out.println(jedis.lpop("list"));
//	jedis.rpush("list", "1","2","3","4");
//		System.out.println(jedis.lpop("list"));
//		jedis.rpush("list", "1","2","3","4");
//		System.out.println(jedis.lpop("list"));
		jedis.rpush("list", "1","2","3","4");
		System.out.println(jedis.lpop("list"));
		
	}
	
	
/**
 * 4.控制事务
 */
	@Test
	public void testTx() {
		//开启事务
//		Transaction transaction = jedis.multi();
//		try {
//			transaction.set("a", "aaa");
//			transaction.set("b", "bbb");
//			transaction.set("c", "ccc");
//             			int a=1/0;
//			//2.事务提交
//            transaction.exec();
//		} catch (Exception e) {
//             e.printStackTrace();
//             //3.事务回滚
//           //  transaction.exec();
//           transaction.discard();
//		}
		Transaction transaction=null;
		//开启事务
		try {
			transaction = jedis.multi();
			transaction.set("a", "aaa");
			transaction.set("b", "bbb");
			transaction.set("c", "ccc");
//			int a=1/0;
			//tijiaoshiwu
			transaction.exec();
		} catch (Exception e) {
        e.printStackTrace();
        transaction.discard();
		}
	}
	
	/**
	 * 实现redis分片操作
	 * 
	 * 
	 */
	@Test
	public void testShards() {
		List<JedisShardInfo> list=new ArrayList<>();
		list.add(new JedisShardInfo("192.168.38.129",6379));
		list.add(new JedisShardInfo("192.168.38.129",6380));
		list.add(new JedisShardInfo("192.168.38.129",6381));
		ShardedJedis jedis =new ShardedJedis(list);
		jedis.set("1906", "redis分片测试");
		System.out.println(jedis.get("1906"));
	}
	
	
	
	
	
	/**
	 * 测试哨兵
	 * 调用原理:
	 * 用户通过哨兵,连接redis的主机,进行操作
	 * masterName:主机变量名称
	 * sentinels:redis节点的信息
	 * set<String>
	 */
	@Test
	public void testSentinel() {
		Set<String> sentinels=new HashSet<>();
		sentinels.add("192.168.38.129:26379");
		JedisSentinelPool sentinelPool
           =new JedisSentinelPool("mymaster", sentinels);		
		Jedis jedis = sentinelPool.getResource();
		jedis.set("1906", "哨兵测试成功");
		System.out.println(jedis.get("1906"));
	}
	
	@Test
	public void testCluster() {
		HashSet<HostAndPort> node = new HashSet<>();
		node.add(new HostAndPort("192.168.38.129",7000 ));
		node.add(new HostAndPort("192.168.38.129", 7001));
		node.add(new HostAndPort("192.168.38.129", 7002));
		node.add(new HostAndPort("192.168.38.129", 7003));
		node.add(new HostAndPort("192.168.38.129", 7004));
		node.add(new HostAndPort("192.168.38.129", 7005));
		JedisCluster  cluster=new JedisCluster(node);
		cluster.set("1906", "redis集群");
		System.out.println(cluster.get("1906")); 
	}
}