package com.shm.mall.item.pojo;

import com.shm.mall.pojo.TbItem;

public class Item extends TbItem {

	public Item(TbItem tbItem) {
		this.setId(tbItem.getId());
		this.setTitle(tbItem.getTitle());
		this.setImage(tbItem.getImage());
		this.setBarcode(tbItem.getBarcode());
		this.setCid(tbItem.getCid());
		this.setCreated(tbItem.getCreated());
		this.setNum(tbItem.getNum());
		this.setPrice(tbItem.getPrice());
		this.setSellPoint(tbItem.getSellPoint());
		this.setStatus(tbItem.getStatus());
		this.setUpdated(tbItem.getUpdated());
	}
	public String[] getImages() {
		String images2 = this.getImage();
		if(images2 != null && !"".equals(images2)) {
			String[] strings = images2.split(",");
			return strings;
		}
		return null;
	}
}
