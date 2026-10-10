package com.gym.management.controller;

import com.gym.management.dto.*;
import com.gym.management.entity.*;
import com.gym.management.exception.ConflictException;
import com.gym.management.service.PaymentService;
import com.gym.management.service.PaymentReceiptRenderer;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/payments")
public class PaymentController {
 private final PaymentService service; private final PaymentReceiptRenderer receipts;
 public PaymentController(PaymentService service,PaymentReceiptRenderer receipts){this.service=service;this.receipts=receipts;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Payment create(@Valid @RequestBody PaymentRequest r){return service.create(r);}
 @PostMapping("/{id}/process") public Payment process(@PathVariable Long id,@Valid @RequestBody MockDecisionRequest r){return service.process(id,r.approved());}
 @PostMapping("/{id}/cancel") public Payment cancel(@PathVariable Long id){return service.cancel(id);}
 @GetMapping public List<Payment> list(@RequestParam(required=false)Long memberId,@RequestParam(required=false)PaymentStatus status,@RequestParam(required=false)PaymentMethod method,@RequestParam(required=false)@org.springframework.format.annotation.DateTimeFormat(iso=org.springframework.format.annotation.DateTimeFormat.ISO.DATE)java.time.LocalDate from,@RequestParam(required=false)@org.springframework.format.annotation.DateTimeFormat(iso=org.springframework.format.annotation.DateTimeFormat.ISO.DATE)java.time.LocalDate to,@RequestParam(required=false)String reference){return service.search(memberId,status,method,from,to,reference);}
 @GetMapping("/{id}") public Payment get(@PathVariable Long id){return service.get(id);}
 @GetMapping("/member/{memberId}") public List<Payment> member(@PathVariable Long memberId){return service.list(memberId);}
 @GetMapping("/transaction/{reference}") public Payment reference(@PathVariable String reference){return service.byReference(reference);}
 @GetMapping(value="/{id}/receipt",produces=MediaType.TEXT_HTML_VALUE) public ResponseEntity<String> receipt(@PathVariable Long id){Payment p=service.get(id);if(p.getStatus()!=PaymentStatus.SUCCESS)throw new ConflictException("Receipt is available only for successful payments");return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/html;charset=UTF-8")).body(receipts.render(p));}
}
