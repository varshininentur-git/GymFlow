package com.gym.management.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record PlanRequest(@NotBlank String name, @NotNull @Min(1) @Max(365) Integer durationDays,
    @NotNull @DecimalMin("0.01") BigDecimal price, String description, Boolean active) {}
