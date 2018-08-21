package com.valecom.yingul.model;

import org.json.JSONObject;

import java.io.Serializable;

public class Yng_ShippingState implements Serializable {

	private String sucursal="";
	private String fecha="";
	private String idMotivo="";
	private JSONObject motivo= new JSONObject();
	private String idEstado="";
	private String estado="";

	public Yng_ShippingState() {
		super();
	}

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(String idMotivo) {
        this.idMotivo = idMotivo;
    }

    public JSONObject getMotivo() {
        return motivo;
    }

    public void setMotivo(JSONObject motivo) {
        this.motivo = motivo;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
