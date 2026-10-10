package com.gym.management.repository;
import com.gym.management.entity.FitnessMeasurement;import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;
public interface FitnessMeasurementRepository extends JpaRepository<FitnessMeasurement,Long>{List<FitnessMeasurement> findByMemberIdOrderByMeasurementDateDescIdDesc(Long memberId);Optional<FitnessMeasurement> findByIdAndMemberId(Long id,Long memberId);}
