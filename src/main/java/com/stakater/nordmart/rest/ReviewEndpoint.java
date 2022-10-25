package com.stakater.nordmart.rest;

import com.stakater.nordmart.exception.InvalidDataException;
import com.stakater.nordmart.model.Review;
import com.stakater.nordmart.service.ReviewService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewEndpoint {
    static final Logger LOG = LoggerFactory.getLogger(ReviewEndpoint.class);
    final ReviewService reviewService;

    @GET
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Review>> get(final @PathParam("productId") String productId) {
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
    public ResponseEntity add(final @PathParam("productId") String productId,
                              final @PathParam("customerName") String customerName,
                              final @PathParam("rating") String rating,
                              final @PathParam("text") String text) {
        try {
            Review review = reviewService.addReview(productId, customerName, rating, text);
            return ResponseEntity.ok(review);
        } catch (InvalidDataException ie) {
            return ResponseEntity.badRequest().body(ie.getMessage());
        }
    }

    @DELETE
    @Path("/{reviewId}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(final @PathParam("reviewId") String reviewId) {
        String response = reviewService.deleteReview(reviewId);
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .body(response);
    }

    @GET
    @Path("/logError")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public ResponseEntity<String> logError() {
        String msg = "LOG ERROR MSG";
        LOG.error(msg);

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .body(msg);
    }

}
