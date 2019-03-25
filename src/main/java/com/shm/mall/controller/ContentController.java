package com.shm.mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shm.mall.content.service.ContentService;
import com.shm.mall.pojo.EasyUIDataGridResult;
import com.shm.mall.pojo.TbContent;
import com.shm.mall.utils.MallResult;

@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(long categoryId,int page,int rows) {
		EasyUIDataGridResult result = contentService.getcontentList(categoryId, page, rows);
		return result;
	}
	
	/**
	 * 删除内容
	 * @param ids
	 * @return
	 */
	@RequestMapping("/content/delete")
	@ResponseBody
	public MallResult deleteContent(@RequestParam("ids") Long[] ids) {
		MallResult result = contentService.deleteContent(ids);
		return result;
	}
	
	/**
	 * 编辑内容
	 * @param content
	 * @return
	 */
	@RequestMapping(value="/rest/content/edit",method=RequestMethod.POST)
	@ResponseBody
	public MallResult editContent(TbContent content) {
		MallResult result = contentService.updateContent(content);
		return result;
	}
	
	/**
	 * 添加内容
	 * @param content
	 * @return
	 */
	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	@ResponseBody
	public MallResult addContent(TbContent content) {
		MallResult result = contentService.addContent(content);
		return result;
	}
	
}
