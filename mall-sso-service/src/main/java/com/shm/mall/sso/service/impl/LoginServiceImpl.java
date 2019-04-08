package com.shm.mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.shm.mall.jedis.JedisClient;
import com.shm.mall.mapper.TbUserMapper;
import com.shm.mall.pojo.TbUser;
import com.shm.mall.pojo.TbUserExample;
import com.shm.mall.pojo.TbUserExample.Criteria;
import com.shm.mall.sso.service.LoginService;
import com.shm.mall.utils.JsonUtils;
import com.shm.mall.utils.MallResult;

/**
 * 登录逻辑处理
 * @author SHM
 *
 */
@Service
public class LoginServiceImpl implements LoginService {

	@Autowired 
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public MallResult userLogin(String username, String password) {
		
		// 业务逻辑
		// 1、判断用户名和密码是否正确
		//根据用户名查询用户信息
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if(list == null || list.size() == 0) {
			//返回登陆失败
			return MallResult.build(400, "用户名或密码错误！");
		}
		//取用户信息
		TbUser user = list.get(0);
		//判断密码是否正确
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			// 2、如果不正确，返回登录失败
			return MallResult.build(400, "用户名或密码错误！");
		}
		// 3、如果正确，生成token，
		String token = UUID.randomUUID().toString();
		// 4、把用户名和密码写入Redis，key为token，value为用户信息
		user.setPassword(null);
		jedisClient.set("SESSION" + token, JsonUtils.objectToJson(user));
		// 5、设置session的过期时间
		jedisClient.expire("SESSION" + token, SESSION_EXPIRE);
		// 6、把token返回
		return MallResult.ok(token);
	}

}
