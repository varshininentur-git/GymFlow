package com.gym.management;
import com.gym.management.entity.*;import com.gym.management.repository.*;import org.junit.jupiter.api.Test;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;import java.math.BigDecimal;import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest class PaymentRevenueRepositoryTests {
 @Autowired PaymentRepository payments;@Autowired MemberRepository members;
 @Test void revenueAggregateIncludesSuccessAndExcludesPendingAndFailed(){Member m=new Member();m.setFullName("Report Member");m.setEmail("report@example.com");m.setPhone("555-0100");m.setCategory(MemberCategory.GIRLS);m.setJoiningDate(java.time.LocalDate.now());members.save(m);
  Payment paid=new Payment("TXN-OK","idem-ok",m,null,new BigDecimal("75.00"),PaymentMethod.CASH,PaymentPurpose.OUTSTANDING_FEE);paid.complete(PaymentStatus.SUCCESS,java.time.LocalDateTime.now(),"MOCK-OK",null);
  Payment pending=new Payment("TXN-PENDING","idem-pending",m,null,new BigDecimal("20.00"),PaymentMethod.UPI,PaymentPurpose.OUTSTANDING_FEE);
  Payment failed=new Payment("TXN-FAIL","idem-fail",m,null,new BigDecimal("30.00"),PaymentMethod.CARD,PaymentPurpose.OUTSTANDING_FEE);failed.complete(PaymentStatus.FAILED,java.time.LocalDateTime.now(),"MOCK-FAIL","Declined");payments.saveAll(java.util.List.of(paid,pending,failed));
  assertEquals(new BigDecimal("75.00"),payments.successfulTotal());
 }
}
