package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_Query implements Serializable {
    private long queryId;
	private String query;
	private String answer;
	private String date;
	private String status;

    private Yng_Item yng_Item;
    private Yng_User user;
    private Yng_User seller;
	
	public Yng_Query() {
		super();
	}

	public long getQueryId() {
		return queryId;
	}

	public void setQueryId(long queryId) {
		this.queryId = queryId;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Yng_Item getYng_Item() {
		return yng_Item;
	}

	public void setYng_Item(Yng_Item yng_Item) {
		this.yng_Item = yng_Item;
	}

	public Yng_User getUser() {
		return user;
	}

	public void setUser(Yng_User user) {
		this.user = user;
	}

	public Yng_User getSeller() {
		return seller;
	}

	public void setSeller(Yng_User seller) {
		this.seller = seller;
	}
}
