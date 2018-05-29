package com.valecom.yingul.Item;

/**
 * Created by gonzalo on 22-05-18.
 */

public class ItemReview {

    private String ReviewId;
    private String ReviewUserName;
    private String ReviewTime;
    private String ReviewMessage;

    public String getReviewId() {
        return ReviewId;
    }
    public void setReviewId(String ReviewId) {
        this.ReviewId = ReviewId;
    }

    public String getReviewUserName() {
        return ReviewUserName;
    }
    public void setReviewUserName(String ReviewUserName) {
        this.ReviewUserName = ReviewUserName;
    }

    public String getReviewTime() {return ReviewTime;}
    public void setReviewTime(String ReviewTime) {
        this.ReviewTime = ReviewTime;
    }

    public String getReviewMessage() {return ReviewMessage;}
    public void setReviewMessage(String ReviewMessage) {this.ReviewMessage = ReviewMessage;}

}
