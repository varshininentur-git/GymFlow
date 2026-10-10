package com.gym.management.controller;
import com.gym.management.service.ReportService;import org.springframework.format.annotation.DateTimeFormat;import org.springframework.web.bind.annotation.*;import java.time.*;import java.math.BigDecimal;
@RestController @RequestMapping("/api/reports")
public class ReportController {
 private final ReportService reports;public ReportController(ReportService reports){this.reports=reports;}
 @GetMapping("/payments") public ReportService.PaymentReport payments(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate from,@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate to){return reports.paymentReport(from,to);}
 @GetMapping("/payments/daily") public BigDecimal daily(@RequestParam(required=false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate date){return reports.daily(date==null?LocalDate.now():date);}
 @GetMapping("/payments/monthly") public BigDecimal monthly(@RequestParam(required=false)@DateTimeFormat(pattern="yyyy-MM")YearMonth month){return reports.monthly(month==null?YearMonth.now():month);}
 @GetMapping("/attendance/today") public long attendanceToday(){return reports.attendanceToday();}
}
