package com.gym.management.repository;
import com.gym.management.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface AttendanceRepository extends JpaRepository<Attendance,Long>{
 boolean existsByMemberIdAndCheckOutAtIsNull(Long memberId);
 boolean existsByMemberId(Long memberId);
 Optional<Attendance> findFirstByMemberIdAndCheckOutAtIsNullOrderByCheckInAtDesc(Long memberId);
 List<Attendance> findByMemberIdOrderByCheckInAtDesc(Long memberId);
 List<Attendance> findByMemberIdAndAttendanceDateGreaterThanEqualAndAttendanceDateLessThanEqualOrderByAttendanceDateAscCheckInAtAsc(Long memberId,LocalDate from,LocalDate to);
 @Query("select distinct a.attendanceDate from Attendance a where a.member.id = :memberId order by a.attendanceDate desc")
 List<LocalDate> findDistinctAttendanceDateByMemberIdOrderByAttendanceDateDesc(@Param("memberId") Long memberId);
 List<Attendance> findTop10ByMemberIdOrderByCheckInAtDesc(Long memberId);
 long countByMemberIdAndAttendanceDateGreaterThanEqualAndAttendanceDateLessThan(Long memberId,LocalDate from,LocalDate to);
 List<Attendance> findByAttendanceDateOrderByCheckInAtDesc(LocalDate day);
 List<Attendance> findAllByOrderByCheckInAtDesc();
 long countByAttendanceDate(LocalDate day);
}
