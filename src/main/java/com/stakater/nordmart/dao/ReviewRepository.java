package com.stakater.nordmart.dao;


import com.stakater.nordmart.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByProductId(final String productId);
    Review findReviewById(final String reviewId);
    void deleteById(final String id);

}
