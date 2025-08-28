package com.bosch.internship.service.impl;

import com.bosch.internship.entity.Cart;
import com.bosch.internship.entity.CartItem;
import com.bosch.internship.entity.Product;
import com.bosch.internship.entity.User;
import com.bosch.internship.repo.CartItemRepo;
import com.bosch.internship.repo.CartRepo;
import com.bosch.internship.repo.ProductRepo;
import com.bosch.internship.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductRepo productRepo;

    @Override
    public Cart getCartForUser(User user) {
        return cartRepo.findByUser(user)
                .orElseGet(()-> {
                    Cart cart = Cart.builder().user(user).build();
                    return cartRepo.save(cart);
                });
    }

    @Override
    public Cart addItem(User user, Long productId, int quantity) {

        Cart cart = getCartForUser(user);

        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found!"));

        CartItem exist = cart.getItems().stream()
                .filter(i ->  i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (exist != null){
            exist.setQuantity(exist.getQuantity() + quantity);
            cartItemRepo.save(exist);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();

            cart.getItems().add(newItem);
            cartItemRepo.save(newItem);
        }

        return cartRepo.save(cart);
    }

    @Override
    public Cart updateItem(User user, Long itemId, int quantity) {

        Cart cart = getCartForUser(user);

        CartItem cartItem = cartItemRepo.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Cart item not found"));
        if(!cartItem.getCart().getId().equals(cart.getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Item does not belong to this cart");

        cartItem.setQuantity(quantity);
        cartItemRepo.save(cartItem);

        return cartRepo.save(cart);
    }

    @Override
    public void removeItem(User user, Long itemId) {
        Cart cart = getCartForUser(user);

        CartItem cartItem = cartItemRepo.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Cart item not found"));
        if(!cartItem.getCart().getId().equals(cart.getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Item does not belong to this cart");

        cart.getItems().remove(cartItem);
        cartItemRepo.delete(cartItem);
        cartRepo.save(cart);

    }
}
