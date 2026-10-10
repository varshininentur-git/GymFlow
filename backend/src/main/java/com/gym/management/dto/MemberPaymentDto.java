package com.gym.management.dto;
import com.gym.management.entity.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public record MemberPaymentDto(Long id,String transactionReference,String purpose,BigDecimal amount,String method,String status,LocalDateTime requestedAt,LocalDateTime completedAt) { public static MemberPaymentDto from(Payment p){return new MemberPaymentDto(p.getId(),p.getTransactionReference(),p.getPurpose().name(),p.getAmount(),p.getMethod().name(),p.getStatus().name(),p.getRequestedAt(),p.getCompletedAt());} }
