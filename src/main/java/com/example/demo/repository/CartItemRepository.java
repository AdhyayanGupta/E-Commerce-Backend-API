package com.example.demo.repository;

import com.example.demo.model.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CartItemRepository extends MongoRepository<CartItem, String> {
    List<CartItem> findByUserId(String userId);
    CartItem findByUserIdAndProductId(String userId, String productId);
}
