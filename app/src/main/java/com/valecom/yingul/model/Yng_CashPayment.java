package com.valecom.yingul.model;

import java.io.Serializable;
import java.util.Date;

public class Yng_CashPayment implements Serializable {
    private Long cashPaymentId;
	private String documentType;
	private String documentNumber;
	private String paymentMethod;
	private String URL_PAYMENT_RECEIPT_PDF;
	private String URL_PAYMENT_RECEIPT_HTML;
	private String buyJson;
    private Date expiration;
	
	public Yng_CashPayment(){
		super();
	}

	public Long getCashPaymentId() {
		return cashPaymentId;
	}

	public void setCashPaymentId(Long cashPaymentId) {
		this.cashPaymentId = cashPaymentId;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getURL_PAYMENT_RECEIPT_PDF() {
		return URL_PAYMENT_RECEIPT_PDF;
	}

	public void setURL_PAYMENT_RECEIPT_PDF(String URL_PAYMENT_RECEIPT_PDF) {
		this.URL_PAYMENT_RECEIPT_PDF = URL_PAYMENT_RECEIPT_PDF;
	}

	public String getURL_PAYMENT_RECEIPT_HTML() {
		return URL_PAYMENT_RECEIPT_HTML;
	}

	public void setURL_PAYMENT_RECEIPT_HTML(String URL_PAYMENT_RECEIPT_HTML) {
		this.URL_PAYMENT_RECEIPT_HTML = URL_PAYMENT_RECEIPT_HTML;
	}

	public String getBuyJson() {
		return buyJson;
	}

	public void setBuyJson(String buyJson) {
		this.buyJson = buyJson;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
}
