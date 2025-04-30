package com.gr1tEnt.models;

import lombok.*;

// CustomerDto for displaying info about the client
@Getter
@Setter
@ToString
public class CustomerDto {
    private String first_name;
    private String last_mane;
    private String address;
}