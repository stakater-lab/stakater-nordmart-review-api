package com.stakater.nordmart.service;

import com.stakater.nordmart.dao.ReviewRepository;
import com.stakater.nordmart.model.Review;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class ReviewServiceImpl implements ReviewService {
    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository repository;
    private final MeterRegistry meterRegistry;
    private Map<Integer, Counter> ratingCounters = new HashMap<>();

    @Value("${application.mode}")
    String mode;

    public ReviewServiceImpl(ReviewRepository repository, MeterRegistry meterRegistry) {
        this.repository = repository;
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        if ("dev".contentEquals(mode)) {
            repository.deleteAll();
            // cache dummy review
            List<Review> dummy = new ArrayList<>();
            dummy.add(new Review("329199", "Tolvan+Tolvansson - Callum", "3", "I+think+this+sticker+is+ok"));
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
                    LOG.info("Saved dummy review: {}", r);
                }
            } catch (IllegalArgumentException iae) {
                LOG.error("Error saving the review");
                LOG.error(iae.getMessage());
            }
        }

        ratingCounters.put(1, buildRatingCounters(1));
        ratingCounters.put(2, buildRatingCounters(2));
        ratingCounters.put(3, buildRatingCounters(3));
        ratingCounters.put(4, buildRatingCounters(4));
        ratingCounters.put(5, buildRatingCounters(5));
    }

    private Counter buildRatingCounters(final Integer rating) {
        return Counter.builder("nordmart-review.ratings")    // 2 - create a counter using the fluent API
                .tag("rating", rating.toString())
                .description("Total number of ratings for all product")
                .register(meterRegistry);
    }

    @Override
    public List<Review> getReviews(String productId) {
        LOG.info("getReviews: {}", productId);
        List<Review> ret = new ArrayList<>();
        try {
            for (Review review : repository.findByProductId(productId)) {
                LOG.info("Review: {}", review);
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

        LOG.info("addReview: productId: {}, customerName: {}, rating: {}, text: {}", productId, customerName, rating, text);
        Review review = new Review(productId, customerName, rating, text);
        Date now = new Date();
        review.setDateTime(now);
        try {
            repository.save(review);
            LOG.info("added review: {}", review);
            ratingCounters.get(Review.getRangedRating(NumberUtils.toInt(rating, 3))).increment();
        } catch (NumberFormatException ne) {
            LOG.warn("error parsing the rating to Integer from string, will not increment rating counter");
        } catch (IllegalArgumentException iae) {
            LOG.error("error saving review");
            LOG.error(iae.getMessage());
        }
        return review;
    }

    @Override
    public void deleteReview(String reviewId) {
        LOG.info("deleteReview: {}", reviewId);
        try {
            repository.deleteById(reviewId);
            LOG.info("deleted review: {}", reviewId);
        } catch (IllegalArgumentException iae) {
            LOG.error("error deleting review");
            LOG.error(iae.getMessage());
        }
    }
}
