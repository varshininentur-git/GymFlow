package com.gym.management.repository;
import com.gym.management.entity.GymAnnouncement;import org.springframework.data.jpa.repository.JpaRepository;import java.time.*;import java.util.*;
public interface GymAnnouncementRepository extends JpaRepository<GymAnnouncement,Long>{List<GymAnnouncement> findByActiveTrueAndStartsAtLessThanEqualAndAudienceInOrderByStartsAtDesc(LocalDateTime now,Collection<String> audience);}
