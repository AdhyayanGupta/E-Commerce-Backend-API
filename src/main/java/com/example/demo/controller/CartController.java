package com.example.demo.controller;

import com.example.demo.dto.CartRequest;
import com.example.demo.model.CartItem;
import com.example.demo.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService service;

    public CartController(CartService s) {
        this.service = s;
    }

    @PostMapping("/add")
    public CartItem add(@RequestBody CartRequest r) {
        return service.add(r.getUserId(), r.getProductId(), r.getQuantity());
    }

    @GetMapping("/{userId}")
    public List<CartItem> get(@PathVariable String userId) {
        return service.getUserCart(userId);
    }

    @DeleteMapping("/{userId}/clear")
    public void clear(@PathVariable String userId) {
        service.clear(userId);
    }
}

