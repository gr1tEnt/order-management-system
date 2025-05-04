package com.gr1tEnt.models;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Product {
    private UUID product_id;
    private String product_name;
    private String product_description;
    private double price;
    private int stock_quantity;
    Category category;
}
