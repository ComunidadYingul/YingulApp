package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_StateShipping implements Serializable {
	private String NombreEnvio="";
	private String NroAndreani="";
	private String FechaAlta="";
	private String Fecha="";
	private String Estado="";
	private String Motivo="";
	private String Sucursal="";
	public Yng_StateShipping() {
		super();
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

	public String getFechaAlta() {
		return FechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		FechaAlta = fechaAlta;
	}

	public String getFecha() {
		return Fecha;
	}

	public void setFecha(String fecha) {
		Fecha = fecha;
	}

	public String getEstado() {
		return Estado;
	}

	public void setEstado(String estado) {
		Estado = estado;
	}

	public String getMotivo() {
		return Motivo;
	}

	public void setMotivo(String motivo) {
		Motivo = motivo;
	}

	public String getSucursal() {
		return Sucursal;
	}

	public void setSucursal(String sucursal) {
		Sucursal = sucursal;
	}
}
