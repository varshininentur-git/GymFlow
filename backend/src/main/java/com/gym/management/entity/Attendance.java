package com.gym.management.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity @Table(name="attendance",indexes={@Index(name="idx_attendance_date",columnList="attendance_date"),@Index(name="idx_attendance_member",columnList="member_id")})
public class Attendance {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.EAGER,optional=false) @JoinColumn(name="member_id",nullable=false) private Member member;
 @Column(name="attendance_date",nullable=false) private LocalDate attendanceDate;
 @Column(nullable=false) private LocalDateTime checkInAt;
 private LocalDateTime checkOutAt;
 public Attendance(){}
 public Attendance(Member m,LocalDateTime at){member=m;checkInAt=at;attendanceDate=at.toLocalDate();}
 public Long getId(){return id;} public Member getMember(){return member;} public LocalDate getAttendanceDate(){return attendanceDate;} public LocalDateTime getCheckInAt(){return checkInAt;} public LocalDateTime getCheckOutAt(){return checkOutAt;} public void setCheckOutAt(LocalDateTime v){checkOutAt=v;}
}
