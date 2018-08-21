package com.valecom.yingul.model;

import org.json.JSONObject;
import java.io.Serializable;

public class Yng_ShippingTraceability implements Serializable {

	private String FechaAlta="";
	private JSONObject Eventos= new JSONObject();
	private String NombreEnvio="";
	private String NroAndreani="";

	public Yng_ShippingTraceability() {
		super();
	}

	public String getFechaAlta() {
		return FechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		FechaAlta = fechaAlta;
	}

	public JSONObject getEventos() {
		return Eventos;
	}

	public void setEventos(JSONObject eventos) {
		Eventos = eventos;
	}

	public String getNombreEnvio() {
		return NombreEnvio;
	}

	public void setNombreEnvio(String nombreEnvio) {
		NombreEnvio = nombreEnvio;
	}

	public String getNroAndreani() {
		return NroAndreani;
	}

	public void setNroAndreani(String nroAndreani) {
		NroAndreani = nroAndreani;
	}
}
