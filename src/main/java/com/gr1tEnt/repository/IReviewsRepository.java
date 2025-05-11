package com.gr1tEnt.repository;

import com.gr1tEnt.models.Review;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

public interface IReviewsRepository {
    boolean addReview(Review review);
    Optional<Review> findReviewById(UUID reviewId);
    List<Review> findReviewsByProductId(UUID productId);
    List<Review> findReviewsByCustomerId(UUID customerId);
    List<Review> findReviewsByRating(int rating);
    boolean deleteReview(UUID reviewId, UUID customerId);
    OptionalDouble getAverageRatingForProduct(UUID productId);
    long countReviewsForProduct(UUID productId);
    List<UUID> findProductIdsWithAverageRatingAbove(double minAverageRating);
    List<UUID> findCustomerIdsWhoWroteMoreThanNReviews(int minReviewCount);
    List<Review> findNRecentReviewsForProduct(UUID productId, int limit);
}
