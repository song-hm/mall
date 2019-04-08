package com.shm.mall.cart.service;

import java.util.List;

import com.shm.mall.pojo.TbItem;
import com.shm.mall.utils.MallResult;

public interface CartService {
	MallResult addCart(long userId,long itemId,int num);
	MallResult mergeCart(long userId,List<TbItem> itemList);
	List<TbItem> getCartList(long userId);
	MallResult updateCartNum(long userId,long itemId,int num);
	MallResult deleteCartItem(long userId,long itemId);
	MallResult clearCartItem(long userId);
}
