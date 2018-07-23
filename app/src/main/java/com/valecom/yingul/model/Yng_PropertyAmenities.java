package com.valecom.yingul.model;

/**
 * Created by gonzalo on 23-07-18.
 */

public class Yng_PropertyAmenities {
    private long propertyAmenities;

    private Yng_Property property;

    private Yng_Amenities amenities;

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
