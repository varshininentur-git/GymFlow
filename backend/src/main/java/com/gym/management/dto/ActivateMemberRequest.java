package com.gym.management.dto;
import jakarta.validation.constraints.*;
public record ActivateMemberRequest(@NotBlank String token, @NotBlank @Size(min=12, max=72) String password) {
    @Override public String toString() { return "ActivateMemberRequest[token=[REDACTED], password=[REDACTED]]"; }
}
