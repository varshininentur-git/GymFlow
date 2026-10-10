package com.gym.management.service;
import com.gym.management.entity.*;
import com.gym.management.exception.*;
import com.gym.management.repository.AttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.List;
@Service @Transactional
public class AttendanceService {
 private final AttendanceRepository attendance;private final GymService gym;
 public AttendanceService(AttendanceRepository attendance,GymService gym){this.attendance=attendance;this.gym=gym;}
 public Attendance checkIn(Long memberId){Member m=gym.lockMember(memberId);if(!"ACTIVE".equals(m.getMembershipStatus()))throw new ConflictException("An active membership is required to check in");
 if(attendance.existsByMemberIdAndCheckOutAtIsNull(memberId))throw new ConflictException("Member already has an active check-in");return attendance.save(new Attendance(m,LocalDateTime.now()));}
 public Attendance checkOut(Long memberId){Attendance a=attendance.findFirstByMemberIdAndCheckOutAtIsNullOrderByCheckInAtDesc(memberId).orElseThrow(()->new ConflictException("No active check-in found for this member"));a.setCheckOutAt(LocalDateTime.now());return a;}
 @Transactional(readOnly=true) public List<Attendance> list(Long memberId,LocalDate date){if(memberId!=null){gym.member(memberId);return attendance.findByMemberIdOrderByCheckInAtDesc(memberId).stream().filter(a->date==null||a.getAttendanceDate().equals(date)).toList();}return date==null?attendance.findAllByOrderByCheckInAtDesc():attendance.findByAttendanceDateOrderByCheckInAtDesc(date);}
 @Transactional(readOnly=true) public long countToday(){return attendance.countByAttendanceDate(LocalDate.now());}
}
