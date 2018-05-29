package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_Ubication implements Serializable {

    private Long ubicationId;
	private String latitud;
	private String longitud;
	private String street;
	private String number;
	private String postalCode;
	private String aditional;
	private String codAndreani="";
	
	private String department="";
	private String withinStreets="";

    private Yng_City yng_City;
    private Yng_Province yng_Province;
    private Yng_Department yng_Department;
	private Yng_Barrio yng_Barrio;
	private Yng_Country yng_Country;

	public Yng_Ubication() {
		super();
	}

	public Long getUbicationId() {
		return ubicationId;
	}

	public void setUbicationId(Long ubicationId) {
		this.ubicationId = ubicationId;
	}

	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getAditional() {
		return aditional;
	}

	public void setAditional(String aditional) {
		this.aditional = aditional;
	}

	public String getCodAndreani() {
		return codAndreani;
	}

	public void setCodAndreani(String codAndreani) {
		this.codAndreani = codAndreani;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getWithinStreets() {
		return withinStreets;
	}

	public void setWithinStreets(String withinStreets) {
		this.withinStreets = withinStreets;
	}

	public Yng_City getYng_City() {
		return yng_City;
	}

	public void setYng_City(Yng_City yng_City) {
		this.yng_City = yng_City;
	}

	public Yng_Province getYng_Province() {
		return yng_Province;
	}

	public void setYng_Province(Yng_Province yng_Province) {
		this.yng_Province = yng_Province;
	}

	public Yng_Department getYng_Department() {
		return yng_Department;
	}

	public void setYng_Department(Yng_Department yng_Department) {
		this.yng_Department = yng_Department;
	}

	public Yng_Barrio getYng_Barrio() {
		return yng_Barrio;
	}

	public void setYng_Barrio(Yng_Barrio yng_Barrio) {
		this.yng_Barrio = yng_Barrio;
	}

	public Yng_Country getYng_Country() {
		return yng_Country;
	}

	public void setYng_Country(Yng_Country yng_Country) {
		this.yng_Country = yng_Country;
	}
}
