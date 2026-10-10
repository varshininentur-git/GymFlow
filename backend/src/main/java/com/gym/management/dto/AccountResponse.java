package com.gym.management.dto;
import com.gym.management.entity.GymUser;
import java.time.Instant;
public record AccountResponse(Long id,String username,String role,boolean enabled,Instant createdAt){public static AccountResponse from(GymUser u){return new AccountResponse(u.getId(),u.getUsername(),u.getRole().name(),u.isEnabled(),u.getCreatedAt());}}
