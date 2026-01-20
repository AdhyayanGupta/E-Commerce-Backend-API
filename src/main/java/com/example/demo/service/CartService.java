package com.example.demo.service;

import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartRepo;
    private final ProductRepository productRepo;

    public CartService(CartItemRepository c, ProductRepository p) {
        this.cartRepo = c;
        this.productRepo = p;
    }

    public CartItem add(String userId, String productId, int qty) {
        Product p = productRepo.findById(productId).orElseThrow();

        if (p.getStock() < qty) throw new RuntimeException("Out of stock");

        CartItem item = cartRepo.findByUserIdAndProductId(userId, productId);
        if (item == null) {
            item = new CartItem();
            item.setUserId(userId);
            item.setProductId(productId);
            item.setQuantity(qty);
        } else {
            item.setQuantity(item.getQuantity() + qty);
        }
        return cartRepo.save(item);
    }

    public List<CartItem> getUserCart(String userId) {
        return cartRepo.findByUserId(userId);
    }

    public void clear(String userId) {
        cartRepo.deleteAll(getUserCart(userId));
    }
}
