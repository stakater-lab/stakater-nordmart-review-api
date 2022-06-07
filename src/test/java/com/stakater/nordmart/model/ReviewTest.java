package com.stakater.nordmart.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Review.class)
class ReviewTest {

    @Test
    void getRangedRatingTest() {
        // GIVEN
        int negativeRating = -2;
        int ratingGreaterThanFive = 7;

        // WHEN
        int negativeRatingResult = Review.getRangedRating(negativeRating);
        int ratingGreaterThanFiveResult = Review.getRangedRating(ratingGreaterThanFive);

        // THEN
        Assertions.assertEquals(1, negativeRatingResult);
        Assertions.assertEquals(5, ratingGreaterThanFiveResult);
    }
}
