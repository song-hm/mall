package com.shm.mall.service;

import com.shm.mall.utils.MallResult;

public interface ItemDescService {
//	MallResult deleteItemDescById(long itemId);
	MallResult deleteItemDesc(long[] itemId);

	MallResult getItemDescById(long itemId);
}
