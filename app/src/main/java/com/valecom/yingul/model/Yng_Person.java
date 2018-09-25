package com.valecom.yingul.model;

/**
 * Created by gonzalo on 25-05-18.
 */

public class Yng_Person {

    private Long personId;
    private String name;
    private String lastname;
    private boolean business;
    private Yng_User yng_User;

    public Yng_Person (){

    }

    public Long getPersonId() {
        return personId;
    }
    public void setPersonId(Long personId) {
        this.personId = personId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean isBusiness() {
        return business;
    }

    public void setBusiness(boolean business) {
        this.business = business;
    }

    public Yng_User getYng_User() {
        return yng_User;
    }
    public void setYng_User(Yng_User yng_User) {
        this.yng_User = yng_User;
    }


}
