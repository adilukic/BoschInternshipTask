package com.bosch.internship.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @Schema(example = "adilukic",
            description = "Username must be unique",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(example = "Matija",
            description = "First name",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(example = "Lukic",
            description = "Last name",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(example = "Matija123",
            description = "Password must be between 8 and 24 char",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Password is required")
    @Length(min = 8, max = 24,
            message = "Password length must be between 8 and 24")
    private String password;

    @Schema(example = "069000000",
            description = "Username must be unique",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?\\d+$",
            message = "Phone should contain only digits")
    private String phoneNum;
}
