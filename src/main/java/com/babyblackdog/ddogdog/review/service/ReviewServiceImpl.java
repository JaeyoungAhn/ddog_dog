package com.babyblackdog.ddogdog.review.service;

import com.babyblackdog.ddogdog.review.domain.Review;
import com.babyblackdog.ddogdog.review.domain.vo.Content;
import com.babyblackdog.ddogdog.review.domain.vo.Rating;
import com.babyblackdog.ddogdog.review.service.dto.ReviewResult;
import com.babyblackdog.ddogdog.reviewRoomReservationMapping.domain.ReviewRoomReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ReviewServiceImpl implements ReviewService {

  private final ReviewStore store;
  private final ReviewReader reader;

  public ReviewServiceImpl(ReviewStore store, ReviewReader reader) {
    this.store = store;
    this.reader = reader;
  }

  @Transactional
  @Override
  public ReviewResult registerReview(Long roomId, Long reservationId, String content, Double rating) {
    Review review = new Review(new Content(content), new Rating(rating));
    Review savedReview = store.registerReview(review);
    return ReviewResult.of(savedReview);
  }

  @Transactional
  @Override
  public ReviewResult updateReview(Long reviewId, String content, Double rating) {
    Review retrievedReview = reader.findReviewById(reviewId);
    retrievedReview.setContent(new Content(content));
    retrievedReview.setRating(new Rating(rating));
    return ReviewResult.of(retrievedReview);
  }

  @Override
  public Page<ReviewResult> findReviewsByUserId(Long userId, Pageable pageable) {
    Page<Review> retrievedReviews = reader.findReviewsByUserId(userId, pageable);
    return retrievedReviews.map(ReviewResult::of);
  }

  @Override
  public Page<ReviewResult> findReviewsByReviewRoomReservation(Page<ReviewRoomReservation> reviewRoomReservations) {
    Page<Review> retrievedReviews = reader.findReviewsByRoomId(reviewRoomReservations.get());
    return retrievedReviews.map(ReviewResult::of);
  }

}
