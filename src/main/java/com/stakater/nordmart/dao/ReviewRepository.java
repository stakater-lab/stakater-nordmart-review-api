package com.stakater.nordmart.dao;


import com.stakater.nordmart.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByProductId(String productId);
    Review findReviewById(String reviewId);
    void deleteById(String id);

}
