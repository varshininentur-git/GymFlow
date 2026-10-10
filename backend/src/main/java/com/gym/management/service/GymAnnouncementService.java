package com.gym.management.service;
import com.gym.management.dto.MemberFitnessDtos.*;import com.gym.management.entity.GymAnnouncement;import com.gym.management.exception.NotFoundException;import com.gym.management.repository.GymAnnouncementRepository;import jakarta.validation.Valid;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import java.time.*;import java.util.*;
@Service @Transactional
public class GymAnnouncementService{
 private final GymAnnouncementRepository repository;public GymAnnouncementService(GymAnnouncementRepository repository){this.repository=repository;}
 @Transactional(readOnly=true)public List<AnnouncementDto> list(){return repository.findAll().stream().sorted(Comparator.comparing(GymAnnouncement::getStartsAt).reversed()).map(this::dto).toList();}
 public AnnouncementDto save(Long id,@Valid AnnouncementRequest r){if(r.endsAt()!=null&&r.endsAt().isBefore(r.startsAt()))throw new IllegalArgumentException("Announcement end must be after its start");GymAnnouncement a=id==null?new GymAnnouncement(r.title().trim(),r.message().trim(),r.audience(),r.startsAt(),r.endsAt()):repository.findById(id).orElseThrow(()->new NotFoundException("Announcement not found"));if(id!=null)a.update(r.title().trim(),r.message().trim(),r.audience(),r.startsAt(),r.endsAt(),r.active());return dto(repository.save(a));}
 public void deactivate(Long id){GymAnnouncement a=repository.findById(id).orElseThrow(()->new NotFoundException("Announcement not found"));a.update(a.getTitle(),a.getMessage(),a.getAudience(),a.getStartsAt(),a.getEndsAt(),false);}
 private AnnouncementDto dto(GymAnnouncement a){return new AnnouncementDto(a.getId(),a.getTitle(),a.getMessage(),a.getAudience(),a.getStartsAt(),a.getEndsAt(),a.isActive(),a.getCreatedAt());}
}
