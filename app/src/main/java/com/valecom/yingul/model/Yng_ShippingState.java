package com.valecom.yingul.model;

import org.json.JSONObject;

import java.io.Serializable;

public class Yng_ShippingState implements Serializable {

	private String Sucursal="";
	private String Fecha="";
	private String IdMotivo="";
	private JSONObject Motivo= new JSONObject();
	private String IdEstado="";
	private String Estado="";

	public Yng_ShippingState() {
		super();
	}

	public String getSucursal() {
		return Sucursal;
	}

	public void setSucursal(String sucursal) {
		Sucursal = sucursal;
	}

	public String getFecha() {
		return Fecha;
	}

	public void setFecha(String fecha) {
		Fecha = fecha;
	}

	public String getIdMotivo() {
		return IdMotivo;
	}

	public void setIdMotivo(String idMotivo) {
		IdMotivo = idMotivo;
	}

	public JSONObject getMotivo() {
		return Motivo;
	}

	public void setMotivo(JSONObject motivo) {
		Motivo = motivo;
	}

	public String getIdEstado() {
		return IdEstado;
	}

	public void setIdEstado(String idEstado) {
		IdEstado = idEstado;
	}

	public String getEstado() {
		return Estado;
	}

	public void setEstado(String estado) {
		Estado = estado;
	}
}
