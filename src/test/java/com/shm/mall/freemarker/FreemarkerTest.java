package com.shm.mall.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerTest {

	@Test
	public void testFreemarker() throws Exception{
		//创建一个模板文件
		//创建一个configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//设置模板文件保存的目录
		configuration.setDirectoryForTemplateLoading(new File("D:\\Program Files\\eclipse\\workspace2\\mall-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
		//设置模板文件的编码格式，一般就是utf-8
		configuration.setDefaultEncoding("utf-8");
		//加载一个模板文件，创建一个模板对象
//		Template template = configuration.getTemplate("hello.ftl");
		Template template = configuration.getTemplate("student.ftl");
		//创建一个数据集，可以是pojo也可以是map。推荐使用map
		Map data = new HashMap<>();
		data.put("hello", "hello freemarker!");
		//创建一个pojo对象
		Student student = new Student(1, "张无忌", 26, "光明顶");
		data.put("student", student);
		//添加一个list
		List<Student> stuList = new ArrayList<>();
		stuList.add(new Student(1, "张无忌", 26, "光明顶"));
		stuList.add(new Student(2, "小龙女", 25, "古墓"));
		stuList.add(new Student(3, "杨过", 24, "古墓"));
		stuList.add(new Student(4, "黄蓉", 26, "桃花岛"));
		stuList.add(new Student(5, "郭靖", 27, "襄阳城"));
		stuList.add(new Student(6, "郭襄", 17, "襄阳城"));
		stuList.add(new Student(7, "赵敏", 22, "蒙古"));
		stuList.add(new Student(8, "周芷若", 22, "峨嵋山"));
		data.put("stuList", stuList);
		//添加一个日期类型
		data.put("date", new Date());
		//null值的测试
		data.put("val","123");
		//创建一个writer对象，指定输出文件的路径及文件名
//		Writer out = new FileWriter("D:\\Own\\file\\temp\\freemarker\\hello.txt");
		Writer out = new FileWriter("D:\\Own\\file\\temp\\freemarker\\student.html");
		//生成静态页面
		template.process(data, out);
		//关闭流
		out.close();
	}
}
