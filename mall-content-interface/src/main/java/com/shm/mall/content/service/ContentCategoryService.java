package com.shm.mall.content.service;

import java.util.List;

import com.shm.mall.pojo.EasyUITreeNode;
import com.shm.mall.utils.MallResult;

public interface ContentCategoryService {

	List<EasyUITreeNode> getContentCatList(long parentId);
	MallResult addContentCategory(long parentId,String name);
	MallResult updateContentCategory(long id, String name);
	MallResult deleteContentCategory(long id);
}
