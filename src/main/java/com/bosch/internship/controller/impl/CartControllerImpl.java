package com.bosch.internship.controller.impl;

import com.bosch.internship.controller.CartController;
import com.bosch.internship.entity.Cart;
import com.bosch.internship.entity.User;
import com.bosch.internship.repo.UserRepository;
import com.bosch.internship.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartControllerImpl implements CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    private User getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    @Override
    public ResponseEntity<Cart> getCart() {
        return ResponseEntity.ok(cartService.getCartForUser(getCurrentUser()));
    }

    @Override
    public ResponseEntity<Cart> addToCart(Long productId, int quantity) {
        return ResponseEntity.ok(cartService.addItem(getCurrentUser(), productId, quantity));
    }

    @Override
    public ResponseEntity<Cart> updateCartItem(Long id, int quantity) {
        return ResponseEntity.ok(cartService.updateItem(getCurrentUser(), id, quantity));
    }

    @Override
    public ResponseEntity<Void> removeCartItem(Long id) {
        cartService.removeItem(getCurrentUser(), id);
        return ResponseEntity.noContent().build();
    }
}
