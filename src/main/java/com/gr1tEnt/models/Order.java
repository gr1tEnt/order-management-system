package com.gr1tEnt.models;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Order {
    private UUID order_id;
    private UUID customer_id;
    private LocalDate order_date;
    private OrderStatus status;
    private double total_amount;
    private String shipping_address;
}
