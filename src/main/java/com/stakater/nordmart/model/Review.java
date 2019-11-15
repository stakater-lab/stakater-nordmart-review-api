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

    public Review(String productId, String customerName, int rating, String text) {
        super();
        if(rating < 1) {
            this.rating = 1;
        } else if (rating > 5) {
            this.rating = 5;
        } else {
            this.rating = rating;
        }
        if(text != null) {
            this.reviewText = text;
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

    @Override
    public String toString() {
        return String.format(
                "Review[id=%s, productId='%s', customerName='%s', rating='%d', reviewText='%s']",
                this.id, this.productId, this.customerName, this.rating, this.reviewText);
    }
}
