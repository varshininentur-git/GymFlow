package com.gym.management.dto;
import jakarta.validation.constraints.*;
public record TrainerRequest(@NotBlank String name,@NotBlank @Email String email,@NotBlank String phone,@NotBlank String specialization,@NotBlank String availability,Boolean active){}
