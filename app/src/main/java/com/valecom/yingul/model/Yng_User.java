package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_User implements Serializable {

    private Long userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String phone2;
    private String webSite;
	private String documentType="";
	private String 	documentNumber="";

	private String profileBanner;
	private String profilePhoto;
	private String profileVideo;
	
    private boolean enabled=true;

	private Yng_Ubication yng_Ubication;

	public Yng_User() {
		super();
	}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
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

    public String getProfileBanner() {
        return profileBanner;
    }

    public void setProfileBanner(String profileBanner) {
        this.profileBanner = profileBanner;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getProfileVideo() {
        return profileVideo;
    }

    public void setProfileVideo(String profileVideo) {
        this.profileVideo = profileVideo;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Yng_Ubication getYng_Ubication() {
        return yng_Ubication;
    }

    public void setYng_Ubication(Yng_Ubication yng_Ubication) {
        this.yng_Ubication = yng_Ubication;
    }
}