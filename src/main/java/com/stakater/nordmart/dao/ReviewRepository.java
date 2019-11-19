package com.stakater.nordmart.dao;


import java.util.List;

import com.stakater.nordmart.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, String> {

    public List<Review> findByProductId(String productId);
    public void deleteById(String id);

}
