package com.shm.mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shm.mall.content.service.ContentCategoryService;
import com.shm.mall.mapper.TbContentCategoryMapper;
import com.shm.mall.pojo.EasyUITreeNode;
import com.shm.mall.pojo.TbContentCategory;
import com.shm.mall.pojo.TbContentCategoryExample;
import com.shm.mall.pojo.TbContentCategoryExample.Criteria;
import com.shm.mall.utils.MallResult;

/**
 * 商品分类管理service
 * @author SHM
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCatList(long parentId) {
		// 根据parentId查询子节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> catList = contentCategoryMapper.selectByExample(example);
		
		//转换成EasyUITreeNode列表
		List<EasyUITreeNode> nodeList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : catList) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			nodeList.add(node);
		}
		return nodeList;
	}
	
	@Override
	public MallResult addContentCategory(long parentId, String name) {
		// 创建一个TbContentCategory的pojo对象
		TbContentCategory contentCategory = new TbContentCategory();
		//设置pojo属性
		//'该类目是否为父类目，1为true，0为false',新添加的节点一定是叶子节点
		contentCategory.setIsParent(false);
		contentCategory.setName(name);
		contentCategory.setParentId(parentId);
		//默认排序就是1
		contentCategory.setSortOrder(1);
		// '状态。可选值:1(正常),2(删除)'
		contentCategory.setStatus(1);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//插入到数据库
		contentCategoryMapper.insert(contentCategory);
		//判断父节点的isparent属性，如果不是true，改为true
		//根据parentId查询父节点
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()) {
			parent.setIsParent(true);
			//更新到数据库
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		//返回结果，返回mallresult，包含pojo
		
		return MallResult.ok(contentCategory);
	}
	
	
	/**
	 * 重命名节点
	 */
	@Override
	public MallResult updateContentCategory(long id, String name) {
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		contentCategory.setName(name);
		contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
		return MallResult.ok();
	}

	@Override
	public MallResult deleteContentCategory(long id) {
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		if(!contentCategory.getIsParent()) {
			contentCategoryMapper.deleteByPrimaryKey(id);
		}
		Long parentId = contentCategory.getParentId();
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		int count = contentCategoryMapper.countByExample(example);
		if(count == 0) {
			TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
			parent.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
				
		return MallResult.ok();
	}

}
