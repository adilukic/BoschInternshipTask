package com.bosch.internship;

import com.bosch.internship.entity.Cart;
import com.bosch.internship.entity.CartItem;
import com.bosch.internship.entity.Product;
import com.bosch.internship.entity.User;
import com.bosch.internship.repo.CartItemRepo;
import com.bosch.internship.repo.CartRepo;
import com.bosch.internship.repo.ProductRepo;
import com.bosch.internship.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartRepo cartRepo;

    @Mock
    private CartItemRepo cartItemRepo;

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Cart cart;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().id(1L).username("testUser").build();
        cart = Cart.builder().id(1L).user(user).items(new ArrayList<>()).build();
        product = Product.builder().id(1L).name("Laptop").price(1000.0).build();
        cartItem = CartItem.builder().id(1L).cart(cart).product(product).quantity(2).build();
    }

    @Test
    void testGetCartForUser_NewCartCreated() {
        when(cartRepo.findByUser(user)).thenReturn(Optional.empty());
        when(cartRepo.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.getCartForUser(user);

        assertEquals(user, result.getUser());
        verify(cartRepo, times(1)).save(any(Cart.class));
    }

    @Test
    void testAddItem_NewItem() {
        when(cartRepo.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepo.save(cart)).thenReturn(cart);

        Cart result = cartService.addItem(user, 1L, 3);

        assertEquals(1, result.getItems().size());
        assertEquals(3, result.getItems().get(0).getQuantity());
        verify(cartItemRepo, times(1)).save(any(CartItem.class));
    }

    @Test
    void testAddItem_UpdateExisting() {
        cart.getItems().add(cartItem);

        when(cartRepo.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepo.save(cart)).thenReturn(cart);

        Cart result = cartService.addItem(user, 1L, 2);

        assertEquals(1, result.getItems().size());
        assertEquals(4, result.getItems().get(0).getQuantity());
        verify(cartItemRepo, times(1)).save(cartItem);
    }

    @Test
    void testAddItem_ProductNotFound() {
        when(cartRepo.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> cartService.addItem(user, 1L, 1));
    }

    @Test
    void testUpdateItem_Success() {
        cart.getItems().add(cartItem);

        when(cartRepo.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepo.findById(1L)).thenReturn(Optional.of(cartItem));
        when(cartRepo.save(cart)).thenReturn(cart);

        Cart result = cartService.updateItem(user, 1L, 10);

        assertEquals(10, result.getItems().get(0).getQuantity());
        verify(cartItemRepo, times(1)).save(cartItem);
    }

    @Test
    void testUpdateItem_ItemNotFound() {
        when(cartRepo.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> cartService.updateItem(user, 1L, 5));
    }

    @Test
    void testRemoveItem_Success() {
        cart.getItems().add(cartItem);

        when(cartRepo.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepo.findById(1L)).thenReturn(Optional.of(cartItem));

        cartService.removeItem(user, 1L);

        assertTrue(cart.getItems().isEmpty());
        verify(cartItemRepo, times(1)).delete(cartItem);
        verify(cartRepo, times(1)).save(cart);
    }

    @Test
    void testRemoveItem_NotFound() {
        when(cartRepo.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> cartService.removeItem(user, 1L));
    }

    @Test
    void testRemoveItem_NotOwned() {
        Cart anotherCart = Cart.builder().id(2L).user(user).items(new ArrayList<>()).build();
        CartItem otherItem = CartItem.builder().id(2L).cart(anotherCart).product(product).quantity(1).build();

        when(cartRepo.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepo.findById(2L)).thenReturn(Optional.of(otherItem));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> cartService.removeItem(user, 2L));
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }
}

