package com.valecom.yingul.Item;

/**
 * Created by gonzalo on 23-05-18.
 */

public class ItemAllCategory {

    private String AllCategoryId;
    private String AllCategoryTitle;
    private String AllCategoryImage;
    private String AllCategoryLink;
    private String AllCategoryDescription;

    public String getAllCategoryId() {
        return AllCategoryId;
    }
    public void setAllCategoryId(String AllCategoryId) {
        this.AllCategoryId = AllCategoryId;
    }

    public String getAllCategoryTitle() {
        return AllCategoryTitle;
    }

    public void setAllCategoryTitle(String allCategoryTitle) {
        AllCategoryTitle = allCategoryTitle;
    }

    public String getAllCategoryImage() {
        return AllCategoryImage;
    }
    public void setAllCategoryImage(String AllCategoryImage) {
        this.AllCategoryImage = AllCategoryImage;
    }

    public String getAllCategoryLink() {return AllCategoryLink;}
    public void setAllCategoryLink(String AllCategoryLink) {
        this.AllCategoryLink = AllCategoryLink;
    }

    public String getAllCategoryDescription() {return AllCategoryDescription;}
    public void setAllCategoryDescription(String AllCategoryDescription) {
        this.AllCategoryDescription = AllCategoryDescription;
    }
    
}
