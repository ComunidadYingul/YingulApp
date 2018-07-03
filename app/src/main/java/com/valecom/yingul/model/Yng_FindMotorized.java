package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_FindMotorized implements Serializable {
	private int findMotorizedId;
	private Long minPrice;
	private Long maxPrice;
	private Long rankPrice;
	private Long minYear;
	private Long categoryId;
	public Yng_FindMotorized() {
		super();
	}

	public int getFindMotorizedId() {
		return findMotorizedId;
	}

	public void setFindMotorizedId(int findMotorizedId) {
		this.findMotorizedId = findMotorizedId;
	}

	public Long getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Long minPrice) {
		this.minPrice = minPrice;
	}

	public Long getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Long maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Long getRankPrice() {
		return rankPrice;
	}

	public void setRankPrice(Long rankPrice) {
		this.rankPrice = rankPrice;
	}

	public Long getMinYear() {
		return minYear;
	}

	public void setMinYear(Long minYear) {
		this.minYear = minYear;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
