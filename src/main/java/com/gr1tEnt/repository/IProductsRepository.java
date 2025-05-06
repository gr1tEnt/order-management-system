package com.gr1tEnt.repository;

import com.gr1tEnt.models.ProductCategory;
import com.gr1tEnt.models.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface IProductsRepository {
    boolean addProduct(Product product);
    Optional<Product> findProductById(UUID productId);
    boolean updateStockQuantity(UUID productId, int quantity);
    boolean deleteProductById(UUID productId);
    List<Product> findProductsByCategory(ProductCategory category);
    Map<ProductCategory, Long> countProductsByCategory();
    List<Product> findProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> findProductsByNameLike(String namePattern);
    List<Product> findProductsInStock();
}
