package com.gym.management.controller;
import com.gym.management.dto.MemberFitnessDtos.*;import com.gym.management.service.GymAnnouncementService;import jakarta.validation.Valid;import org.springframework.http.HttpStatus;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/admin/announcements")public class AdminAnnouncementController{
 private final GymAnnouncementService service;public AdminAnnouncementController(GymAnnouncementService service){this.service=service;}
 @GetMapping public List<AnnouncementDto> list(){return service.list();}
 @PostMapping @ResponseStatus(HttpStatus.CREATED)public AnnouncementDto create(@Valid @RequestBody AnnouncementRequest r){return service.save(null,r);}
 @PutMapping("/{id}")public AnnouncementDto update(@PathVariable Long id,@Valid @RequestBody AnnouncementRequest r){return service.save(id,r);}
 @DeleteMapping("/{id}")@ResponseStatus(HttpStatus.NO_CONTENT)public void deactivate(@PathVariable Long id){service.deactivate(id);}
}
