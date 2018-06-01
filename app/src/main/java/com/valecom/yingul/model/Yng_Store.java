package com.valecom.yingul.model;

/**
 * Created by gonzalo on 30-05-18.
 */

public class Yng_Store {
    private Long storeId;
    private String employeesQuantity;
    private String ecommerceExperience;
    private String webSite;
    private String traficInvest;
    private String itemsType;
    private String name;
    private String summary;
    private String video;

    private java.lang.String mainImage;
    private java.lang.String bannerImage;

    private Yng_User user;

    private Yng_Category mainCategory;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getEmployeesQuantity() {
        return employeesQuantity;
    }

    public void setEmployeesQuantity(String employeesQuantity) {
        this.employeesQuantity = employeesQuantity;
    }

    public String getEcommerceExperience() {
        return ecommerceExperience;
    }

    public void setEcommerceExperience(String ecommerceExperience) {
        this.ecommerceExperience = ecommerceExperience;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getTraficInvest() {
        return traficInvest;
    }

    public void setTraficInvest(String traficInvest) {
        this.traficInvest = traficInvest;
    }

    public String getItemsType() {
        return itemsType;
    }

    public void setItemsType(String itemsType) {
        this.itemsType = itemsType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public Yng_User getUser() {
        return user;
    }

    public void setUser(Yng_User user) {
        this.user = user;
    }

    public Yng_Category getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(Yng_Category mainCategory) {
        this.mainCategory = mainCategory;
    }
}
