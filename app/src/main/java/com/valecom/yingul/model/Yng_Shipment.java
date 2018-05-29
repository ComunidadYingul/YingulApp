package com.valecom.yingul.model;

/**
 * Created by gonzalo on 14-05-18.
 */

public class Yng_Shipment {

    private Long shipmentId;
    private java.lang.String respuesta="";
    private String typeMail="";
    private Yng_Item yng_Item;
        private Yng_User yng_User;
    private java.lang.String ticket="";
    //numero para realizar el seguimiento si lo tiene
    private String shipmentCod="";

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getTypeMail() {
        return typeMail;
    }

    public void setTypeMail(String typeMail) {
        this.typeMail = typeMail;
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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getShipmentCod() {
        return shipmentCod;
    }

    public void setShipmentCod(String shipmentCod) {
        this.shipmentCod = shipmentCod;
    }
}
