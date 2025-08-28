package com.bosch.internship;

import com.bosch.internship.controller.impl.CartControllerImpl;
import com.bosch.internship.entity.Cart;
import com.bosch.internship.entity.User;
import com.bosch.internship.repo.UserRepository;
import com.bosch.internship.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartControllerImplTest {

    @Mock
    private CartService cartService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartControllerImpl cartController;

    private User user;
    private Cart cart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .build();

        cart = Cart.builder()
                .id(1L)
                .user(user)
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
    }

    @Test
    void testGetCart() {
        when(cartService.getCartForUser(user)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.getCart();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());

        verify(cartService, times(1)).getCartForUser(user);
    }

    @Test
    void testAddToCart() {
        when(cartService.addItem(user, 100L, 2)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.addToCart(100L, 2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(cartService, times(1)).addItem(user, 100L, 2);
    }

    @Test
    void testUpdateCartItem() {
        when(cartService.updateItem(user, 10L, 5)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.updateCartItem(10L, 5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(cartService, times(1)).updateItem(user, 10L, 5);
    }

    @Test
    void testRemoveCartItem() {
        doNothing().when(cartService).removeItem(user, 10L);

        ResponseEntity<Void> response = cartController.removeCartItem(10L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(cartService, times(1)).removeItem(user, 10L);
    }
}

