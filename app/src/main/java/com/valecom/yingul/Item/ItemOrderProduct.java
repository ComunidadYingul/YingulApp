package com.valecom.yingul.Item;

/**
 * Created by gonzalo on 22-05-18.
 */

public class ItemOrderProduct {

    private String OrderId;
    private String OrderName;
    private String OrderImage;
    private String OrderSeller;
    private String OrderPrice;
    private String OrderOfferPercentage;
    private String OrderDiscountPrice;
    private String OrderDate;
    private int OrderQuantity;

    public String getOrderName() {
        return OrderName;
    }
    public void setOrderName(String OrderName) {
        this.OrderName = OrderName;
    }

    public String getOrderSeller() {
        return OrderSeller;
    }
    public void setOrderSeller(String OrderSeller) {
        this.OrderSeller = OrderSeller;
    }

    public String getOrderImage() {return OrderImage;}
    public void setOrderImage(String OrderImage) {
        this.OrderImage = OrderImage;
    }

    public String getOrderPrice() {return OrderPrice;}
    public void setOrderPrice(String OrderPrice) {this.OrderPrice = OrderPrice;}

    public String getOrderOfferPercentage() {return OrderOfferPercentage;}
    public void setOrderOfferPercentage(String OrderOfferPercentage) {
        this.OrderOfferPercentage = OrderOfferPercentage;
    }

    public String getOrderDiscountPrice() {return OrderDiscountPrice;}
    public void setOrderDiscountPrice(String OrderDiscountPrice) {
        this.OrderDiscountPrice = OrderDiscountPrice;
    }

    public String getOrderDate() {return OrderDate;}
    public void setOrderDate(String OrderDate) {
        this.OrderDate = OrderDate;
    }

    public int getOrderQuantity() {return OrderQuantity;}
    public void setOrderQuantity(int OrderQuantity) {
        this.OrderQuantity = OrderQuantity;
    }

}
