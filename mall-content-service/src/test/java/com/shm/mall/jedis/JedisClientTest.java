package com.shm.mall.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JedisClientTest {

	@Test
	public void testJedisClient()throws Exception{
		//初始化一个spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
		//从容器中获取jedisClient对象
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		jedisClient.set("mytest","jedisClient");
		String string = jedisClient.get("mytest");
		System.out.println(string);
	}
}
