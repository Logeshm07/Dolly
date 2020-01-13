package com.example.ecommerce.demo.model;

import lombok.Data;

@Data
public class ChargeRequest {

    public enum Currency {
        USD
    }
    private String description;
    private long amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;
    //private String timestamp;
}