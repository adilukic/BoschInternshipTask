package com.bosch.internship;

import com.bosch.internship.controller.dto.ProductRequest;
import com.bosch.internship.controller.impl.ProductControllerImpl;
import com.bosch.internship.entity.Product;
import com.bosch.internship.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerImplTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductControllerImpl productController;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("Gaming laptop")
                .price(1200.0)
                .build();
    }

    @Test
    void testGetProducts() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Product> page = new PageImpl<>(List.of(product), pageable, 1);

        when(productService.getAllProducts(any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<Product>> response = productController.getProducts(0, 10, "id", "asc", null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals("Laptop", response.getBody().getContent().get(0).getName());

        verify(productService, times(1)).getAllProducts(any(Pageable.class));
    }

    @Test
    void testGetProductById() {
        when(productService.getProductById(1L)).thenReturn(product);

        ResponseEntity<Product> response = productController.getProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Laptop", response.getBody().getName());

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testAddProduct() {
        ProductRequest request = new ProductRequest("Phone", "Smartphone", 800.0);
        Product saved = Product.builder()
                .id(2L)
                .name("Phone")
                .description("Smartphone")
                .price(800.0)
                .build();

        when(productService.addProduct(any(Product.class))).thenReturn(saved);

        ResponseEntity<Product> response = productController.addProduct(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Phone", response.getBody().getName());
        assertEquals(800.0, response.getBody().getPrice());

        verify(productService, times(1)).addProduct(any(Product.class));
    }
}

