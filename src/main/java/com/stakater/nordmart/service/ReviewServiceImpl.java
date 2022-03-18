package com.stakater.nordmart.service;

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
            List<Review> dummy = new ArrayList<Review>();
            dummy.add(new Review("329199", "Rasheed", "3", "I+think+this+sticker+is+ok"));
            dummy.add(new Review("329199", "Darth+Vader", "5", "Best+ever!+I+always+use+on+the+walls+of+the+death+star"));
            dummy.add(new Review("329199", "Stormtrooper0032", "5", "My+boss+forced+me+to+put+5+stars"));
            dummy.add(new Review("165613", "Frodo", "4", "Cool+enough+for+summer+warm+enough+for+winter"));
            dummy.add(new Review("165614", "Dr+Nykterstein", "1", "i+dont+like+it"));
            dummy.add(new Review("165614", "Marko+Polo", "2", "Not+what+I+was+looking+for"));
            dummy.add(new Review("165954", "Marko+Polo", "5", "Exactly+what+I+was+looking+for!"));
            dummy.add(new Review("444434", "Earthman", "3", "The+watch+is+average+I+think"));
            dummy.add(new Review("444435", "Luke+Skywalker", "4", "My+goto+practice+gadget"));
            try {
                for(Review r : dummy){
                    repository.save(r);
                    LOG.info("Saved dummy review: " + r.toString());
                }
            } catch (IllegalArgumentException iae) {
                LOG.error("Error saving the review");
                LOG.error(iae.getMessage());
            }
        }
    }

    @Override
    public List<Review> getReviews(String productId) {
        LOG.info("getReviews: " + productId);
        List<Review> ret = new ArrayList<Review>();
        try {
            for (Review review : repository.findByProductId(productId)) {
                LOG.info(review.toString());
                ret.add(review);

            }
        } catch (Exception e) {
            LOG.error("error when getting reviews");
            LOG.error(e.getMessage());

        }
        return ret;
    }

    @Override
    public Review addReview(String productId, String customerName, String rating, String text) {

        LOG.info("addReview: productId: " + productId + ", customerName: " + customerName + ", rating: " + rating+ ", text: " + text);
        Review review = new Review(productId, customerName, rating, text);
        Date now = new Date();
        review.setDateTime(now);
        //BasicDBObject timeNow = new BasicDBObject("date", now);
        try {
            repository.save(review);
            LOG.info("added review: " + review.toString());
        } catch (IllegalArgumentException iae) {
            LOG.error("error saving review");
            LOG.error(iae.getMessage());
        }
        return review;
    }

    @Override
    public void deleteReview(String reviewId) {
        LOG.info("deleteReview: " + reviewId);
        try {
            repository.deleteById(reviewId);
            LOG.info("deleted review: " + reviewId);
        } catch (IllegalArgumentException iae) {
            LOG.error("error deleting review");
            LOG.error(iae.getMessage());
        }
    }
}
