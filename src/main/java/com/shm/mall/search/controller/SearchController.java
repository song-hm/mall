package com.shm.mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shm.mall.pojo.SearchResult;
import com.shm.mall.search.service.SearchService;

/**
 * 商品搜索的Controller
 * @author SHM
 *
 */
@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;
	@Value("${SEARCH_RESULT_ROWS}")
	private int SEARCH_RESULT_ROWS;
	
	@RequestMapping("/search")
	public String search(String keyword,
			@RequestParam(defaultValue="1")Integer page ,Model model) throws Exception {
		keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
		//查询商品列表
		SearchResult searchResult = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
		//把结果传递给页面
		model.addAttribute("query", keyword);
		model.addAttribute("page", page);
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("recourdCount", searchResult.getRecordCount());
		model.addAttribute("itemList", searchResult.getItemList());
		
		//异常测试
//		int a = 1/0;
		//返回逻辑视图
		return "search";		
	}
}
