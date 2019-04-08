package com.shm.mall.sso.service;

import com.shm.mall.utils.MallResult;

/**
 * 根据token查询用户信息
 * @author SHM
 *
 */
public interface TokenService {

	MallResult getUserByToken(String token);
}
