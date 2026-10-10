package com.gym.management.dto;
import jakarta.validation.constraints.NotNull;
public record MockDecisionRequest(@NotNull Boolean approved){}
