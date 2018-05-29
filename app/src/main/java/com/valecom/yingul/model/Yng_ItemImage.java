package com.valecom.yingul.model;

public class Yng_ItemImage {
    private Long itemImageId;
	private java.lang.String image;
    private Yng_Item item;
	
	public Yng_ItemImage() {
		super();
	}

	public Long getItemImageId() {
		return itemImageId;
	}

	public void setItemImageId(Long itemImageId) {
		this.itemImageId = itemImageId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Yng_Item getItem() {
		return item;
	}

	public void setItem(Yng_Item item) {
		this.item = item;
	}
}
