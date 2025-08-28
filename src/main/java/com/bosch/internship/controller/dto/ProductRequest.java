package com.bosch.internship.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank
    @Schema(description = "Name of the product")
    private String name;

    @NotBlank
    @Schema(description = "Description of the product")
    private String description;

    @NotNull
    @Schema(description = "Price of the product")
    private Double price;
}
