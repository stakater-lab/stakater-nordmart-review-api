package com.stakater.nordmart.service;

import com.mongodb.BasicDBObject;
import com.stakater.nordmart.dao.ReviewRepository;
import com.stakater.nordmart.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ReviewServiceImpl implements ReviewService {
    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    private ReviewRepository repository;

    @Value("${application.mode}")
    String mode;


    @PostConstruct
    public void init() {
        if ("dev".contentEquals(mode)) {
            repository.deleteAll();
            // cache dummy review
            Review dummy = new Review("666", "Dummy Review0", 3, "dev team thinks this product is ok...");
            repository.save(dummy);
            LOG.info("Saved dummy review: " + dummy.toString());
        }
    }

    @Override
    public List<Review> getReviews(String productId) {
        LOG.info("getReviews: " + productId);
        List<Review> ret = new ArrayList<Review>();
        for (Review review : repository.findByProductId(productId)) {
            LOG.info(review.toString());
            ret.add(review);

        }
        return ret;

    }

    @Override
    public Review addReview(String productId, String costumerName, int rating, String text) {

        LOG.info("addReview: productId: " + productId + ", customerName: " + costumerName+ ", rating: " + rating+ ", text: " + text);
        Review review = new Review(productId, costumerName, rating, text);
        Date now = new Date();
        review.setDateTime(now);
        BasicDBObject timeNow = new BasicDBObject("date", now);
        repository.save(review);
        return review;
    }

    @Override
    public void deleteReview(String reviewId) {
        LOG.info("deleteReview: " + reviewId);
        repository.deleteById(reviewId);
    }
}
