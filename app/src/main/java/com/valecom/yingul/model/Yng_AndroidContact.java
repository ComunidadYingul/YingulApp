package com.valecom.yingul.model;

public class Yng_AndroidContact {

    private int android_contact_ID;
    private String android_contact_Name;
    private String android_contact_TelefonNr;
    private Yng_User user;

    public Yng_AndroidContact() {
        super();
    }

    public int getAndroid_contact_ID() {
        return android_contact_ID;
    }

    public void setAndroid_contact_ID(int android_contact_ID) {
        this.android_contact_ID = android_contact_ID;
    }

    public String getAndroid_contact_Name() {
        return android_contact_Name;
    }

    public void setAndroid_contact_Name(String android_contact_Name) {
        this.android_contact_Name = android_contact_Name;
    }

    public String getAndroid_contact_TelefonNr() {
        return android_contact_TelefonNr;
    }

    public void setAndroid_contact_TelefonNr(String android_contact_TelefonNr) {
        this.android_contact_TelefonNr = android_contact_TelefonNr;
    }

    public Yng_User getUser() {
        return user;
    }

    public void setUser(Yng_User user) {
        this.user = user;
    }
}
