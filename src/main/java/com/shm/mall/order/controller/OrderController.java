package com.shm.mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shm.mall.cart.service.CartService;
import com.shm.mall.order.pojo.OrderInfo;
import com.shm.mall.order.service.OrderService;
import com.shm.mall.pojo.TbItem;
import com.shm.mall.pojo.TbUser;
import com.shm.mall.utils.MallResult;

@Controller
public class OrderController {

	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/order/order-cart")
	public String showOrder(HttpServletRequest request) {
		//从request中取用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		//根据用户去收货地址列表
		//当前使用静态数据
		//取支付方式列表 当前静态数据
		//去购物车列表
		List<TbItem> cartList = cartService.getCartList(user.getId());
		//把购物车列表传递给页面
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "order-cart";		
	}
	
	@RequestMapping(value="/order/create",method=RequestMethod.POST)
	public String  createOrder(OrderInfo orderInfo,HttpServletRequest request) {
		//取用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		//把用户信息添加到orderInfo
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		
		//调用服务生成订单
		MallResult result = orderService.createOrder(orderInfo);
		//如果订单生成成功，需要删除购物车
		if(result.getStatus() == 200) {
			cartService.clearCartItem(user.getId());
		}
		//把订单号传递给页面
		request.setAttribute("orderId",result.getData());
		request.setAttribute("payment",orderInfo.getPayment());
		//返回逻辑视图
		return "success";
	}
}
