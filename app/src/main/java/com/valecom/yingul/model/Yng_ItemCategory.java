package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_ItemCategory implements Serializable {

    private long itemCategoryId;
    private Yng_Item item;
    private Yng_Category category;
	
	
	public Yng_ItemCategory() {
		super();
	}

	public long getItemCategoryId() {
		return itemCategoryId;
	}

	public void setItemCategoryId(long itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}

	public Yng_Item getItem() {
		return item;
	}

	public void setItem(Yng_Item item) {
		this.item = item;
	}

	public Yng_Category getCategory() {
		return category;
	}

	public void setCategory(Yng_Category category) {
		this.category = category;
	}
}
