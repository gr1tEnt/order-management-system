package com.gr1tEnt.models;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Customer {
    private UUID customer_id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String address;
}
