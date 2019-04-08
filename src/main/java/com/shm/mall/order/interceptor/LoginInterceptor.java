package com.shm.mall.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.shm.mall.cart.service.CartService;
import com.shm.mall.pojo.TbItem;
import com.shm.mall.pojo.TbUser;
import com.shm.mall.sso.service.TokenService;
import com.shm.mall.utils.CookieUtils;
import com.shm.mall.utils.JsonUtils;
import com.shm.mall.utils.MallResult;

public class LoginInterceptor implements HandlerInterceptor {

	@Value("${SSO_URL}")
	private String SSO_URL;
	@Autowired 
	private TokenService tokenService;
	@Autowired
	private CartService cartService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		//判断token是否存在
		if(StringUtils.isBlank(token)) {
			//如果去不到token，则用户是未登录状态，跳转到登录页面，用户登录成功后，跳转到当前URL
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
			//拦截
			return false;
		}
		//如果token存在，则调用sso系统的服务，根据token取用户信息
		MallResult result = tokenService.getUserByToken(token);
		//如果取不到用户信息，则用户登录过期，调转到登录页面
		if(result.getStatus() != 200) {
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL());
			//拦截
			return false;
		}
		//如果取到用户信息，是登录状态，把用户信息写入request
		TbUser user= (TbUser) result.getData();
		request.setAttribute("user", user);
		//判断cookie中是否有购物车列表，如果有合并到服务端
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if(StringUtils.isNotBlank(json)) {
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(json, TbItem.class));
		}
		//放行
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
