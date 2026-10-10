package com.gym.management.controller;
import com.gym.management.dto.CheckInRequest;
import com.gym.management.entity.Attendance;
import com.gym.management.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
@RestController @RequestMapping("/api/attendance")
public class AttendanceController {
 private final AttendanceService service;public AttendanceController(AttendanceService service){this.service=service;}
 @PostMapping("/check-in") @ResponseStatus(HttpStatus.CREATED) public Attendance checkIn(@Valid @RequestBody CheckInRequest r){return service.checkIn(r.memberId());}
 @PostMapping("/check-out") public Attendance checkOut(@Valid @RequestBody CheckInRequest r){return service.checkOut(r.memberId());}
 @GetMapping public List<Attendance> list(@RequestParam(required=false)Long memberId,@RequestParam(required=false)LocalDate date){return service.list(memberId,date);}
 @GetMapping("/member/{memberId}") public List<Attendance> member(@PathVariable Long memberId){return service.list(memberId,null);}
}
