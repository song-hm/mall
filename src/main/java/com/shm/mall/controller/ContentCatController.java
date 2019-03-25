package com.shm.mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shm.mall.content.service.ContentCategoryService;
import com.shm.mall.pojo.EasyUITreeNode;
import com.shm.mall.utils.MallResult;

/**
 * 内容管理分类controller
 * @author SHM
 *
 */
@Controller
public class ContentCatController {

	@Autowired
	private ContentCategoryService contentCategoryService;
	
	/**
	 * 节点展示
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> showContentCatList(@RequestParam(name="id",defaultValue="0")long parentId){
		List<EasyUITreeNode> contentCatList = contentCategoryService.getContentCatList(parentId);
		return contentCatList;
	}
	
	/**
	 * 添加分类节点
	 * @param parentId
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public MallResult createContentCategory(long parentId,String name) {
		MallResult result = contentCategoryService.addContentCategory(parentId, name);
		return result;
		
	}
	
	/**
	 * 重命名节点
	 * @param id
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/content/category/update",method=RequestMethod.POST)
	@ResponseBody
	public MallResult updateContentCategory(long id,String name) {
		MallResult result = contentCategoryService.updateContentCategory(id,name);
		return result;
	}
	
	@RequestMapping(value="/content/category/delete/",method=RequestMethod.POST)
	@ResponseBody
	public MallResult deleteContentCategory(long id) {
		MallResult result = contentCategoryService.deleteContentCategory(id);
		return result;
	}
	
}
