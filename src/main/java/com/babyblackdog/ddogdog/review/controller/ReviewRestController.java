package com.babyblackdog.ddogdog.review.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.babyblackdog.ddogdog.review.controller.dto.ReviewResponse;
import com.babyblackdog.ddogdog.review.service.ReviewService;
import com.babyblackdog.ddogdog.review.service.dto.ReviewResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/reviews", produces = APPLICATION_JSON_VALUE)
public class ReviewRestController {

  private final ReviewService reviewService;

  public ReviewRestController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @PostMapping
  public ResponseEntity<ReviewResponse> createReview(
      @RequestParam Long reservationId,
      @RequestParam String content,
      @RequestParam Double rating) {

    ReviewResult addedReview = reviewService.registerReview(reservationId, content, rating);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ReviewResponse.of(addedReview));
  }

}
