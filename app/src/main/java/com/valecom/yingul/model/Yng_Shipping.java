package com.valecom.yingul.model;

import java.io.Serializable;

/**
 * Created by gonzalo on 14-05-18.
 */

public class Yng_Shipping implements Serializable {

    private Long shippingId;
    private String typeShipping="";
    private boolean dhl=false;
    private boolean fedex=false;
    private boolean andreani=false;
    private String shippingStatus="";
    private String nameContact="";
    private String lastName="";
    private String phoneContact="";
    //crear desde dhl hasta shipment de manera manual

    private Yng_Quote yng_Quote=new Yng_Quote();

    private Yng_Shipment yng_Shipment=new Yng_Shipment();

    public Long getShippingId() {
        return shippingId;
    }

    public void setShippingId(Long shippingId) {
        this.shippingId = shippingId;
    }

    public String getTypeShipping() {
        return typeShipping;
    }

    public void setTypeShipping(String typeShipping) {
        this.typeShipping = typeShipping;
    }

    public boolean isDhl() {
        return dhl;
    }

    public void setDhl(boolean dhl) {
        this.dhl = dhl;
    }

    public boolean isFedex() {
        return fedex;
    }

    public void setFedex(boolean fedex) {
        this.fedex = fedex;
    }

    public boolean isAndreani() {
        return andreani;
    }

    public void setAndreani(boolean andreani) {
        this.andreani = andreani;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getNameContact() {
        return nameContact;
    }

    public void setNameContact(String nameContact) {
        this.nameContact = nameContact;
    }

    public String getPhoneContact() {
        return phoneContact;
    }

    public void setPhoneContact(String phoneContact) {
        this.phoneContact = phoneContact;
    }

    public Yng_Quote getYng_Quote() {
        return yng_Quote;
    }

    public void setYng_Quote(Yng_Quote yng_Quote) {
        this.yng_Quote = yng_Quote;
    }

    public Yng_Shipment getYng_Shipment() {
        return yng_Shipment;
    }

    public void setYng_Shipment(Yng_Shipment yng_Shipment) {
        this.yng_Shipment = yng_Shipment;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
