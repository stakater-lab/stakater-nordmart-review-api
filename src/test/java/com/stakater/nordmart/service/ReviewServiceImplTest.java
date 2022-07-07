package com.stakater.nordmart.service;

import com.stakater.nordmart.dao.ReviewRepository;
import com.stakater.nordmart.exception.InvalidDataException;
import com.stakater.nordmart.model.Review;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@FieldDefaults(level = AccessLevel.PRIVATE)
class ReviewServiceImplTest {
    ReviewServiceImpl reviewService;

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
        reviewService = new ReviewServiceImpl(reviewRepository, new SimpleMeterRegistry());
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
        List<Review> reviews = reviewService.getReviews(productId);

        // THEN
        assertAll("Test Get review",
                () -> assertEquals(3, reviews.size()),
                () -> assertEquals(3, reviews.get(0).getRating()),
                () -> assertEquals("Tolvan+Tolvansson - Callum", reviews.get(0).getCustomerName()),
                () -> assertEquals("I+think+this+sticker+is+ok", reviews.get(0).getReviewText()));
    }

    @Test
    void getInvalidReviewsTest() {
        // GIVEN
        String productId = "testId";

        // WHEN
        List<Review> reviews = reviewService.getReviews(productId);

        // THEN
        assertAll("Test Get Invalid review",
                () -> assertNotNull(reviews),
                () -> assertEquals(0, reviews.size()));
    }

    @Test
    @SneakyThrows
    void addReviewTest() {
        // GIVEN
        String productId = "123456";
        String customerName = "Callum";
        String rating = "5";
        String text = "Good";

        // WHEN
        Review review = reviewService.addReview(productId, customerName, rating, text);

        // THEN
        assertAll("Test Add review",
                () -> assertEquals(productId, review.getProductId()),
                () -> assertEquals(5, review.getRating()),
                () -> assertEquals(customerName, review.getCustomerName()),
                () -> assertEquals(text, review.getReviewText()));
    }

    @Test
    @SneakyThrows
    void addReviewInvalidProductIdTest() {
        // GIVEN
        String productId = null;
        String customerName = "Callum";
        String rating = "5";
        String text = "Good";

        // WHEN
        InvalidDataException thrown = assertThrows(
                InvalidDataException.class,
                () -> reviewService.addReview(productId, customerName, rating, text),
                "Expected addReview() to throw, but it didn't");

        // THEN
        assertTrue(thrown.getMessage().contains("Error while saving the review. ProductId cannot be blank"));
    }

    @Test
    @SneakyThrows
    void addReviewInvalidRatingTest() {
        // GIVEN
        String productId = "12345";
        String customerName = "Callum";
        String rating = "invalid ratings";
        String text = "Good";

        // WHEN
        InvalidDataException thrown = assertThrows(
                InvalidDataException.class,
                () -> reviewService.addReview(productId, customerName, rating, text),
                "Expected addReview() to throw, but it didn't");

        // THEN
        assertTrue(thrown.getMessage().contains("Error while saving the review. Rating can only be a number"));
    }

    @Test
    void deleteReviewTest() {
        // GIVEN
        String productId = "329199";
        List<Review> reviews = reviewService.getReviews(productId);
        String reviewId = reviews.get(0).getId();
        String expectedResponse = String.format("Deleted review: %s", reviewId);

        // WHEN
        String response = reviewService.deleteReview(reviewId);
        List<Review> reviewResult = reviewService.getReviews(productId);

        // THEN
        assertAll("Test Delete review",
                () -> assertEquals(expectedResponse, response),
                () -> assertEquals(2, reviewResult.size()));
    }

    @Test
    void deleteReviewInvalidReviewIdTest() {
        // GIVEN
        String reviewId = "Invalid Review Id";
        String expectedResponse = String.format("Review does not exist for reviewId: %s", reviewId);

        // WHEN
        String response = reviewService.deleteReview(reviewId);

        // THEN
        assertEquals(expectedResponse, response);
    }
}
