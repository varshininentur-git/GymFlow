package com.gym.management.controller;
import com.gym.management.dto.MemberFitnessDtos.NotificationDto;import com.gym.management.service.MemberNotificationService;import org.springframework.http.HttpStatus;import org.springframework.security.core.Authentication;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/member/notifications")public class MemberNotificationController{
 private final MemberNotificationService service;public MemberNotificationController(MemberNotificationService service){this.service=service;}
 @GetMapping public List<NotificationDto> list(Authentication a){return service.list(a.getName());}
 @PostMapping("/{id}/read")public NotificationDto read(Authentication a,@PathVariable Long id){return service.markRead(a.getName(),id);}
 @PostMapping("/read-all")@ResponseStatus(HttpStatus.NO_CONTENT)public void readAll(Authentication a){service.markAllRead(a.getName());}
}
