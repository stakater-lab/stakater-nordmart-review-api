package com.stakater.nordmart.service;

import com.stakater.nordmart.dao.ReviewRepository;
import com.stakater.nordmart.exception.InvalidDataException;
import com.stakater.nordmart.model.Review;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewServiceImpl implements ReviewService {
    static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

    final ReviewRepository repository;
    final MeterRegistry meterRegistry;
    // A map that contains the rating as key and counter as value
    final Map<Integer, Counter> ratingCounters = new HashMap<>();

    // mode is not final because it is getting value from application.properties
    @Value("${application.mode}")
    String mode;

    public ReviewServiceImpl(final ReviewRepository repository, final MeterRegistry meterRegistry) {
        this.repository = repository;
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        // Adding separate counter for each type of rating
        ratingCounters.put(1, buildRatingCounters(1));
        ratingCounters.put(2, buildRatingCounters(2));
        ratingCounters.put(3, buildRatingCounters(3));
        ratingCounters.put(4, buildRatingCounters(4));
        ratingCounters.put(5, buildRatingCounters(5));

        if ("dev".contentEquals(mode)) {
            repository.deleteAll();
            // cache dummy review
            List<Review> dummy = new ArrayList<>();
            dummy.add(new Review("329199", "Tolvan+Tolvansson - Callum", "3", "I+think+this+sticker+is+ok"));
            dummy.add(
                    new Review("329199", "Darth+Vader", "5", "Best+ever!+I+always+use+on+the+walls+of+the+death+star"));
            dummy.add(new Review("329199", "Stormtrooper0032", "5", "My+boss+forced+me+to+put+5+stars"));
            dummy.add(new Review("165613", "Frodo", "4", "Cool+enough+for+summer+warm+enough+for+winter"));
            dummy.add(new Review("165614", "Dr+Nykterstein", "1", "i+dont+like+it"));
            dummy.add(new Review("165614", "Marko+Polo", "2", "Not+what+I+was+looking+for"));
            dummy.add(new Review("165954", "Marko+Polo", "5", "Exactly+what+I+was+looking+for!"));
            dummy.add(new Review("444434", "Earthman", "3", "The+watch+is+average+I+think"));
            dummy.add(new Review("444435", "Luke+Skywalker", "4", "My+goto+practice+gadget"));
            try {
                for (Review review : dummy) {
                    repository.save(review);
                    // Increment the counter upon adding a new review
                    ratingCounters.get(review.getRating()).increment();
                    LOG.info("Saved dummy review: {}", review);
                }
            } catch (IllegalArgumentException iae) {
                LOG.error("Error saving the review");
                LOG.error(iae.getMessage());
            }
        }
    }

    private Counter buildRatingCounters(final Integer rating) {
        return Counter.builder("nordmart-review.ratings") // 2 - create a counter using the fluent API
                .tag("rating", rating.toString())
                .description("Total number of ratings for all product")
                .register(meterRegistry);
    }

    @Override
    public List<Review> getReviews(final String productId) {
        LOG.info("getReviews: {}", productId);
        List<Review> ret = new ArrayList<>();
        for (Review review : repository.findByProductId(productId)) {
            LOG.info("Review: {}", review);
            ret.add(review);
        }
        return ret;
    }

    @Override
    public Review addReview(final String productId, final String customerName, final String rating, final String text)
            throws InvalidDataException {
        validateReview(productId, rating);
        LOG.info("addReview: productId: {}, customerName: {}, rating: {}, text: {}", productId, customerName, rating,
                text);
        Review review = new Review(productId, customerName, rating, text);
        Date now = new Date();
        review.setDateTime(now);
        repository.save(review);
        LOG.info("added review: {}", review);
        // Increment the counter upon adding a new review
        if (Objects.nonNull(ratingCounters) && !ratingCounters.isEmpty()) {
            ratingCounters.get(Review.getRangedRating(NumberUtils.toInt(rating, 3))).increment();
        }
        return review;
    }

    // Micrometer Prometheus counter doesn't support decrement, so can't decrease
    // the counter upone deletion
    @Override
    public String deleteReview(final String reviewId) {
        LOG.info("deleteReview: {}", reviewId);
        String response = String.format("Review does not exist for reviewId: %s", reviewId);
        Review review = repository.findReviewById(reviewId);
        if (Objects.nonNull(review)) {
            repository.deleteById(reviewId);
            response = String.format("Deleted review: %s", reviewId);
        }
        LOG.info(response);
        return response;
    }

    private void validateReview(final String productId, final String rating) throws InvalidDataException {
        String errorMessage = null;
        if (StringUtils.isBlank(productId)) {
            errorMessage = "Error while saving the review. ProductId cannot be blank";
        } else if (!NumberUtils.isParsable(rating)) {
            errorMessage = "Error while saving the review. Rating can only be a number";
        }
        if (Objects.nonNull(errorMessage)) {
            LOG.error(errorMessage);
            throw new InvalidDataException(errorMessage);
        }
    }
}
