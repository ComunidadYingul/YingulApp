package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_Service implements Serializable {
    private Long serviceId;
	private String emailService;

    private Yng_Item yng_Item;

    //private Set<Yng_ServiceProvince> cobertureZone = new HashSet<>();

    public Yng_Service() {
    	super();
    }

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public String getEmailService() {
		return emailService;
	}

	public void setEmailService(String emailService) {
		this.emailService = emailService;
	}

	public Yng_Item getYng_Item() {
		return yng_Item;
	}

	public void setYng_Item(Yng_Item yng_Item) {
		this.yng_Item = yng_Item;
	}
}
