package com.bosch.internship.controller;

import com.bosch.internship.controller.dto.ProductRequest;
import com.bosch.internship.entity.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/api/products")
@Tag(name = "Product Service Api Controller")
public interface ProductController {
    @Operation(
            summary = "List all products",
            description = "Fetch a paginated, sorted and optionally filtered list of products"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class)))
    })
    @GetMapping(produces = "application/json")
    ResponseEntity<Page<Product>> getProducts(
            @Parameter(description = "Page number") @Valid @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size") @Valid @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @Valid @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @Valid @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Optional name filter") @Valid @RequestParam(required = false) String name,
            @Parameter(description = "Optional min price filter") @Valid @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Optional max price filter") @Valid @RequestParam(required = false) Double maxPrice
    );


    @Operation(summary = "Get single product", description = "Fetch product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<Product> getProductById(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id
    );


    @Operation(summary = "Add new product", description = "Create a new product (ADMIN only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductRequest.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - only ADMIN can add products")
    })
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    ResponseEntity<Product> addProduct(
            @RequestBody @Valid ProductRequest productRequest
    );
}
