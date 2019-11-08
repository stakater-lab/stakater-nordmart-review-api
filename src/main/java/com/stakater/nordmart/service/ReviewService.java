package com.stakater.nordmart.service;

import com.stakater.nordmart.model.Review;

import java.util.List;

public interface ReviewService {

    public List<Review> getReviews(String productId);

    public Review addReview(String productId, String costumerName, int rating, String text);
    public void deleteReview(String reviewId);

}
