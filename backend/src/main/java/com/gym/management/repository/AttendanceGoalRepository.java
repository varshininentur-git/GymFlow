package com.gym.management.repository;
import com.gym.management.entity.AttendanceGoal;import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;
public interface AttendanceGoalRepository extends JpaRepository<AttendanceGoal,Long>{Optional<AttendanceGoal> findByMemberId(Long memberId);}
