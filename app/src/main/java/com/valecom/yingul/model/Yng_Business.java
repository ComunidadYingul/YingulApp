package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_Business implements Serializable {

    private Long businessId;
	private String businessName;
	private String documentType;
	private String documentNumber;
	private String contributorType;
	private Yng_User user;
	
	public Yng_Business() {
		super();
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public Yng_User getUser() {
		return user;
	}

	public void setUser(Yng_User user) {
		this.user = user;
	}

	public String getContributorType() { return contributorType;}

	public void setContributorType(String contributorType) {this.contributorType = contributorType;	}
}
