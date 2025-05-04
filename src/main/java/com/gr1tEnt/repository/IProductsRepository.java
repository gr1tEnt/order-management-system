package com.gr1tEnt.repository;

import com.gr1tEnt.models.Category;
import com.gr1tEnt.models.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProductsRepository {
    boolean addProduct(Product product);
    Optional<Product> findProductById(UUID productId);
    int updateStockQuantity(UUID productId, int quantity);
    boolean deleteProductById(UUID productId);
    List<Product> findProductsByCategory(Category category);
}
