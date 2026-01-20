package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService s) {
        this.service = s;
    }

    @PostMapping
    public Product create(@RequestBody Product p) {
        return service.create(p);
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }
}
