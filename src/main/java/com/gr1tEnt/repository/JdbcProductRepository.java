package com.gr1tEnt.repository;

import com.gr1tEnt.models.ProductCategory;
import com.gr1tEnt.models.Product;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcProductRepository implements IProductsRepository {
    private final Connection conn;

    public JdbcProductRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products (product_id, product_name, product_description, price, stock_quantity, category) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, String.valueOf(product.getProduct_id()));
            stmt.setString(2, product.getProduct_name());
            stmt.setString(3, product.getProduct_description());
            stmt.setBigDecimal(4, product.getPrice());
            stmt.setInt(5, product.getStock_quantity());
            stmt.setString(6, product.getCategory().name());

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
                        rs.getBigDecimal("price"),
                        rs.getInt("stock_quantity"),
                        ProductCategory.valueOf(rs.getString("category"))
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
        String sql = "DELETE " +
                "FROM products " +
                "WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(productId));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> findProductsByCategory(ProductCategory category) {
        List<Product> currentProducts = new ArrayList<>();
        String sql = "SELECT product_id, product_name, product_description, price, stock_quantity, category " +
                "FROM products " +
                "WHERE category = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(category));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        UUID.fromString(rs.getString("product_id")),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock_quantity"),
                        ProductCategory.valueOf(rs.getString("category"))
                );
                currentProducts.add(product);
            }
            return currentProducts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<ProductCategory, Long> countProductsByCategory() {
        Map<ProductCategory, Long> groupedByCategory = new HashMap<>();

        String sql = "SELECT COUNT(product_id) AS quantity, category " +
                "FROM products " +
                "GROUP BY category " +
                "ORDER BY COUNT(product_id) DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                groupedByCategory.put(
                        ProductCategory.valueOf(rs.getString("category")),
                        rs.getLong("quantity")
                );
            }

            return groupedByCategory;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> findProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT product_id, product_name, product_description, price, stock_quantity, category " +
                "FROM products " +
                "WHERE price >= ? AND price =< ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, minPrice);
            stmt.setBigDecimal(2, maxPrice);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        UUID.fromString(rs.getString("product_id")),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock_quantity"),
                        ProductCategory.valueOf(rs.getString("category"))
                );
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> findProductsByNameLike(String namePattern) {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT product_id, product_name, product_description, price, stock_quantity, category " +
                "FROM products " +
                "WHERE product_name LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String pattern = "%" + namePattern + "%";

            stmt.setString(1, pattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                        UUID.fromString(rs.getString("product_id")),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock_quantity"),
                        ProductCategory.valueOf(rs.getString("category"))
                );
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> findProductsInStock() {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT product_id, product_name, product_description, price, stock_quantity, category " +
                "FROM products " +
                "WHERE stock_quantity > 0";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                        UUID.fromString(rs.getString("product_id")),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock_quantity"),
                        ProductCategory.valueOf(rs.getString("category"))
                );
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<ProductCategory, Integer> getTotalStockQuantityPerCategory() {
        Map<ProductCategory, Integer> quantityPerCategory = new HashMap<>();

        String sql = "SELECT category, SUM(stock_quantity) AS quantity " +
                "FROM products " +
                "GROUP BY category";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                quantityPerCategory.put(ProductCategory.valueOf(rs.getString("category")),
                rs.getInt("quantity"));
            }
            return quantityPerCategory;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
