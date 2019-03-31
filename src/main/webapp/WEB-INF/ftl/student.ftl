<html>
<head>
	<title>student</title>
</head>
<body>
	学生信息：<br/>
	学号：${student.id}&nbsp;&nbsp;&nbsp;&nbsp;
	姓名：${student.name}&nbsp;&nbsp;&nbsp;&nbsp;
	年龄：${student.age}&nbsp;&nbsp;&nbsp;&nbsp;
	家庭住址：${student.addr}&nbsp;&nbsp;&nbsp;&nbsp;<br/>
	学生列表：
	<table border="1">
		<tr>
			<th>序号</th>
			<th>学号</th>
			<th>姓名</th>
			<th>年龄</th>
			<th>家庭住址</th>			
		</tr>
		<#list stuList as stu>
		<#if stu_index % 2 == 0>
		<tr bgcolor="red">
		<#else>
		<tr bgcolor="green">
		</#if>
			<th>${stu_index}</th>
			<th>${stu.id}</th>
			<th>${stu.name}</th>
			<th>${stu.age}</th>
			<th>${stu.addr}</th>			
		</tr>
		</#list>
	</table>
	<br/>
	<!-- 可以使用?date,?time,?datetime,?string(pattern) -->
	当前日期：${date?string("yyy/MM/dd HH:mm:ss")}
	<br/>
	null值的处理：${val!"val的值为null"}<br/>
	判断val的值是否为空：<br/>
	<#if val??>
	val中有内容
	<#else>
	val的值为null
	</#if>
	<br/>
	引入模板测试：<br/>
	<#include "hello.ftl">
</body>
</html>

