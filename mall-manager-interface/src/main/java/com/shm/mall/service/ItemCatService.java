package com.shm.mall.service;

import java.util.List;

import com.shm.mall.pojo.EasyUITreeNode;

public interface ItemCatService {
	List<EasyUITreeNode> getItemCatList(long parentId);
}
