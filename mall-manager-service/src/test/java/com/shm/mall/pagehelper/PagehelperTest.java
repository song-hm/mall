package com.shm.mall.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shm.mall.mapper.TbItemMapper;
import com.shm.mall.pojo.TbItem;
import com.shm.mall.pojo.TbItemExample;

/**
 * 测试pagehelper
 * @author SHM
 *
 */
public class PagehelperTest {

	@Test
	public void testPagehelper() throws Exception{
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//从容器中获得Mapper代理对象
		TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
		//执行SQL语句之前设置分页信息使用pagehelper的startPage方法
		PageHelper.startPage(1, 10);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);
		//取分页信息。使用pageInfo 1、总记录数 2、总页数 。当前页码
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		System.out.println(pageInfo.getTotal());
		System.out.println(pageInfo.getPages());
		System.out.println(pageInfo.getSize());
	}
}
