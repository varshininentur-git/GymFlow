package com.gym.management.dto;
import com.gym.management.entity.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record PaymentRequest(@NotNull Long memberId,Long planId,@NotNull PaymentMethod method,@NotNull PaymentPurpose purpose,
 @NotBlank @Size(max=100) String idempotencyKey,@DecimalMin("0.01") BigDecimal amount){}
