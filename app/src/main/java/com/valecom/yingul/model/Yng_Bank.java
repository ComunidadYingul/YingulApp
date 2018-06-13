package com.valecom.yingul.model;

/**
 * Created by gonzalo on 12-06-18.
 */

public class Yng_Bank {
    private Long bankId;
    private String name;

    public Yng_Bank() {
        super();
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
