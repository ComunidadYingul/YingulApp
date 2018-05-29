package com.valecom.yingul.model;

import java.io.Serializable;

/**
 * Created by gonzalo on 14-05-18.
 */

public class Yng_Buy implements Serializable {

    private Long buyId;
    private double cost;
    private double shippingCost;
    private double itemCost;
    private String money;
    private int quantity;
    //datos de la compra obtenidos con la ip de comprador
    private String ip;
    private String org;
    private String lat;
    private String lon;
    private String city;
    private String country;
    private String countryCode;
    private String regionName;
    private String zip;
    private String time;
    private String userAgent;
    private String cookie;
    private String deviceSessionId;

    private Yng_User user;

    private Yng_User seller;

    private Yng_Item yng_item;

    private Yng_Payment yng_Payment;

    private Yng_Shipping shipping;

    public Long getBuyId() {
        return buyId;
    }

    public void setBuyId(Long buyId) {
        this.buyId = buyId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public double getItemCost() {
        return itemCost;
    }

    public void setItemCost(double itemCost) {
        this.itemCost = itemCost;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getDeviceSessionId() {
        return deviceSessionId;
    }

    public void setDeviceSessionId(String deviceSessionId) {
        this.deviceSessionId = deviceSessionId;
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

    public Yng_Item getYng_item() {
        return yng_item;
    }

    public void setYng_item(Yng_Item yng_item) {
        this.yng_item = yng_item;
    }

    public Yng_Payment getYng_Payment() {
        return yng_Payment;
    }

    public void setYng_Payment(Yng_Payment yng_Payment) {
        this.yng_Payment = yng_Payment;
    }

    public Yng_Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Yng_Shipping shipping) {
        this.shipping = shipping;
    }
}
