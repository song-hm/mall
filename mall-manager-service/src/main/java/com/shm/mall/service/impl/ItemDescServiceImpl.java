package com.shm.mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shm.mall.mapper.TbItemDescMapper;
import com.shm.mall.pojo.TbItemDesc;
import com.shm.mall.service.ItemDescService;
import com.shm.mall.utils.MallResult;

@Service
public class ItemDescServiceImpl implements ItemDescService {

	@Autowired
	private TbItemDescMapper itemDescMapper;

	@Override
	public MallResult deleteItemDesc(long[] itemId) {
		for (long l : itemId) {
			itemDescMapper.deleteByPrimaryKey(l);
		}
		return MallResult.ok();
	}

	@Override
	public MallResult getItemDescById(long itemId) {
		TbItemDesc idesc = itemDescMapper.selectByPrimaryKey(itemId);
		return MallResult.ok(idesc);
	}

	/*@Override
	public MallResult deleteItemDescById(long itemId) {
			TbItemDescExample example = new TbItemDescExample();
			Criteria criteria = example.createCriteria();
			//设置查询条件
			criteria.andItemIdEqualTo(itemId);
			itemDescMapper.deleteByExample(example);
			return MallResult.ok();
	}*/

}
