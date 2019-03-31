package com.shm.mall.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{

	private long recordCount;
	private  List<SearchItem> itemList;
	private int totalPages;
	public long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}
	public List<SearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	
}
