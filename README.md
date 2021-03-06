# mall

* 宜立方网上商城
* 宜立方网上商城是一个综合性的B2C平台，类似京东商城、天猫商城。

## 1.	功能列表

* 后台管理系统：管理商品、订单、类目、商品规格属性、用户管理以及内容发布等功能。
* 前台系统：用户可以在前台系统中进行注册、登录、浏览商品、首页、下单等操作。
* 会员系统：用户可以在该系统中查询已下的订单等信息。
* 订单系统：提供下单、查询订单、修改订单状态。
* 搜索系统：提供商品的搜索功能。
* 单点登录系统：为多个系统之间提供用户登录凭证以及查询登录用户的信息。

## 2. 基于soa的架构

* SOA：Service Oriented Architecture面向服务的架构。也就是把工程拆分成服务层、表现层两个工程。
* 服务层中包含业务逻辑，只需要对外提供服务即可。表现层只需要处理和页面的交互，业务逻辑都是调用服务层的服务来实现。</br>

![SOA](https://github.com/song-hm/mall/blob/master/mallPic/SOA%E6%9E%B6%E6%9E%84.png)  

## 3.	宜立方商城系统架构

![系统架构](https://github.com/song-hm/mall/blob/master/mallPic/%E7%B3%BB%E7%BB%9F%E6%9E%B6%E6%9E%84.png)

## 4.技术架构
* 前端：EasyUI
* 框架：SpringMvc+Spring+Mybatis
 


