package com.gr1tEnt.repository;

import com.gr1tEnt.models.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class JdbcReviewsRepository implements IReviewsRepository {
    private final Connection conn;

    public JdbcReviewsRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean addReview(Review review) {
        String sql = "INSERT INTO reviews (review_id, product_id, customer_id, rating, comment_text, review_date) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, String.valueOf(review.getReview_id()));
            stmt.setString(2, String.valueOf(review.getProduct_id()));
            stmt.setString(3, String.valueOf(review.getCustomer_id()));
            stmt.setInt(4, review.getRating());
            stmt.setString(5, review.getComment_text());
            stmt.setObject(6, Instant.now());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Review> findReviewById(UUID reviewId) {
        String sql = "SELECT review_id, product_id, customer_id, rating, comment_text, review_date " +
                "FROM reviews " +
                "WHERE review_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, String.valueOf(reviewId));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Review review = new Review(
                            UUID.fromString(rs.getString("review_id")),
                            UUID.fromString(rs.getString("product_id")),
                            UUID.fromString(rs.getString("customer_id")),
                            rs.getInt("rating"),
                            rs.getString("comment_text"),
                            rs.getObject("review_date", Instant.class)
                    );
                    return Optional.of(review);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
