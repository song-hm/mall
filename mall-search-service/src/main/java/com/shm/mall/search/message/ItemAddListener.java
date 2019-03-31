package com.shm.mall.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.shm.mall.pojo.SearchItem;
import com.shm.mall.search.mapper.ItemMapper;

/**
 * 监听商品添加消息，接收消息后，将对应的商品信息同步到索引库
 * @author SHM
 *
 */
public class ItemAddListener implements MessageListener {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	@Override
	public void onMessage(Message message) {
		
		try {
			// 从信息中取商品ID
			TextMessage textMessage =  (TextMessage) message;
			String text = textMessage.getText();
			Long itemId = new Long(text);
			//等待事务提交
			Thread.sleep(1000);
			//根据商品ID查询商品信息
			SearchItem searchItem= itemMapper.getItemById(itemId);
			//创建一个文档对象
			SolrInputDocument solrInputDocument = new SolrInputDocument();
			//向文档对象中添加域
			solrInputDocument.addField("id", searchItem.getId());
			solrInputDocument.addField("item_title", searchItem.getTitle());
			solrInputDocument.addField("item_sell_point",searchItem.getSell_point());
			solrInputDocument.addField("item_price",searchItem.getPrice());
			solrInputDocument.addField("item_image",searchItem.getImage());
			solrInputDocument.addField("item_category_name",searchItem.getCategory_name());
			//把文档写入索引库
			solrServer.add(solrInputDocument);
			//提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
