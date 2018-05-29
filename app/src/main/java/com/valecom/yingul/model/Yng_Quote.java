package com.valecom.yingul.model;

import java.io.Serializable;

/**
 * Created by gonzalo on 14-05-18.
 */

public class Yng_Quote implements Serializable {

    private long quoteId;
    private double rate;
    private double rateOrigin;
    private int quantity;

    private java.lang.String respuesta="";

    private Yng_Item yng_Item;

    private Yng_User yng_User;

    private Yng_Branch yng_Branch;

    public long getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(long quoteId) {
        this.quoteId = quoteId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getRateOrigin() {
        return rateOrigin;
    }

    public void setRateOrigin(double rateOrigin) {
        this.rateOrigin = rateOrigin;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Yng_Item getYng_Item() {
        return yng_Item;
    }

    public void setYng_Item(Yng_Item yng_Item) {
        this.yng_Item = yng_Item;
    }

    public Yng_User getYng_User() {
        return yng_User;
    }

    public void setYng_User(Yng_User yng_User) {
        this.yng_User = yng_User;
    }

    public Yng_Branch getYng_Branch() {
        return yng_Branch;
    }

    public void setYng_Branch(Yng_Branch yng_Branch) {
        this.yng_Branch = yng_Branch;
    }
}
