package com.stakater.nordmart.service;

import com.stakater.nordmart.model.Review;

import java.util.List;

public interface ReviewService {

    List<Review> getReviews(final String productId);
    Review addReview(final String productId, final String customerName, final String rating, final String text);
    String deleteReview(final String reviewId);

}
