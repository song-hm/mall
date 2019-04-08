package com.shm.mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shm.mall.jedis.JedisClient;
import com.shm.mall.pojo.TbUser;
import com.shm.mall.sso.service.TokenService;
import com.shm.mall.utils.JsonUtils;
import com.shm.mall.utils.MallResult;

/**
 * 根据token取用户信息
 * @author SHM
 *
 */
@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public MallResult getUserByToken(String token) {
		// 根据token到Redis取用户信息
		String json = jedisClient.get("SESSION" + token);
		//取不到用户信息，则说明token过期，返回登录过期
		if(StringUtils.isBlank(json)) {
			return MallResult.build(201, "用户登录已经过期");
		}
		//取到用户信息，更新token过期时间
		jedisClient.expire("SESSION" + token, SESSION_EXPIRE);
		//返回成功，返回MallResult包含user信息
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return MallResult.ok(user);
	}

}
