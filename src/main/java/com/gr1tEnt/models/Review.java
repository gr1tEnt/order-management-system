package com.gr1tEnt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Review {
    private UUID review_id;
    private UUID product_id;
    private UUID customer_id;
    private int rating;
    private String comment_text;
    private Instant review_date;
}
