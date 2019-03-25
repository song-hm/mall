package com.shm.mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shm.mall.mapper.TbItemCatMapper;
import com.shm.mall.pojo.EasyUITreeNode;
import com.shm.mall.pojo.TbItemCat;
import com.shm.mall.pojo.TbItemCatExample;
import com.shm.mall.pojo.TbItemCatExample.Criteria;
import com.shm.mall.service.ItemCatService;
/**
 * 商品分类管理的service
 * @author SHM
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		//根据parentId查询子节点列表
		TbItemCatExample example = new TbItemCatExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
		
		//创建返回结果list
		List<EasyUITreeNode> resultList = new ArrayList<>();
		//把列表转换成EasyUITreeNode列表
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			//设置属性
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			//添加到结果列表
			resultList.add(node);
		}
		
		//返回结果
		return resultList;
	}

}
