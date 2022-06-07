package com.stakater.nordmart.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Review.class)
class ReviewTest {

    @ParameterizedTest
    @CsvSource({"-2,1", "7,5", "2,2"})
    void getRangedRatingTest(final int inputRating, final int responseRating) {
        // WHEN
        int result = Review.getRangedRating(inputRating);

        // THEN
        Assertions.assertEquals(responseRating, result);
    }
}
