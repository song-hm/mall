package com.shm.mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shm.mall.search.service.SearchItemService;
import com.shm.mall.utils.MallResult;

/**
 * 导入商品到索引库
 * @author SHM
 *
 */
@Controller
public class SearchItemController {

	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public MallResult importSearchItem() {
		MallResult result = searchItemService.importAllItems();
		return result;
	}
}
