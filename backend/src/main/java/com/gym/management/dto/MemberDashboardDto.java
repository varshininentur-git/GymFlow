package com.gym.management.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
public record MemberDashboardDto(MemberProfileDto profile,MemberMembershipDto membership,long attendanceThisMonth,LocalDateTime mostRecentCheckIn,long successfulPaymentCount,BigDecimal successfulPaymentTotal,LocalDateTime mostRecentPayment,List<MemberPaymentDto> recentPayments) {}
