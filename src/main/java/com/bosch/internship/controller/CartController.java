package com.bosch.internship.controller;

import com.bosch.internship.entity.Cart;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/cart")
@Tag(name = "CartService Api Controller")
public interface CartController {

    @Operation(summary = "Get cart contents", description = "Fetch the authenticated user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cart.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping(produces = "application/json")
    ResponseEntity<Cart> getCart();


    @Operation(summary = "Add item to cart", description = "Add a product to the user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cart.class))),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping(value = "/add", produces = "application/json")
    ResponseEntity<Cart> addToCart(
            @Parameter(description = "Product ID", required = true) @Valid @RequestParam Long productId,
            @Parameter(description = "Quantity", required = true) @Valid @RequestParam int quantity);


    @Operation(summary = "Update cart item quantity", description = "Update the quantity of a specific cart item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cart.class))),
            @ApiResponse(responseCode = "404", description = "Cart item not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - item not owned by user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping(value = "/item/{id}", produces = "application/json")
    ResponseEntity<Cart> updateCartItem(
            @Parameter(description = "Cart Item ID", required = true) @PathVariable Long id,
            @Parameter(description = "New quantity", required = true) @RequestParam int quantity
    );


    @Operation(summary = "Remove item from cart", description = "Remove a specific item from the user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item removed successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - item not owned by user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @DeleteMapping(value = "/item/{id}")
    ResponseEntity<Void> removeCartItem(
            @Parameter(description = "Cart Item ID", required = true) @PathVariable Long id);
}
