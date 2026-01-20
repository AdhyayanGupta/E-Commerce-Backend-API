package com.example.demo.dto;

import lombok.Data;

@Data
public class CartRequest {
    private String userId;
    private String productId;
    private int quantity;
}
