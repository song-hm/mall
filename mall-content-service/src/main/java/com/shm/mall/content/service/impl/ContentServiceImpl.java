package com.shm.mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shm.mall.content.service.ContentService;
import com.shm.mall.jedis.JedisClient;
import com.shm.mall.mapper.TbContentMapper;
import com.shm.mall.pojo.EasyUIDataGridResult;
import com.shm.mall.pojo.TbContent;
import com.shm.mall.pojo.TbContentExample;
import com.shm.mall.pojo.TbContentExample.Criteria;
import com.shm.mall.utils.JsonUtils;
import com.shm.mall.utils.MallResult;


@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	@Override
	public EasyUIDataGridResult getcontentList(long categoryId,int page,int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//取分页结果
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		//取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}
	@Override
	public MallResult deleteContent(Long[] ids) {
		for (Long long1 : ids) {
			TbContent content = contentMapper.selectByPrimaryKey(long1);
			contentMapper.deleteByPrimaryKey(long1);
			//缓存同步，即删除缓存中对应的数据。			
			try {
				jedisClient.hdel(CONTENT_LIST,content.getCategoryId().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return MallResult.ok();
	}
	@Override
	public MallResult updateContent(TbContent content) {
		Long id = content.getId();
		content.setUpdated(new Date());
		TbContentExample example = new TbContentExample();		
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		contentMapper.updateByExampleSelective(content, example);
		//缓存同步，即删除缓存中对应的数据。
		try {
			jedisClient.hdel(CONTENT_LIST,content.getCategoryId().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MallResult.ok();
	}
	@Override
	public MallResult addContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//插入到数据库
		contentMapper.insert(content);
		//缓存同步，即删除缓存中对应的数据。
		try {
			jedisClient.hdel(CONTENT_LIST,content.getCategoryId().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MallResult.ok();
	}
	
	
	@Override
	public List<TbContent> getContentListByCid(long cid) {
		//查询缓存
		try {
			//缓存中有直接返回结果
			String json = jedisClient.hget(CONTENT_LIST,cid+"");
			if(StringUtils.isNotBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json,TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//缓存中没有则查询数据库
		// 创建离线对象
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);		
		//把结果添加到缓存
		try {
			jedisClient.hset(CONTENT_LIST, cid+"",JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
