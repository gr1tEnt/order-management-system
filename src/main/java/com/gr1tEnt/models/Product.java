package com.gr1tEnt.models;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Product {
    private UUID product_id;
    private String product_name;
    private String product_description;
    private BigDecimal price;
    private int stock_quantity;
    private ProductCategory category;
}
