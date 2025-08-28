package com.bosch.internship.service;

import com.bosch.internship.entity.Cart;
import com.bosch.internship.entity.User;

public interface CartService {
    Cart getCartForUser(User user);
    Cart addItem(User user, Long productId, int quantity);
    Cart updateItem(User user, Long itemId, int quantity);
    void removeItem(User user, Long itemId);
}
