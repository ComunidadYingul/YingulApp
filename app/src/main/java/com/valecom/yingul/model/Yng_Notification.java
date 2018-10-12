package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_Notification implements Serializable {

    private long notificationId;
	private String title;
	private String description;
	private String url;
	private String date;
	private String status;
	private String desktopStatus;

    private Yng_User user;

    private Yng_Item item;
	
	public Yng_Notification() {
		super();
	}

	public long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public Yng_User getUser() {
		return user;
	}

	public void setUser(Yng_User user) {
		this.user = user;
	}

	public Yng_Item getItem() {
		return item;
	}

	public void setItem(Yng_Item item) {
		this.item = item;
	}

	public String getDesktopStatus() {
		return desktopStatus;
	}

	public void setDesktopStatus(String desktopStatus) {
		this.desktopStatus = desktopStatus;
	}
}
