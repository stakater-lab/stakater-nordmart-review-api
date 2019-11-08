package com.stakater.nordmart.rest;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.stakater.nordmart.model.Review;
import com.stakater.nordmart.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RestController
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION)
@Path("/review")
public class ReviewEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(ReviewEndpoint.class);


    @Autowired
    private ReviewService reviewService;

    @GET
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Review> getReview(@PathParam("productId") String productId) {
        return reviewService.getReviews(productId);
    }

    @POST
    @Path("/{productId}/{customerName}/{rating}/{text}")
    @Produces(MediaType.APPLICATION_JSON)
    public Review add(@PathParam("productId") String productId,
                   @PathParam("customerName") String customerName,
                   @PathParam("rating") int rating,
                   @PathParam("text") String text
                   ) throws Exception {
        return reviewService.addReview(productId, customerName, rating, text);
    }


    @DELETE
    @Path("/{reviewId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("reviewId") String reviewId
    ) throws Exception {
        reviewService.deleteReview(reviewId);
    }

}
