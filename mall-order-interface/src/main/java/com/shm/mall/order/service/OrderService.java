package com.shm.mall.order.service;

import com.shm.mall.order.pojo.OrderInfo;
import com.shm.mall.utils.MallResult;

public interface OrderService {

	MallResult createOrder(OrderInfo orderInfo);
}
