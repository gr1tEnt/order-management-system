package com.gr1tEnt.repository;

import com.gr1tEnt.models.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;

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
}
