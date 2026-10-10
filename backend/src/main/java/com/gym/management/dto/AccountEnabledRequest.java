package com.gym.management.dto;
import jakarta.validation.constraints.NotNull;
public record AccountEnabledRequest(@NotNull Boolean enabled) {}
