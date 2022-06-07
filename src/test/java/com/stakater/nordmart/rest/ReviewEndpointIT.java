package com.stakater.nordmart.rest;

import com.stakater.nordmart.dao.ReviewRepository;
import com.stakater.nordmart.model.Review;
import com.stakater.nordmart.service.ReviewServiceImpl;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DataMongoTest
@FieldDefaults(level = AccessLevel.PRIVATE)
class ReviewEndpointIT {

    ReviewEndpoint reviewEndpoint;

    @Autowired
    ReviewRepository reviewRepository;

    @BeforeEach
    public void setUp() {
        List<Review> dummy = new ArrayList<>();
        dummy.add(new Review("329199", "Tolvan+Tolvansson - Callum", "3", "I+think+this+sticker+is+ok"));
        dummy.add(new Review("329199", "Darth+Vader", "5", "Best+ever!+I+always+use+on+the+walls+of+the+death+star"));
        dummy.add(new Review("329199", "Stormtrooper0032", "5", "My+boss+forced+me+to+put+5+stars"));
        dummy.add(new Review("165613", "Frodo", "4", "Cool+enough+for+summer+warm+enough+for+winter"));
        dummy.add(new Review("165614", "Dr+Nykterstein", "1", "i+dont+like+it"));
        reviewRepository.saveAll(dummy);
        reviewEndpoint = new ReviewEndpoint(new ReviewServiceImpl(reviewRepository, new SimpleMeterRegistry()));
    }

    @AfterEach
    public void cleanUp() {
        reviewRepository.deleteAll();
    }

    @Test
    void getReviewsTest() {
        // GIVEN
        String productId = "329199";

        // WHEN
        ResponseEntity<List<Review>> responseEntity = reviewEndpoint.get(productId);
        List<Review> reviews = responseEntity.getBody();

        // THEN
        assertAll("Test Get review API",
                () -> assertEquals(3, reviews.size()),
                () -> assertEquals(3, reviews.get(0).getRating()),
                () -> assertEquals("Tolvan+Tolvansson - Callum", reviews.get(0).getCustomerName()),
                () -> assertEquals("I+think+this+sticker+is+ok", reviews.get(0).getReviewText()));
    }

    @Test
    void addReviewTest() {
        // GIVEN
        String productId = "123456";
        String customerName = "Callum";
        String rating = "5";
        String text = "Good";

        // WHEN
        ResponseEntity response = reviewEndpoint.add(productId, customerName, rating, text);
        Review review = (Review) response.getBody();

        // THEN
        assertAll("Test Add review API",
                () -> assertEquals(productId, review.getProductId()),
                () -> assertEquals(5, review.getRating()),
                () -> assertEquals(customerName, review.getCustomerName()),
                () -> assertEquals(text, review.getReviewText()));
    }

    @Test
    void addInvalidReviewTest() {
        // GIVEN
        String productId = null;
        String customerName = "Callum";
        String rating = "5";
        String text = "Good";

        // WHEN
        ResponseEntity response = reviewEndpoint.add(productId, customerName, rating, text);

        // THEN
        assertAll("Test Add review API",
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertInstanceOf(String.class, response.getBody()),
                () -> assertEquals("Error while saving the review. ProductId cannot be blank", response.getBody()));
    }

    @Test
    void deleteReviewTest() {
        // GIVEN
        String productId = "329199";
        ResponseEntity<List<Review>> responseEntity = reviewEndpoint.get(productId);
        List<Review> reviews = responseEntity.getBody();
        String reviewId = reviews.get(0).getId();
        String expectedResponse = String.format("Deleted review: %s", reviewId);

        // WHEN
        ResponseEntity<String> response = reviewEndpoint.delete(reviewId);
        List<Review> reviewResult = reviewEndpoint.get(productId).getBody();

        // THEN
        assertAll("Test Delete review API",
                () -> assertEquals(expectedResponse, response.getBody()),
                () -> assertEquals(2, reviewResult.size()));
    }
}
