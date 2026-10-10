package com.gym.management.controller;

import com.gym.management.dto.*;
import com.gym.management.entity.Payment;
import com.gym.management.entity.PaymentStatus;
import com.gym.management.exception.ConflictException;
import com.gym.management.service.MemberPortalService;
import com.gym.management.service.PaymentReceiptRenderer;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/member")
public class MemberPortalController {
    private final MemberPortalService portal; private final PaymentReceiptRenderer receipts;
    public MemberPortalController(MemberPortalService portal,PaymentReceiptRenderer receipts){this.portal=portal;this.receipts=receipts;}
    @GetMapping("/dashboard") public MemberDashboardDto dashboard(Authentication auth){return portal.dashboard(auth.getName());}
    @GetMapping("/profile") public MemberProfileDto profile(Authentication auth){return portal.profile(auth.getName());}
    @GetMapping("/membership") public MemberMembershipDto membership(Authentication auth){return portal.membership(auth.getName());}
    @GetMapping("/attendance") public java.util.List<MemberAttendanceDto> attendance(Authentication auth){return portal.attendanceHistory(auth.getName());}
    @GetMapping("/attendance/activity") public MemberAttendanceActivityDto attendanceActivity(Authentication auth,@RequestParam @org.springframework.format.annotation.DateTimeFormat(iso=org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate from,@RequestParam @org.springframework.format.annotation.DateTimeFormat(iso=org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate to){return portal.attendanceActivity(auth.getName(),from,to);}
    @GetMapping("/payments") public java.util.List<MemberPaymentDto> payments(Authentication auth){return portal.payments(auth.getName());}
    @GetMapping(value="/payments/{id}/receipt",produces=MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> receipt(@PathVariable Long id,Authentication auth){Payment payment=portal.ownedPayment(auth.getName(),id);if(payment.getStatus()!=PaymentStatus.SUCCESS)throw new ConflictException("Receipt is available only for successful payments");return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/html;charset=UTF-8")).body(receipts.render(payment));}
}
