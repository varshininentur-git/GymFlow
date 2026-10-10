package com.gym.management.dto;
import com.gym.management.entity.Attendance;
import java.time.*;
public record MemberAttendanceDto(LocalDate attendanceDate,LocalDateTime checkInAt,LocalDateTime checkOutAt) { public static MemberAttendanceDto from(Attendance a){return new MemberAttendanceDto(a.getAttendanceDate(),a.getCheckInAt(),a.getCheckOutAt());} }
