package com.valecom.yingul.model;

import java.io.Serializable;

/**
 * Created by gonzalo on 14-05-18.
 */

public class Yng_Payment implements Serializable {

    private Long paymentId;
    private String name;
    private String type;
    private String paymentPlan;
    private String status;
    private Long orderId;
    private String referenceCode;
    private String transactionId;
    private double value;
    private String currency;
    private String buyStatus;

    private Yng_Card yng_Card;
    private Yng_CashPayment cashPayment;
    //private Yng_Request yng_Request;


    public Yng_Payment() {
        super();
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPaymentPlan() {
        return paymentPlan;
    }

    public void setPaymentPlan(String paymentPlan) {
        this.paymentPlan = paymentPlan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBuyStatus() {
        return buyStatus;
    }

    public void setBuyStatus(String buyStatus) {
        this.buyStatus = buyStatus;
    }

    public Yng_Card getYng_Card() {
        return yng_Card;
    }

    public void setYng_Card(Yng_Card yng_Card) {
        this.yng_Card = yng_Card;
    }

    public Yng_CashPayment getCashPayment() {
        return cashPayment;
    }

    public void setCashPayment(Yng_CashPayment cashPayment) {
        this.cashPayment = cashPayment;
    }
}
