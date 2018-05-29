package com.valecom.yingul.Item;

/**
 * Created by gonzalo on 21-05-18.
 */

public class ItemCategory {

    private String CategoryId;
    private String CategoryName;
    private String CategoryImage;
    private String CategoryImageBanner;
    private String CategoryNoItem;

    public String getCategoryId() {
        return CategoryId;
    }
    public void setCategoryId(String CategoryId) {
        this.CategoryId = CategoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }
    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }

    public String getCategoryImage() {return CategoryImage;}
    public void setCategoryImage(String CategoryImage) {
        this.CategoryImage = CategoryImage;
    }

    public String getCategoryImageBanner() {return CategoryImageBanner;}
    public void setCategoryImageBanner(String CategoryImageBanner) {this.CategoryImageBanner = CategoryImageBanner;}

    public String getCategoryNoItem() {return CategoryNoItem;}
    public void setCategoryNoItem(String CategoryNoItem) {
        this.CategoryNoItem = CategoryNoItem;
    }

}
