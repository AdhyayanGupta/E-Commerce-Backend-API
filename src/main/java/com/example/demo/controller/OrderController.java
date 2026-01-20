package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService s) {
        this.service = s;
    }

    @PostMapping("/{userId}")
    public Order create(@PathVariable String userId) {
        return service.createOrder(userId);
    }

    @GetMapping("/{orderId}")
    public Order get(@PathVariable String orderId) {
        return service.get(orderId);
    }
}
