package com.shm.mall.sso.service;

import com.shm.mall.pojo.TbUser;
import com.shm.mall.utils.MallResult;

public interface RegisterService {
	MallResult checkData(String param,int type);
	MallResult register(TbUser user);

}
