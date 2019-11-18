package com.stakater.nordmart.model;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable {


    private static final long serialVersionUID = -7304814269819778382L;

    @Id
    private String id;

    private String productId;
    private String customerName = "";
    private int rating = 3;
    private String reviewText = "";
    private Date dateTime;

    public Review() {

    }

    public Review(String productId, String customerName, String rating, String text) {
        super();
        try {
            int ratingNumber = Integer.parseInt(rating);
            if (ratingNumber < 1) {
                this.rating = 1;
            } else if (ratingNumber > 5) {
                this.rating = 5;
            } else {
                this.rating = ratingNumber;
            }
            if (text != null) {
                this.reviewText = text;
            }
        } catch (NumberFormatException ne) {

        }
        this.productId = productId;
        this.customerName = customerName;
        this.dateTime = new Date();

    }

    public String getId() { return this.id; }
    public String getProductId() { return this.productId; }
    public String getCustomerName() { return this.customerName; }
    public int getRating() { return this.rating; }
    public String getReviewText() { return this.reviewText; }
    public Date getDateTime() { return this.dateTime; }
    public void setDateTime(Date d) { this.dateTime = d; }



    public void setId(String id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    @Override
    public String toString() {
        return String.format(
                "Review[id=%s, productId='%s', customerName='%s', rating='%d', reviewText='%s']",
                this.id, this.productId, this.customerName, this.rating, this.reviewText);
    }
}
