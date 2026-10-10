package com.gym.management.dto;
import jakarta.validation.constraints.NotNull;
public record CheckInRequest(@NotNull Long memberId){}
