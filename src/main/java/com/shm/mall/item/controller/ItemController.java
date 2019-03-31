package com.shm.mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shm.mall.item.pojo.Item;
import com.shm.mall.pojo.TbItem;
import com.shm.mall.pojo.TbItemDesc;
import com.shm.mall.service.ItemService;

/**
 * 商品详情展示Controller
 * @author SHM
 *
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable("itemId")long itemId,Model model) {
		TbItem tbItem = itemService.getItemById(itemId);
		Item item = new Item(tbItem);
		TbItemDesc itemDesc = itemService.getItemDescById(itemId);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
}
