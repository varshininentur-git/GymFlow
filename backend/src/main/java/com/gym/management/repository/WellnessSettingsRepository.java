package com.gym.management.repository;
import com.gym.management.entity.WellnessSettings;import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;
public interface WellnessSettingsRepository extends JpaRepository<WellnessSettings,Long>{Optional<WellnessSettings> findByMemberId(Long memberId);}
