package com.shm.mall.service;

import com.shm.mall.pojo.EasyUIDataGridResult;
import com.shm.mall.pojo.TbItem;
import com.shm.mall.pojo.TbItemDesc;
import com.shm.mall.utils.MallResult;

public interface ItemService {

	TbItem getItemById(long itemId);
	EasyUIDataGridResult getItemList(int page,int rows);
	MallResult addItem(TbItem item,String desc);
//	MallResult deleteItemById(long itemId);
	MallResult deleteItem(long[] itemId);
//	MallResult downItem(long itemId);
	MallResult instockItem(long[] itemId,TbItem item);
	MallResult reshelfItem(long[] itemId,TbItem item);
	MallResult updateItem(TbItem item, String desc);
	
	TbItemDesc getItemDescById(long itemId);
}
