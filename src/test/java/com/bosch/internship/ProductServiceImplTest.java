package com.bosch.internship.service.impl;

import com.bosch.internship.entity.Product;
import com.bosch.internship.repo.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
    }

    @Test
    void testGetAllProducts() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product));

        when(productRepo.findAll(pageable)).thenReturn(productPage);

        Page<Product> result = productService.getAllProducts(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepo, times(1)).findAll(pageable);
    }

    @Test
    void testGetProductByIdFound() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productRepo, times(1)).findById(1L);
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepo.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            productService.getProductById(99L);
        });

        assertEquals("404 NOT_FOUND \"Product not found\"", exception.getMessage());
        verify(productRepo, times(1)).findById(99L);
    }

    @Test
    void testAddProduct() {
        when(productRepo.save(product)).thenReturn(product);

        Product result = productService.addProduct(product);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productRepo, times(1)).save(product);
    }
}
