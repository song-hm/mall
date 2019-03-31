package com.shm.mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.shm.mall.pojo.SearchItem;
import com.shm.mall.pojo.SearchResult;

/**
 * 商品搜索dao
 * @author SHM
 *
 */
@Repository
public class SearchDao{

	@Autowired
	private SolrServer solrServer;
	/**
	 * 根据查询条件查询索引库
	 * @param query
	 * @return
	 */
	public SearchResult search(SolrQuery query) throws Exception{
		//根据query查询索引库
		QueryResponse queryResponse = solrServer.query(query);
		//取查询结果
		SolrDocumentList results = queryResponse.getResults();
		//取结果总记录数
		long recordCount = results.getNumFound();
		SearchResult result = new SearchResult();
		result.setRecordCount(recordCount);
		//取商品列表，需要高亮显示
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		List<SearchItem> itemlist = new ArrayList<>();
		for (SolrDocument solrDocument : results) {
			SearchItem searchItem = new SearchItem();
			searchItem.setId((String) solrDocument.get("id"));
			//取高亮显示
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if(list != null && list.size() > 0) {
				title = list.get(0);
			}else {
				title = (String) solrDocument.get("item_title");
			}
			searchItem.setTitle(title);
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			//添加到商品列表
			itemlist.add(searchItem);
		}
		//返回结果
		result.setItemList(itemlist);
		return result;
	}
}
