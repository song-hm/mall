package com.shm.mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shm.mall.cart.service.CartService;
import com.shm.mall.pojo.TbItem;
import com.shm.mall.pojo.TbUser;
import com.shm.mall.service.ItemService;
import com.shm.mall.utils.CookieUtils;
import com.shm.mall.utils.JsonUtils;
import com.shm.mall.utils.MallResult;

/**
 * 购物车处理controller
 * @author SHM
 *
 */
@Controller
public class CartController {
	@Autowired
	private ItemService itemService;
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	@Autowired
	private CartService cartService;

	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId,@RequestParam Integer num,
			HttpServletRequest request,HttpServletResponse response) {
		//判断用户是否是登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		//是，则把购物车写入Redis
		if(user != null) {
			//保存服务端
			cartService.addCart(user.getId(), itemId, num);
			//返回逻辑视图
			return  "cartSuccess";
		}
		//未登录则写入cookie
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//判断商品在商品列表中是否存在
		Boolean flag = false;
		for (TbItem tbItem : cartList) {
			if(tbItem.getId() == itemId.longValue()) {
				flag = true;
				//如果存在数量相加
				tbItem.setNum(tbItem.getNum() + num);
				//跳出循环
				break;
			}
		}
		//如果不存在，根据商品ID查询商品信息，得到一个TbItem对象
		if(!flag) {
			TbItem tbItem = itemService.getItemById(itemId);
			//设置商品数量
			tbItem.setNum(num);
			//取一张图片
			String image = tbItem.getImage();
			if(StringUtils.isNotBlank(image)) {
				tbItem.setImage(image.split(",")[0]);
			}
			//把商品添加到商品列表
			cartList.add(tbItem);
			
		}
		//写入cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		//返回添加成功页面
		return "cartSuccess";
		
	}
	
	/**
	 * 从cookie中取购物车列表的方法
	 * @param request
	 * @return
	 */
	private List<TbItem> getCartListFromCookie(HttpServletRequest request){
		String json = CookieUtils.getCookieValue(request, "cart", true);
		//判断json是否为空
		if(StringUtils.isBlank(json)) {
			return new ArrayList<>();
		}
		//把json转换为商品列表
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	/**
	 * 展示购物车列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request,HttpServletResponse response) {
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		//如果是登录状态
		if(user != null) {
			//从cookie中取购物车列表
			//如果cookie中购物车列表不为空，则与服务端购物车合并
			cartService.mergeCart(user.getId(), cartList);
			//把cookie中的购物车删除
			CookieUtils.deleteCookie(request, response, "cart");
			//从服务端取购物车列表
			cartList = cartService.getCartList(user.getId());
		}
		
		//如果是未登录状态
		//把列表传递给页面
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "cart";
	}
	
	/**
	 * 更新购物车中商品数量
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public MallResult updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response) {
		
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		//如果是登录状态
		if(user != null) {
			cartService.updateCartNum(user.getId(), itemId, num);
			return MallResult.ok();
		}
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//遍历商品列表找到对应商品
		for (TbItem tbItem : cartList) {
			if(tbItem.getId().longValue() == itemId) {
				//更新数量
				tbItem.setNum(num);
				break;
			}
		}
		//把购物车列表写回cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		//返回成功
		return MallResult.ok();
	}
	
	/**
	 * 删除购物车中商品
	 * @param itemId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartList(@PathVariable Long itemId,
			HttpServletRequest request,HttpServletResponse response) {
		
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		//如果是登录状态
		if(user != null) {
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartListFromCookie(request);
		//遍历列表，找到要删除的商品
		for (TbItem tbItem : cartList) {
			if(tbItem.getId().longValue() == itemId) {
				//删除商品
				cartList.remove(tbItem);
				//跳出循环
				break;
			}
		}
		//把购物车列表写回cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		//返回逻辑视图
		return "redirect:/cart/cart.html";
	}
}
