package com.valecom.yingul.Item;

/**
 * Created by gonzalo on 14-05-18.
 */

public class ItemCategoryList {
    private String CategoryListId;
    private String CategoryListName;
    private String CategoryListImage;
    private String CategoryListDescription;
    private String CategoryListPrice;

    private String CategorySeller;

    public String getCategoryListId() {
        return CategoryListId;
    }
    public void setCategoryListId(String CategoryListId) {
        this.CategoryListId = CategoryListId;}

    public String getCategoryListName() {
        return CategoryListName;
    }
    public void setCategoryListName(String CategoryListName) {
        this.CategoryListName = CategoryListName;}

    public String getCategoryListImage() {return CategoryListImage;}
    public void setCategoryListImage(String CategoryListImage) {
        this.CategoryListImage = CategoryListImage;}

    public String getCategoryListDescription() {return CategoryListDescription;}
    public void setCategoryListDescription(String CategoryListDescription) {
        this.CategoryListDescription = CategoryListDescription;}

    public String getCategoryListPrice() {return CategoryListPrice;}
    public void setCategoryListPrice(String CategoryListPrice) {
        this.CategoryListPrice = CategoryListPrice;}

    public String getCategorySeller() {
        return CategorySeller;
    }

    public void setCategorySeller(String categorySeller) {
        CategorySeller = categorySeller;
    }
}
