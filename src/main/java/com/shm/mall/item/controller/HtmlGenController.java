package com.shm.mall.item.controller;


import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 生成静态页面测试controller
 * @author SHM
 *
 */
@Controller
public class HtmlGenController {

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("/genhtml")
	@ResponseBody
	public String genhtml() throws Exception{
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		//加载模板对象
		Template template = configuration.getTemplate("hello.ftl");
		//创建一个数据集
		Map data = new HashMap<>();
		data.put("hello", "1234567890");
		//指定文件输出的路径及文件名
		Writer out = new FileWriter("D:\\Own\\file\\temp\\freemarker\\hello1.txt");
		//输出文件
		template.process(data, out);
		//关闭流
		out.close();
		return "ok";
	}
}
