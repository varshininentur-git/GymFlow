package com.gym.management.dto;
import com.gym.management.entity.MemberCategory;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record MemberRequest(@NotBlank String fullName, @NotBlank @Email String email, @NotBlank String phone,
    @NotNull MemberCategory category, LocalDate dateOfBirth, String address, @NotNull LocalDate joiningDate,
    Long planId, LocalDate membershipStart, LocalDate membershipEnd) {}
