package com.stakater.nordmart.service;

import com.stakater.nordmart.model.Review;

import java.util.List;

public interface ReviewService {

    public List<Review> getReviews(String productId) throws Exception;

    public Review addReview(String productId, String customerName, int rating, String text) throws Exception;
    public void deleteReview(String reviewId) throws Exception;

}
