package com.valecom.yingul.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gonzalo on 23-07-18.
 */

public class Yng_Amenities implements Serializable {
    private int amenitiesId;
    private String name;

    private Set<Yng_PropertyAmenities> propertyAmenities = new HashSet<>();

    public int getAmenitiesId() {
        return amenitiesId;
    }

    public void setAmenitiesId(int amenitiesId) {
        this.amenitiesId = amenitiesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Yng_PropertyAmenities> getPropertyAmenities() {
        return propertyAmenities;
    }

    public void setPropertyAmenities(Set<Yng_PropertyAmenities> propertyAmenities) {
        this.propertyAmenities = propertyAmenities;
    }
}
