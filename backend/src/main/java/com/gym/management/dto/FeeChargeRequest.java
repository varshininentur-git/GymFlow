package com.gym.management.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record FeeChargeRequest(@NotNull @DecimalMin("0.01") BigDecimal amount,@NotBlank @Size(max=200) String reason){}
