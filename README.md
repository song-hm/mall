# mall
宜立方网上商城  

##  后台管理系统

1、功能  

管理商品、订单、类目、商品规格属性、用户管理以及内容发布等功能。  

2、工程搭建-后台工程  

a)	使用maven搭建工程  

mall-parent：父工程，打包方式pom，管理jar包的版本号。  
    |           项目中所有工程都应该继承父工程。  
|--mall-common：通用的工具类通用的pojo。打包方式jar  
|--mall-manager：服务层工程。聚合工程。Pom工程  
|--mall-manager-dao：打包方式jar  
|--mall-manager-pojo：打包方式jar  
|--mall-manager-interface：打包方式jar  
|--mall-manager-service：打包方式：jar  
    |--mall-manager-web：表现层工程。打包方式war  
b)	使用maven的tomcat插件启动工程  
3、SSM框架整合
数据库
数据库使用mysql数据库
1、在mysql数据库中创建数据库mall
2、将创建数据库的脚本导入到mall中。
Mybatis逆向工程
使用mybatis官方提供的mybatis-generator生成pojo、mapper接口及映射文件。
并且将pojo放到mall-manager-pojo工程中。
将mapper接口及映射文件放到mall-manager-dao工程中。
整合
1、Dao层：
Mybatis的配置文件：SqlMapConfig.xml
不需要配置任何内容，需要有文件头。文件必须存在。
applicationContext-dao.xml：
mybatis整合spring，通过由spring创建数据库连接池，spring管理SqlSessionFactory、mapper代理对象。需要mybatis和spring的整合包。
2、Service层：
applicationContext-service.xml：
所有的service实现类都放到spring容器中管理。并由spring管理事务。

3、表现层：
Springmvc框架，由springmvc管理controller。
Springmvc的三大组件。
二、
1、服务中间件dubbo
Dubbo是资源调度和治理中心的管理工具。
Dubbo采用全Spring配置方式，透明化接入应用，对应用没有任何API侵入，只需用Spring加载Dubbo的配置即可，Dubbo基于Spring的Schema扩展进行加载。
Zookeeper是Apacahe Hadoop的子项目，是一个树型的目录服务，支持变更推送，适合作为Dubbo服务的注册中心，工业强度较高，可用于生产环境，并推荐使用
Zookeeper：
1、可以作为集群的管理工具使用。
2、可以集中管理配置文件。

2、项目改造为基于SOA架构
1.1.1.	拆分工程
1）将表现层工程独立出来：
mall-manager-web
2）将原来的mall-manager改为如下结构
mall-manager
   |--mall-manager-dao
   |--mall-manager-interface
   |--mall-manager-pojo
   |--mall-manager-service（打包方式改为war）
1.1.2.	服务层工程
第一步：把mall-manager的pom文件中删除mall-manager-web模块。
第二步：把mall-manager-web文件夹移动到mall-manager同一级目录。
第三步：mall-manager-service的pom文件修改打包方式
<packaging>war</packaging>
第四步：在mall-manager-service工程中添加web.xml文件
第五步：把mall-manager-web的配置文件复制到mall-manager-service中。
删除springmvc.xml
第六步：web.xml 中只配置spring容器。删除前端控制器
第七步：发布服务
1、在mall-manager-Service工程中添加dubbo依赖的jar包。
2、在spring的配置文件中添加dubbo的约束，然后使用dubbo:service发布服务。
<!-- 引用dubbo服务 -->
	<dubbo:application name="mall-manager-web"/>
	<dubbo:registry protocol="zookeeper" address="192.168.25.128:2181 "/>	
	<dubbo:reference interface="com.shm.mall.service.ItemService" id="itemService" />
表现层工程
改造mall-manager-web工程。
第一步：删除mybatis、和spring的配置文件。只保留springmvc.xml
第二步：修改mall-manager-web的pom文件，
修改parent为mall-parent
添加spring和springmvc的jar包的依赖
删除mall-mangager-service的依赖
添加dubbo的依赖
mall-mangager-web添加对mall-manager-Interface的依赖。
第三步：修改springmvc.xml，在springmvc的配置文件中添加服务的引用。
	<!-- 引用dubbo服务 -->
	<dubbo:application name="mall-manager-web"/>
	<dubbo:registry protocol="zookeeper" address="192.168.25.128:2181"/>	
	<dubbo:reference interface=" com.shm.mall.service.ItemService" id="itemService" />
