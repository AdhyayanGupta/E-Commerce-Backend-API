package com.example.demo.controller;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.model.Payment;
import com.example.demo.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService s) {
        this.service = s;
    }

    @PostMapping("/create")
    public Payment create(@RequestBody PaymentRequest r) {
        return service.create(r.getOrderId());
    }
}

