package com.stakater.nordmart.rest;


import javax.ws.rs.*;
import org.springframework.http.MediaType;

import com.stakater.nordmart.model.Review;
import com.stakater.nordmart.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ReviewEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(ReviewEndpoint.class);


    @Autowired
    private ReviewService reviewService;

    @GetMapping("review/{productId}")
    public List<Review> getReview(@PathParam("productId") String productId) throws Exception {
        List<Review> ret = reviewService.getReviews(productId);
        LOG.info("<rest getReview");
        return ret;
    }

    @PostMapping("review/{productId}/{customerName}/{rating}/{text}")
    public Review add(@PathParam("productId") String productId,
                   @PathParam("customerName") String customerName,
                   @PathParam("rating") String rating,
                   @PathParam("text") String text
                   ) throws Exception {
        return reviewService.addReview(productId, customerName, rating, text);
    }


    @DeleteMapping("review/{reviewId}")
    public void delete(@PathParam("reviewId") String reviewId
    ) throws Exception {
        reviewService.deleteReview(reviewId);
    }

}
