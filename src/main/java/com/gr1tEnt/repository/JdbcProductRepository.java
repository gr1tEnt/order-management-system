package com.gr1tEnt.repository;

import com.gr1tEnt.models.Category;
import com.gr1tEnt.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        String sql = "SELECT product_id, product_name, product_description, price, stock_quantity " +
                "FROM products " +
                "WHERE product_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, String.valueOf(productId));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Product product = new Product(
                        UUID.fromString(rs.getString("product_id")),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity"),
                        Category.valueOf(rs.getString("category"))
                );
                return Optional.of(product);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateStockQuantity(UUID productId, int quantity) {
        String sql = "UPDATE product " +
                "SET stock_quantity = ? " +
                "WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setString(2, String.valueOf(productId));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
