package com.shm.mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shm.mall.pojo.EasyUIDataGridResult;
import com.shm.mall.pojo.TbItem;
import com.shm.mall.service.ItemDescService;
import com.shm.mall.service.ItemService;
import com.shm.mall.utils.MallResult;

/**
 * 商品管理的Controller
 * @author SHM
 *
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private ItemDescService itemDescService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	
	/**
	 * 商品列表
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows) {
		//调用服务查询商品列表
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
		
	}
	
	/**
	 * 商品添加功能
	 */
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public MallResult addItem(TbItem item,String desc) {
		MallResult result = itemService.addItem(item, desc);
		return result;
	}
	
	/**
	 * 商品删除
	 */
	/*@RequestMapping(value="/rest/item/delete",params= {"ids"})
	@ResponseBody
	public MallResult deleteItem(@RequestParam("ids") Long itemId) {
		MallResult result = itemService.deleteItemById(itemId);
		result = itemDescService.deleteItemDescById(itemId);
		return result;
	}*/
	
	/**
	 * 商品下架
	 * 
	 */
	/*@RequestMapping(value="/rest/item/instock",params= {"ids"})
	@ResponseBody
	public MallResult downItem(@RequestParam("ids") Long itemId) {
		MallResult result = itemService.downItem(itemId);
		return result;
	}*/
	
	/**
	 * 商品批量删除
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value="/rest/item/delete",params= {"ids"})
	@ResponseBody
	public MallResult deleteItem(@RequestParam("ids") long[] itemId,TbItem item) {
		
		MallResult result = itemService.deleteItems(itemId,item);
//		result = itemDescService.deleteItemDesc(itemId);
		return result;
	}
	
	/**
	 * 商品批量下架
	 * 
	 */
	@RequestMapping(value="/rest/item/instock",method=RequestMethod.POST)
	@ResponseBody
	public MallResult instockItem(@RequestParam("ids") long[] itemId,TbItem item) {
		MallResult result = itemService.instockItem(itemId, item);
		return result;
	}
	
	/**
	 * 商品批量上架
	 * 
	 */
	@RequestMapping(value="/rest/item/reshelf",method=RequestMethod.POST)
	@ResponseBody
	public MallResult reshelfItem(@RequestParam("ids") long[] itemId,TbItem item) {
		MallResult result = itemService.reshelfItem(itemId, item);
		return result;
	}
	
	/**
	 * 加载商品描述
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value="/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	public MallResult editItem(@PathVariable long itemId) {
		MallResult result = itemDescService.getItemDescById(itemId);
		return result;
	}
	
	/**
	 * 商品更新
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping(value="/rest/item/update",method=RequestMethod.POST)
	@ResponseBody
	public MallResult updateItem(TbItem item,String desc) {
		MallResult result = itemService.updateItem(item,desc);
		return result;
	}
}
