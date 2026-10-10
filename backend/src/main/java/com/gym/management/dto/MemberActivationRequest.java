package com.gym.management.dto;
import jakarta.validation.constraints.*;
public record MemberActivationRequest(@NotNull Long memberId, @NotBlank @Size(max=80) String username) {}
