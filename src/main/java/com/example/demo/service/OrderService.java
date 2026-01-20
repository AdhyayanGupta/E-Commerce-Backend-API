package com.example.demo.service;

import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final CartItemRepository cartRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    public OrderService(CartItemRepository c, ProductRepository p, OrderRepository o) {
        this.cartRepo = c;
        this.productRepo = p;
        this.orderRepo = o;
    }

    public Order createOrder(String userId) {
        List<CartItem> cart = cartRepo.findByUserId(userId);
        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (CartItem c : cart) {
            Product p = productRepo.findById(c.getProductId()).orElseThrow();
            p.setStock(p.getStock() - c.getQuantity());
            productRepo.save(p);

            OrderItem oi = new OrderItem();
            oi.setProductId(p.getId());
            oi.setQuantity(c.getQuantity());
            oi.setPrice(p.getPrice());
            items.add(oi);

            total += p.getPrice() * c.getQuantity();
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setItems(items);
        order.setTotalAmount(total);
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());

        cartRepo.deleteAll(cart);
        return orderRepo.save(order);
    }

    public Order get(String id) {
        return orderRepo.findById(id).orElseThrow();
    }
}
