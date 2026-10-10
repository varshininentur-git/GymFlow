package com.gym.management.repository;
import com.gym.management.entity.MealEntry;import org.springframework.data.jpa.repository.JpaRepository;import java.time.*;import java.util.*;
public interface MealEntryRepository extends JpaRepository<MealEntry,Long>{List<MealEntry> findByMemberIdAndOccurredAtGreaterThanEqualAndOccurredAtLessThanOrderByOccurredAtDesc(Long memberId,LocalDateTime from,LocalDateTime to);Optional<MealEntry> findByIdAndMemberId(Long id,Long memberId);}
