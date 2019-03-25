package com.shm.mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shm.mall.pojo.EasyUITreeNode;
import com.shm.mall.service.ItemCatService;

/**
 * 商品分类管理的controller
 * @author SHM
 *
 */
@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(
			@RequestParam(name="id",defaultValue="0")Long parentId){
		//调用服务查询节点列表
		List<EasyUITreeNode> list  = itemCatService.getItemCatList(parentId);
		return list;
	}
}
