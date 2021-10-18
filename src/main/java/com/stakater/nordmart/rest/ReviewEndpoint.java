package com.stakater.nordmart.rest;

import com.stakater.nordmart.model.Review;
import com.stakater.nordmart.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@RestController
@Path("/review")
public class ReviewEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(ReviewEndpoint.class);

    @Autowired
    private ReviewService reviewService;

    @GET
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Review>> getReview2(@PathParam("productId") String productId) throws Exception {
        List<Review> ret = reviewService.getReviews(productId);
        LOG.info("<rest getReview2");

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .body(ret);
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

    @GET
    @Path("/logError")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<String> logError() {
        String msg = "LOGGING ERROR FOR TILT HANZALA";
        LOG.error(msg);

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .body(msg);                
    }

}
