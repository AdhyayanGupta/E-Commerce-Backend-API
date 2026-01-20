package com.example.demo.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private double price;
    private int stock;
}