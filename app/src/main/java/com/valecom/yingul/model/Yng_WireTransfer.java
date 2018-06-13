package com.valecom.yingul.model;

/**
 * Created by gonzalo on 12-06-18.
 */

public class Yng_WireTransfer {
    private Long wireTransferId;

    private String titularName;
    private String cuitCuil;
    private Long cuitCuilNumber;
    private String accountType;
    private Long cbu;
    private double amount;
    private String currency;
    private String status;

    private Yng_Bank bank;
    private Yng_Transaction transaction;

    public Yng_WireTransfer() {
        super();
    }

    public Long getWireTransferId() {
        return wireTransferId;
    }

    public void setWireTransferId(Long wireTransferId) {
        this.wireTransferId = wireTransferId;
    }

    public String getTitularName() {
        return titularName;
    }

    public void setTitularName(String titularName) {
        this.titularName = titularName;
    }

    public String getCuitCuil() {
        return cuitCuil;
    }

    public void setCuitCuil(String cuitCuil) {
        this.cuitCuil = cuitCuil;
    }

    public Long getCuitCuilNumber() {
        return cuitCuilNumber;
    }

    public void setCuitCuilNumber(Long cuitCuilNumber) {
        this.cuitCuilNumber = cuitCuilNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Long getCbu() {
        return cbu;
    }

    public void setCbu(Long cbu) {
        this.cbu = cbu;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Yng_Bank getBank() {
        return bank;
    }

    public void setBank(Yng_Bank bank) {
        this.bank = bank;
    }

    public Yng_Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Yng_Transaction transaction) {
        this.transaction = transaction;
    }
}
