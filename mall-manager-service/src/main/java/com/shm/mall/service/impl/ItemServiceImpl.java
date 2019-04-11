package com.shm.mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shm.mall.jedis.JedisClient;
import com.shm.mall.mapper.TbItemDescMapper;
import com.shm.mall.mapper.TbItemMapper;
import com.shm.mall.pojo.EasyUIDataGridResult;
import com.shm.mall.pojo.TbItem;
import com.shm.mall.pojo.TbItemDesc;
import com.shm.mall.pojo.TbItemDescExample;
import com.shm.mall.pojo.TbItemExample;
import com.shm.mall.pojo.TbItemExample.Criteria;
import com.shm.mall.service.ItemService;
import com.shm.mall.utils.IDUtils;
import com.shm.mall.utils.JsonUtils;
import com.shm.mall.utils.MallResult;

/**
 * 商品管理的的Service
 * @author SHM
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;
	
	@Override
	public TbItem getItemById(long itemId) {
		//添加查询缓存
		try {
			String string = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":BASE");
			if(StringUtils.isNotBlank(string)) {
				TbItem tbItem = JsonUtils.jsonToPojo(string, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存中没有。则查询数据
		//根据主键查询
//		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			//有结果则添加到缓存
			try {
				jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":BASE", JsonUtils.objectToJson(list.get(0)));
				//设置过期时间
				jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":BASE", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 商品列表
	 */
	@Override
	public EasyUIDataGridResult getItemList(int page,int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		//取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}
	
	/**
	 * 新增商品
	 */
	@Override
	public MallResult addItem(TbItem item, String desc) {
		//生成商品ID
		final long itemId = IDUtils.genItemId();
		//补全属性
		item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表插入数据
		itemMapper.insert(item);
		//创建一个商品描述pojo对象
		TbItemDesc itemDesc = new TbItemDesc();		
		//补全属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		//向商品描述表插入数据
		itemDescMapper.insert(itemDesc);
		jmsTemplate.send(topicDestination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId+"");
				return textMessage;
			}
		});
		//返回成功
		return MallResult.ok();
	}

	/**
	 * 删除商品
	 */
	/*@Override
	public MallResult deleteItemById(long itemId) {		
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		itemMapper.deleteByExample(example);
		return MallResult.ok();
	}*/

	/**
	 * 商品下架
	 */
	/*@Override
	public MallResult downItem(long itemId) {
		TbItem item = this.getItemById(itemId);
		//设置商品状态
		item.setStatus((byte)2);
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKey(item);
		
		return MallResult.ok();
	}*/

	/**
	 * 批量删除商品
	 */
	/*@Override
	public MallResult deleteItem(long[] itemId) {
		for (long l : itemId) {
			itemMapper.deleteByPrimaryKey(l);
		}
		return MallResult.ok();
	}*/
	
	/**
	 * 批量删除商品 ,实际做法是修改商品状态
	 */
	@Override
	public MallResult deleteItems(long[] itemId,TbItem item) {
		for (long l : itemId) {
			item = itemMapper.selectByPrimaryKey(l);
			item.setStatus((byte)3);
			item.setUpdated(new Date());
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return MallResult.ok();
	}

	/**
	 * 商品批量下架
	 */
	@Override
	public MallResult instockItem(long[] itemId,TbItem item) {
		for (long l : itemId) {
			item = itemMapper.selectByPrimaryKey(l);
			item.setStatus((byte)2);
			item.setUpdated(new Date());
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return MallResult.ok();
	}

	/**
	 * 商品批量上架
	 */
	@Override
	public MallResult reshelfItem(long[] itemId,TbItem item) {
		for (long l : itemId) {
			item = itemMapper.selectByPrimaryKey(l);
			item.setStatus((byte)1);
			item.setUpdated(new Date());
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return MallResult.ok();
	}

	@Override
	public MallResult updateItem(TbItem item,String desc) {
		// 1.根据商品id更新商品表
		Long id = item.getId();
		// 创建查询条件，根据id更新商品表
		TbItemExample tbItemExample = new TbItemExample();
		Criteria criteria = tbItemExample.createCriteria();
		criteria.andIdEqualTo(id);
		item.setUpdated(new Date());
		itemMapper.updateByExampleSelective(item,tbItemExample);
		
		// 2.根据商品id更新商品描述表
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemDesc(desc);
		TbItemDescExample tbItemDescExample = new TbItemDescExample();
		com.shm.mall.pojo.TbItemDescExample.Criteria createCriteria = tbItemDescExample.createCriteria();
		createCriteria.andItemIdEqualTo(id);
		itemDesc.setUpdated(new Date());
		itemDescMapper.updateByExampleSelective(itemDesc,tbItemDescExample);

		return MallResult.ok();
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//添加查询缓存
		try {
			String string = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":DESC");
			if(StringUtils.isNotBlank(string)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(string, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存中没有。则查询数据
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		//有结果则添加到缓存
		try {
			jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":DESC", JsonUtils.objectToJson(itemDesc));
			//设置过期时间
			jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":DESC", ITEM_CACHE_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

}
