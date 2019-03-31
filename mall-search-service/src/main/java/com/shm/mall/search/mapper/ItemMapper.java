package com.shm.mall.search.mapper;

import java.util.List;

import com.shm.mall.pojo.SearchItem;

public interface ItemMapper {

	List<SearchItem> getItemList();
	SearchItem getItemById(long itemId);
}
