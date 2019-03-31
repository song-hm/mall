package com.shm.mall.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {

	@Test
	public void testAddDocument() throws Exception{
		//创建一个集群的连接，应该使用CloudSolrServer创建
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.128:2281,192.168.25.128:2282,192.168.25.128:2283");
		//zkHost：zookeeper的地址列表
		//设置一个defaultCollection的属性
		solrServer.setDefaultCollection("collection2");
		//创建文档对象
		SolrInputDocument document = new SolrInputDocument();
		//向文档中添加域
		document.setField("id", "solrCloud01");
		document.setField("item_title", "测试商品1");
		//把文件写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	@Test
	public void testQuery() throws Exception{
		//创建一个CloudSolrServer对象
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.128:2281,192.168.25.128:2282,192.168.25.128:2283");
		//设置默认的collection
		solrServer.setDefaultCollection("collection2");
		//创建一个查询对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery("*:*");
		//执行查询
		QueryResponse queryResponse = solrServer.query(query);
		//取查询结果		
		SolrDocumentList results = queryResponse.getResults();
		System.out.println("总记录数"+results.getNumFound());
		//打印
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.getFieldValue("item_title"));
		}
		
	}
}
