package com.shm.mall.content.service;

import java.util.List;

import com.shm.mall.pojo.EasyUIDataGridResult;
import com.shm.mall.pojo.TbContent;
import com.shm.mall.utils.MallResult;

public interface ContentService {

	EasyUIDataGridResult getcontentList(long categoryId,int page,int rows);

	MallResult deleteContent(Long[] ids);

	/**
	 * 更新内容
	 * @param content
	 * @return
	 */
	MallResult updateContent(TbContent content);
	
	MallResult addContent(TbContent content);
	
	/**
	 * 根据CategoryId获取内容列表
	 * @param cid
	 * @return
	 */
	List<TbContent> getContentListByCid(long cid);
}
