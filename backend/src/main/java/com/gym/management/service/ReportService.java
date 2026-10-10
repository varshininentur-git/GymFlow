package com.gym.management.service;
import com.gym.management.entity.*;import com.gym.management.repository.PaymentRepository;import org.springframework.stereotype.Service;
import java.math.BigDecimal;import java.time.*;import java.util.*;
@Service public class ReportService {
 private final PaymentRepository payments;private final AttendanceService attendance;private final PaymentService paymentService;
 public ReportService(PaymentRepository p,AttendanceService a,PaymentService s){payments=p;attendance=a;paymentService=s;}
 public PaymentReport paymentReport(LocalDate from,LocalDate to){return new PaymentReport(paymentService.revenue(),paymentService.revenue(from,to),paymentService.count(PaymentStatus.PENDING),paymentService.count(PaymentStatus.FAILED),payments.findAllByOrderByRequestedAtDesc().stream().limit(10).toList());}
 public BigDecimal daily(LocalDate day){return paymentService.revenue(day,day);}
 public BigDecimal monthly(YearMonth month){return paymentService.revenue(month.atDay(1),month.atEndOfMonth());}
 public long attendanceToday(){return attendance.countToday();}
 public record PaymentReport(BigDecimal totalSuccessfulCollections,BigDecimal collectionsInRange,long pendingPayments,long failedPayments,List<Payment> recentTransactions){}
}
