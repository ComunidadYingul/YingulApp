package com.valecom.yingul.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gonzalo on 18-05-18.
 */

public class Yng_Property {

    private Long propertyId;
    private String propertyTotalArea;
    private String propertyYear;
    private String condition;
    private Yng_Item yng_Item;
    private Set<Yng_PropertyAmenities> propertyAmenities= new HashSet<>();

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyTotalArea() {
        return propertyTotalArea;
    }

    public void setPropertyTotalArea(String propertyTotalArea) {
        this.propertyTotalArea = propertyTotalArea;
    }

    public String getPropertyYear() {
        return propertyYear;
    }

    public void setPropertyYear(String propertyYear) {
        this.propertyYear = propertyYear;
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

    public Set<Yng_PropertyAmenities> getPropertyAmenities() {
        return propertyAmenities;
    }

    public void setPropertyAmenities(Set<Yng_PropertyAmenities> propertyAmenities) {
        this.propertyAmenities = propertyAmenities;
    }
}
