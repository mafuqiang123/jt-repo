package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

//标识配置类
@Configuration
@PropertySource("classpath:/properties/redis.properties") // 引入配置文件
public class RedisConfig {
//	@Value("${redis.nodes}")
//	private String nodes;// node,node,node

	@Value("${redis.nodes}")
	private String nodes;



	/**
	 * 配置哨兵
	 * 
	 * @return
	 */
//	@Bean // 动态为参数赋值 自动加autowired
//	@Scope("prototype") // 多例对象
//	public Jedis Jedis(JedisSentinelPool sentinelPool) {
//		return sentinelPool.getResource();
//	}

//	@Bean
//	public JedisSentinelPool jedisSentinelPool() {
//		Set<String> sentinels = new HashSet<>();
//		sentinels.add(nodes);
//		JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels);
//		return sentinelPool;
//	}

	
	/**
	 * 
	 * 配置集群
	 */
	@Bean
	public JedisCluster jedisCluster() {
		Set<HostAndPort> nodeSet = getNode();
		return new JedisCluster(nodeSet);
	}

	private HashSet<HostAndPort> getNode() {
		HashSet<HostAndPort> nodess = new HashSet<>();
		String[] arrayNodes = nodes.split(",");
		for (String node : arrayNodes) {
			String[] split2 = node.split(":");
			String host = split2[0];
			Integer port = Integer.parseInt(split2[1]);
			nodess.add(new HostAndPort(host, port));
		}
		return nodess;
	}

//	@Bean
//	public ShardedJedis shardedJedis() {
//		List<JedisShardInfo> shards = getList();
//		return new ShardedJedis(shards);
//	}

//	private List<JedisShardInfo> getList() {
//		List<JedisShardInfo> list = new ArrayList<>();
//
//		String[] arrayNodes = nodes.split(",");
//		for (String node : arrayNodes) {
//			String host = node.split(":")[0];
//			int port = Integer.parseInt(node.split(":")[1]);
//			list.add(new JedisShardInfo(host, port));
//		}
//		return list;
//	}
	///////////////////////////////////////////////
//	@Bean
//	public ShardedJedis getJedis() {
//
//		ArrayList<JedisShardInfo> list = getListd();
//		return new ShardedJedis(list);
//	}
//
//	private ArrayList<JedisShardInfo> getListd() {
//		ArrayList<JedisShardInfo> list = new ArrayList<>();
//		String[] arrayNodes = nodes.split(",");
//		for (String redis : arrayNodes) {
//			String[] split = redis.split(":");
//			String host=split[0];
//			String port=split[1];
//			list.add(new JedisShardInfo(host, port));
//		}
//		return list;
//	}
	////////////////////////////////////////////

//	@Value("${redis.host}")
//	private String host;
//	@Value("${redis.port}")
//	private Integer port;

	// <bean id="jedis" class="com.jt.config.Jedis">
//	@Bean
//	public Jedis jedis() {
//		return new Jedis(host, port);
//		
//	}	
//	List<JedisShardInfo> list=new ArrayList<>();
//	list.add(new JedisShardInfo("192.168.38.129",6379));
//	list.add(new JedisShardInfo("192.168.38.129",6380));
//	list.add(new JedisShardInfo("192.168.38.129",6381));
//	ShardedJedis jedis =new ShardedJedis(list);
//	jedis.set("1906", "redis分片测试");
//	System.out.println(jedis.get("1906"));

}
