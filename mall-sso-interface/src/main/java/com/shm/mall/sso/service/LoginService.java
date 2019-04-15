package com.shm.mall.sso.service;

import com.shm.mall.utils.MallResult;

public interface LoginService {

	//参数：用户名和密码
	/**
	 * 业务逻辑
	 * 1、判断用户名和密码是否正确
	 * 2、如果不正确，返回登录失败
	 * 3、如果正确，生成token，
	 * 4、把用户名和密码写入Redis，key为token，value为用户信息
	 * 5、设置session的过期时间
	 * 6、把token返回
	 * 
	 */
	//返回值 MallResult,其中包含token信息
	MallResult userLogin(String username,String password);
	MallResult userLogout(String token);
}
