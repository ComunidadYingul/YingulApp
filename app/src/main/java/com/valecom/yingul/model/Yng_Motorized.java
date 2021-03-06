package com.valecom.yingul.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gonzalo on 17-05-18.
 */

public class Yng_Motorized implements Serializable{

    private Long motorizedId;
    private String motorizedBrand;
    private String motorizedModel;
    private String motorizedUnicoDue;
    private String condition;
    private Yng_Item yng_Item;
    private Set<Yng_MotorizedConfort> motorizedConfort =new HashSet<>();

    public Long getMotorizedId() {
        return motorizedId;
    }

    public void setMotorizedId(Long motorizedId) {
        this.motorizedId = motorizedId;
    }

    public String getMotorizedBrand() {
        return motorizedBrand;
    }

    public void setMotorizedBrand(String motorizedBrand) {
        this.motorizedBrand = motorizedBrand;
    }

    public String getMotorizedModel() {
        return motorizedModel;
    }

    public void setMotorizedModel(String motorizedModel) {
        this.motorizedModel = motorizedModel;
    }

    public String getMotorizedUnicoDue() {
        return motorizedUnicoDue;
    }

    public void setMotorizedUnicoDue(String motorizedUnicoDue) {
        this.motorizedUnicoDue = motorizedUnicoDue;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Yng_Item getYng_Item() {
        return yng_Item;
    }

    public void setYng_Item(Yng_Item yng_Item) {
        this.yng_Item = yng_Item;
    }

    public Set<Yng_MotorizedConfort> getMotorizedConfort() {
        return motorizedConfort;
    }

    public void setMotorizedConfort(Set<Yng_MotorizedConfort> motorizedConfort) {
        this.motorizedConfort = motorizedConfort;
    }
}
