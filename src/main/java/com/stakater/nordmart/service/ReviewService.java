package com.stakater.nordmart.service;

import com.stakater.nordmart.exception.InvalidDataException;
import com.stakater.nordmart.model.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getReviews(String productId);
    Review addReview(String productId, String customerName, String rating, String text)
            throws InvalidDataException;
    String deleteReview(String reviewId);
}
