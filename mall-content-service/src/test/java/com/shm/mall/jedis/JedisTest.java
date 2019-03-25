package com.shm.mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {

	@Test
	public void testJedis() {
		//创建一个Jedis对象。需要两个参数：host、port
		Jedis jedis = new Jedis("192.168.25.128",6379);
		//直接使用jedis操作Redis。每一个jedis命令都对应一个方法
		jedis.set("test123", "my first jedis");
		String string = jedis.get("test123");
		System.out.println(string);
		//关闭连接
		jedis.close();
	}
	
	@Test
	public void testJedisPool() {
		//创建一个jedisPool 连接池
		JedisPool jedisPool = new JedisPool("192.168.25.128",6379);
		//使用连接池获取一个连接，就是一个jedis对象
		Jedis jedis = jedisPool.getResource();
		//使用jedis操作Redis
		String string = jedis.get("test123");
		System.out.println(string);
		//关闭连接，每次使用完关闭连接。连接池回收资源
		jedis.close();
		//关闭连接池
		jedisPool.close();
		
	}
	
	@Test
	public void testJedisCluster() {
		//创建一个jedisCluster对象,有一个参数是node是一个set类型，set中包含若干个HostAndPort对象
		Set nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.128",7001));
		nodes.add(new HostAndPort("192.168.25.128",7002));
		nodes.add(new HostAndPort("192.168.25.128",7003));
		nodes.add(new HostAndPort("192.168.25.128",7004));
		nodes.add(new HostAndPort("192.168.25.128",7005));
		nodes.add(new HostAndPort("192.168.25.128",7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		//直接使用jedisCluster对象操作Redis
		jedisCluster.set("test","123");
		String string = jedisCluster.get("test");
		System.out.println(string);
		//关闭JedisCluster对象
		jedisCluster.close();
	}
}
