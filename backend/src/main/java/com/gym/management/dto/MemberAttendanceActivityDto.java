package com.gym.management.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record MemberAttendanceActivityDto(
        LocalDate from,
        LocalDate to,
        long visitsInPeriod,
        long visitsThisMonth,
        int currentStreak,
        int longestStreak,
        List<Day> days) {
    public record Day(LocalDate date, int visitCount, List<Visit> visits) {}
    public record Visit(LocalDateTime checkInAt, LocalDateTime checkOutAt) {}
}
