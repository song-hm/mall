package com.shm.mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shm.mall.jedis.JedisClient;
import com.shm.mall.mapper.TbOrderItemMapper;
import com.shm.mall.mapper.TbOrderMapper;
import com.shm.mall.mapper.TbOrderShippingMapper;
import com.shm.mall.order.pojo.OrderInfo;
import com.shm.mall.order.service.OrderService;
import com.shm.mall.pojo.TbOrderItem;
import com.shm.mall.pojo.TbOrderShipping;
import com.shm.mall.utils.MallResult;

/**
 * 订单处理服务
 * @author SHM
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired 
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private JedisClient jedisClent;
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;
	@Value("${ORDER_DETAIL_ID_GEN_KEY}")
	private String ORDER_DETAIL_ID_GEN_KEY;
	
	@Override
	public MallResult createOrder(OrderInfo orderInfo) {
		// 生成订单号，使用Redis的incr生成
		if(!jedisClent.exists(ORDER_ID_GEN_KEY)) {
			jedisClent.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
		}
		String orderId = jedisClent.incr(ORDER_ID_GEN_KEY).toString();
		orderInfo.setOrderId(orderId);
		//补全orderInfo的属性
		//'状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭'
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//插入订单表
		orderMapper.insert(orderInfo);
		//插入订单明细表
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			//生成订单明细ID
			String odId= jedisClent.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
			//补全pojo属性
			tbOrderItem.setId(odId);
			tbOrderItem.setOrderId(orderId);
			//向明细表插入数据
			orderItemMapper.insert(tbOrderItem);
		}
		//插入订单物流表
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		
		//返回MallResult,包含订单号
		return MallResult.ok(orderId);
	}

}
