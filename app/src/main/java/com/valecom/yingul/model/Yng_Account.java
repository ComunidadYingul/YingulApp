package com.valecom.yingul.model;

/**
 * Created by gonzalo on 12-06-18.
 */

public class Yng_Account {
    private Long accountId;

    private double withheldMoney;
    private double availableMoney;
    private double releasedMoney;

    private String currency;
    private boolean accountNonExpired;
    private boolean accountNonLocked;

    private Yng_User user;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public double getWithheldMoney() {
        return withheldMoney;
    }

    public void setWithheldMoney(double withheldMoney) {
        this.withheldMoney = withheldMoney;
    }

    public double getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(double availableMoney) {
        this.availableMoney = availableMoney;
    }

    public double getReleasedMoney() {
        return releasedMoney;
    }

    public void setReleasedMoney(double releasedMoney) {
        this.releasedMoney = releasedMoney;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Yng_User getUser() {
        return user;
    }

    public void setUser(Yng_User user) {
        this.user = user;
    }
}
