package com.gr1tEnt.repository;

import com.gr1tEnt.models.Category;
import com.gr1tEnt.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcProductRepository implements IProductsRepository {
    private final Connection conn;

    public JdbcProductRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products (product_name, product_description, price, stock_quantity) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProduct_name());
            stmt.setString(2, product.getProduct_description());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getStock_quantity());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Product> findProductById(UUID productId) {
        return Optional.empty();
    }

    @Override
    public int updateStockQuantity(UUID productId, int quantity) {
        return 0;
    }

    @Override
    public boolean deleteProductById(UUID productId) {
        return false;
    }

    @Override
    public List<Product> findProductsByCategory(Category category) {
        return List.of();
    }
}
