package com.gr1tEnt.repository;

import com.gr1tEnt.models.Category;
import com.gr1tEnt.models.Product;

import java.sql.Connection;
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
        return false;
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
