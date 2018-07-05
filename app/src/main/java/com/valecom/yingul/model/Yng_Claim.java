package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_Claim implements Serializable{

    private Long claimId;
	private String claimText;
	private boolean change;
	private int codeChange;
	private boolean back;
	private int codeBack;
	private boolean minuse;
	private int codeMinuse;
	private String status;

    private Yng_Confirm confirm;
    
    public Yng_Claim() {
    	super();
    }

	public Long getClaimId() {
		return claimId;
	}

	public void setClaimId(Long claimId) {
		this.claimId = claimId;
	}

	public String getClaimText() {
		return claimText;
	}

	public void setClaimText(String claimText) {
		this.claimText = claimText;
	}

	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

	public int getCodeChange() {
		return codeChange;
	}

	public void setCodeChange(int codeChange) {
		this.codeChange = codeChange;
	}

	public boolean isBack() {
		return back;
	}

	public void setBack(boolean back) {
		this.back = back;
	}

	public int getCodeBack() {
		return codeBack;
	}

	public void setCodeBack(int codeBack) {
		this.codeBack = codeBack;
	}

	public boolean isMinuse() {
		return minuse;
	}

	public void setMinuse(boolean minuse) {
		this.minuse = minuse;
	}

	public int getCodeMinuse() {
		return codeMinuse;
	}

	public void setCodeMinuse(int codeMinuse) {
		this.codeMinuse = codeMinuse;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Yng_Confirm getConfirm() {
		return confirm;
	}

	public void setConfirm(Yng_Confirm confirm) {
		this.confirm = confirm;
	}
}
