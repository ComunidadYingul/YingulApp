package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_PropertyAmenities implements Serializable {
    private long propertyAmenities;
    private Yng_Property property;
    private Yng_Amenities amenities;
    public Yng_PropertyAmenities(){
        super();
    }

    public long getPropertyAmenities() {
        return propertyAmenities;
    }

    public void setPropertyAmenities(long propertyAmenities) {
        this.propertyAmenities = propertyAmenities;
    }

    public Yng_Property getProperty() {
        return property;
    }

    public void setProperty(Yng_Property property) {
        this.property = property;
    }

    public Yng_Amenities getAmenities() {
        return amenities;
    }

    public void setAmenities(Yng_Amenities amenities) {
        this.amenities = amenities;
    }
}