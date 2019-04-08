package com.shm.mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shm.mall.cart.service.CartService;
import com.shm.mall.jedis.JedisClient;
import com.shm.mall.mapper.TbItemMapper;
import com.shm.mall.pojo.TbItem;
import com.shm.mall.utils.JsonUtils;
import com.shm.mall.utils.MallResult;

/**
 * 购物车处理service
 * @author SHM
 *
 */
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	@Autowired
	private TbItemMapper itemMapper;
	
	@Override
	public MallResult addCart(long userId, long itemId,int num) {
		// 把购物车存入Redis
		//数据类型是hash key:用户ID file：商品Id value:商品信息
		//判断商品是否存在
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE+":"+userId, itemId+"");
		//如果存在数量相加
		if(hexists) {
			String json = jedisClient.hget(REDIS_CART_PRE+":"+userId, itemId+"");
			//把json转换为TbItem
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum()+num);
			//写回Redis
			jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(item));
			return MallResult.ok();
		}
		//如果不存在通过ID查询商品信息，
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//设置商品数量
		item.setNum(num);
		//取一张图片
		String image = item.getImage();
		if(StringUtils.isNotBlank(image)) {
			item.setImage(image.split(",")[0]);
		}
		//添加到购物车列表
		jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		//返回成功
		return MallResult.ok();
	}

	@Override
	public MallResult mergeCart(long userId, List<TbItem> itemList) {
		// 遍历商品列表
		// 把列表添加到购物车
		// 如果购物车有，则商品数量相加
		// 如果没有，则将商品添加到购物车
		for (TbItem tbItem : itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		//返回成功
		return MallResult.ok();
	}

	@Override
	public List<TbItem> getCartList(long userId) {
		// 根据用户ID查询购物车列表
		List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE+":"+userId);
		List<TbItem> itemList = new ArrayList<>();
		for (String string : jsonList) {
			//创建一个item对象
			TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
			//添加到列表
			itemList.add(item);
		}
		return itemList;
	}

	@Override
	public MallResult updateCartNum(long userId, long itemId, int num) {
		// 从Redis中取购物车
		String json = jedisClient.hget(REDIS_CART_PRE+":"+userId, itemId+"");
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		//更新商品数量
		item.setNum(num);
		//写入Redis
		jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		return MallResult.ok();
	}

	@Override
	public MallResult deleteCartItem(long userId, long itemId) {
		// 从Redis中删除商品
		jedisClient.hdel(REDIS_CART_PRE+":"+userId, itemId+"");
		return MallResult.ok();
	}

	@Override
	public MallResult clearCartItem(long userId) {
		// 删除用户购物车信息
		jedisClient.del(REDIS_CART_PRE+":"+userId);
		return MallResult.ok();
	}

}
