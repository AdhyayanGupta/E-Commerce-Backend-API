package com.example.demo.repository;

import com.example.demo.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    Payment findByOrderId(String orderId);
    Payment findByCashfreeOrderId(String cashfreeOrderId);
}

