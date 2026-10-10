package com.gym.management.repository;
import com.gym.management.entity.WaterEntry;import org.springframework.data.jpa.repository.JpaRepository;import java.time.*;import java.util.*;
public interface WaterEntryRepository extends JpaRepository<WaterEntry,Long>{List<WaterEntry> findByMemberIdAndOccurredAtGreaterThanEqualAndOccurredAtLessThanOrderByOccurredAtDesc(Long memberId,LocalDateTime from,LocalDateTime to);Optional<WaterEntry> findByIdAndMemberId(Long id,Long memberId);boolean existsByIdempotencyKey(String idempotencyKey);}
