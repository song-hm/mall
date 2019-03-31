package com.shm.mall.search.service;

import com.shm.mall.pojo.SearchResult;

public interface SearchService {

	SearchResult search(String keyword,int page,int rows)throws Exception;
}
