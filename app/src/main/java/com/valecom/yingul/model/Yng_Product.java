package com.valecom.yingul.model;

import java.io.Serializable;

/**
 * Created by gonzalo on 16-05-18.
 */

public class Yng_Product implements Serializable {

    private Long productId;
    private String productCondition;
    private String productSaleConditions;
    private String productFormDelivery;
    private String productPaymentMethod;
    private String productWarranty="";
    private String productPagoEnvio="";
    private String productPeso="";
    private String producVolumen="";
    private int productLength=0;
    private int productWidth=0;
    private int productHeight=0;
    private int productWeight=0;
    private Yng_Item yng_Item;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(String productCondition) {
        this.productCondition = productCondition;
    }

    public String getProductSaleConditions() {
        return productSaleConditions;
    }

    public void setProductSaleConditions(String productSaleConditions) {
        this.productSaleConditions = productSaleConditions;
    }

    public String getProductFormDelivery() {
        return productFormDelivery;
    }

    public void setProductFormDelivery(String productFormDelivery) {
        this.productFormDelivery = productFormDelivery;
    }

    public String getProductPaymentMethod() {
        return productPaymentMethod;
    }

    public void setProductPaymentMethod(String productPaymentMethod) {
        this.productPaymentMethod = productPaymentMethod;
    }

    public String getProductWarranty() {
        return productWarranty;
    }

    public void setProductWarranty(String productWarranty) {
        this.productWarranty = productWarranty;
    }

    public String getProductPagoEnvio() {
        return productPagoEnvio;
    }

    public void setProductPagoEnvio(String productPagoEnvio) {
        this.productPagoEnvio = productPagoEnvio;
    }

    public String getProductPeso() {
        return productPeso;
    }

    public void setProductPeso(String productPeso) {
        this.productPeso = productPeso;
    }

    public String getProducVolumen() {
        return producVolumen;
    }

    public void setProducVolumen(String producVolumen) {
        this.producVolumen = producVolumen;
    }

    public int getProductLength() {
        return productLength;
    }

    public void setProductLength(int productLength) {
        this.productLength = productLength;
    }

    public int getProductWidth() {
        return productWidth;
    }

    public void setProductWidth(int productWidth) {
        this.productWidth = productWidth;
    }

    public int getProductHeight() {
        return productHeight;
    }

    public void setProductHeight(int productHeight) {
        this.productHeight = productHeight;
    }

    public int getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(int productWeight) {
        this.productWeight = productWeight;
    }

    public Yng_Item getYng_Item() {
        return yng_Item;
    }

    public void setYng_Item(Yng_Item yng_Item) {
        this.yng_Item = yng_Item;
    }
}
