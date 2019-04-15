package com.shm.mall.portal.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shm.mall.content.service.ContentService;
import com.shm.mall.pojo.TbContent;
import com.shm.mall.sso.service.LoginService;
import com.shm.mall.utils.CookieUtils;

/**
 * 首页展示controller
 * @author SHM
 *
 */
@Controller
public class IndexController {

	@Value("${CONTENT_LUNBO_ID}")
	private long CONTENT_LUNBO_ID;
	
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@Autowired
	private ContentService contentService;
	@Autowired
	private LoginService loginService;
	
	@RequestMapping("/index")
	public String showIndex(Model model) {
		List<TbContent> ad1List = contentService.getContentListByCid(CONTENT_LUNBO_ID);
		//把结果传递给页面
		model.addAttribute("ad1List", ad1List);
		return "index";
	}
	
	@RequestMapping("/user/logout")
	public String logout(String token,HttpServletRequest request,
			HttpServletResponse response,String callback) {
		token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		loginService.userLogout(token);
		
		CookieUtils.deleteCookie(request, response, token);
		return "redirect:/index.html";
		
//		String url = "/user/logout" + (callback == null ? "" : "?callback=" + callback);
//		return "redirect:"+url;
	}
}
