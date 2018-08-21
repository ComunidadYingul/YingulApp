package com.valecom.yingul.model;

import org.json.JSONObject;
import java.io.Serializable;

public class Yng_ShippingTraceability implements Serializable {

	private String fechaAlta="";
	private JSONObject eventos= new JSONObject();
	private String nombreEnvio="";
	private String nroAndreani="";

	public Yng_ShippingTraceability() {
		super();
	}

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public JSONObject getEventos() {
        return eventos;
    }

    public void setEventos(JSONObject eventos) {
        this.eventos = eventos;
    }

    public String getNombreEnvio() {
        return nombreEnvio;
    }

    public void setNombreEnvio(String nombreEnvio) {
        this.nombreEnvio = nombreEnvio;
    }

    public String getNroAndreani() {
        return nroAndreani;
    }

    public void setNroAndreani(String nroAndreani) {
        this.nroAndreani = nroAndreani;
    }
}
