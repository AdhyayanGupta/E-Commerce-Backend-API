package com.example.demo.service;

import com.cashfree.Cashfree;
import com.cashfree.model.*;
// REMOVE THIS: import io.swagger.client.ApiResponse;
import com.cashfree.ApiResponse; // <--- ADD THIS

import com.example.demo.model.Order;
import com.example.demo.model.Payment;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final OrderRepository orderRepo;

    public PaymentService(PaymentRepository p, OrderRepository o) {
        this.paymentRepo = p;
        this.orderRepo = o;
    }

    public Payment create(String orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 1. Prepare Customer Details (Fetch real email/phone from your User Repo)
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setCustomerId(order.getUserId());
        customerDetails.setCustomerPhone("9999999999"); // TODO: Replace with real user phone
        customerDetails.setCustomerEmail("user@example.com"); // TODO: Replace with real user email

        // 2. Prepare Order Meta
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setOrderAmount((double) order.getTotalAmount());
        orderRequest.setOrderCurrency("INR");
        orderRequest.setOrderId(orderId);
        orderRequest.setCustomerDetails(customerDetails);

        try {
            // 3. Call Cashfree API
            Cashfree pg = new Cashfree();
// Change the type from io.swagger.client.ApiResponse to just ApiResponse
            ApiResponse<OrderEntity> response = pg.PGCreateOrder("2023-08-01", orderRequest, null, null, null);
            if (response.getData() != null) {
                // 4. Save Payment Session in DB
                Payment p = new Payment();
                p.setOrderId(orderId);
                p.setAmount(order.getTotalAmount());
                p.setStatus("PENDING");
                p.setCashfreeOrderId(response.getData().getOrderId());
                p.setPaymentSessionId(response.getData().getPaymentSessionId());

                return paymentRepo.save(p);
            } else {
                throw new RuntimeException("Failed to create payment session");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing Cashfree payment: " + e.getMessage());
        }
    }

    // Used by Webhook to update status
    public void updateStatus(String orderId, String status) {
        Payment p = paymentRepo.findByOrderId(orderId);
        if (p == null) return; // or throw exception

        Order o = orderRepo.findById(orderId).orElse(null);

        p.setStatus(status);
        paymentRepo.save(p);

        if (o != null) {
            if ("SUCCESS".equalsIgnoreCase(status)) {
                o.setStatus("PAID");
            } else if ("FAILED".equalsIgnoreCase(status)) {
                o.setStatus("FAILED");
            }
            orderRepo.save(o);
        }
    }
}