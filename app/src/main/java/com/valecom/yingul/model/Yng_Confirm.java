package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_Confirm implements Serializable {

    private Long confirmId;
	
	private boolean sellerConfirm;
	private int daySellerConfirm;
	private int monthSellerConfirm;
	private int yearSellerConfirm;
	
	private boolean buyerConfirm;
	private int dayBuyerConfirm;
	private int monthBuyerConfirm;
	private int yearBuyerConfirm;
	
	private int dayInitClaim;
	private int monthInitClaim;
	private int yearInitiClaim;
	
	private int dayEndClaim;
	private int monthEndClaim;
	private int yearEndClaim;
	
	private int codeConfirm;
	private String status;

    private Yng_User seller;

    private Yng_User buyer;

    private Yng_Buy buy;
    
    public Yng_Confirm() {
    	super();
    }

	public Long getConfirmId() {
		return confirmId;
	}

	public void setConfirmId(Long confirmId) {
		this.confirmId = confirmId;
	}

	public boolean isSellerConfirm() {
		return sellerConfirm;
	}

	public void setSellerConfirm(boolean sellerConfirm) {
		this.sellerConfirm = sellerConfirm;
	}

	public int getDaySellerConfirm() {
		return daySellerConfirm;
	}

	public void setDaySellerConfirm(int daySellerConfirm) {
		this.daySellerConfirm = daySellerConfirm;
	}

	public int getMonthSellerConfirm() {
		return monthSellerConfirm;
	}

	public void setMonthSellerConfirm(int monthSellerConfirm) {
		this.monthSellerConfirm = monthSellerConfirm;
	}

	public int getYearSellerConfirm() {
		return yearSellerConfirm;
	}

	public void setYearSellerConfirm(int yearSellerConfirm) {
		this.yearSellerConfirm = yearSellerConfirm;
	}

	public boolean isBuyerConfirm() {
		return buyerConfirm;
	}

	public void setBuyerConfirm(boolean buyerConfirm) {
		this.buyerConfirm = buyerConfirm;
	}

	public int getDayBuyerConfirm() {
		return dayBuyerConfirm;
	}

	public void setDayBuyerConfirm(int dayBuyerConfirm) {
		this.dayBuyerConfirm = dayBuyerConfirm;
	}

	public int getMonthBuyerConfirm() {
		return monthBuyerConfirm;
	}

	public void setMonthBuyerConfirm(int monthBuyerConfirm) {
		this.monthBuyerConfirm = monthBuyerConfirm;
	}

	public int getYearBuyerConfirm() {
		return yearBuyerConfirm;
	}

	public void setYearBuyerConfirm(int yearBuyerConfirm) {
		this.yearBuyerConfirm = yearBuyerConfirm;
	}

	public int getDayInitClaim() {
		return dayInitClaim;
	}

	public void setDayInitClaim(int dayInitClaim) {
		this.dayInitClaim = dayInitClaim;
	}

	public int getMonthInitClaim() {
		return monthInitClaim;
	}

	public void setMonthInitClaim(int monthInitClaim) {
		this.monthInitClaim = monthInitClaim;
	}

	public int getYearInitiClaim() {
		return yearInitiClaim;
	}

	public void setYearInitiClaim(int yearInitiClaim) {
		this.yearInitiClaim = yearInitiClaim;
	}

	public int getDayEndClaim() {
		return dayEndClaim;
	}

	public void setDayEndClaim(int dayEndClaim) {
		this.dayEndClaim = dayEndClaim;
	}

	public int getMonthEndClaim() {
		return monthEndClaim;
	}

	public void setMonthEndClaim(int monthEndClaim) {
		this.monthEndClaim = monthEndClaim;
	}

	public int getYearEndClaim() {
		return yearEndClaim;
	}

	public void setYearEndClaim(int yearEndClaim) {
		this.yearEndClaim = yearEndClaim;
	}

	public int getCodeConfirm() {
		return codeConfirm;
	}

	public void setCodeConfirm(int codeConfirm) {
		this.codeConfirm = codeConfirm;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Yng_User getSeller() {
		return seller;
	}

	public void setSeller(Yng_User seller) {
		this.seller = seller;
	}

	public Yng_User getBuyer() {
		return buyer;
	}

	public void setBuyer(Yng_User buyer) {
		this.buyer = buyer;
	}

	public Yng_Buy getBuy() {
		return buy;
	}

	public void setBuy(Yng_Buy buy) {
		this.buy = buy;
	}
}
