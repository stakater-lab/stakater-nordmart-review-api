package com.stakater.nordmart.rest;


import javax.ws.rs.*;
import org.springframework.http.MediaType;

import com.stakater.nordmart.model.Review;
import com.stakater.nordmart.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
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
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public List<Review> getReview(@PathParam("productId") String productId) throws Exception {
        List<Review> ret = reviewService.getReviews(productId);
        LOG.info("<rest getReview");
        return ret;
    }

    @POST
    @Path("/{productId}/{customerName}/{rating}/{text}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Review add(@PathParam("productId") String productId,
                   @PathParam("customerName") String customerName,
                   @PathParam("rating") String rating,
                   @PathParam("text") String text
                   ) throws Exception {
        return reviewService.addReview(productId, customerName, rating, text);
    }


    @DELETE
    @Path("/{reviewId}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathParam("reviewId") String reviewId
    ) throws Exception {
        reviewService.deleteReview(reviewId);
    }

}
