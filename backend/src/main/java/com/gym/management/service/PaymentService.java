package com.gym.management.service;
import com.gym.management.dto.PaymentRequest;
import com.gym.management.entity.*;
import com.gym.management.exception.*;
import com.gym.management.payment.MockPaymentGateway;
import com.gym.management.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.time.*;
import java.util.*;
@Service @Transactional
public class PaymentService {
 private final PaymentRepository payments;private final PaymentStatusEventRepository events;private final GymService gym;private final MockPaymentGateway gateway;
 public PaymentService(PaymentRepository payments,PaymentStatusEventRepository events,GymService gym,MockPaymentGateway gateway){this.payments=payments;this.events=events;this.gym=gym;this.gateway=gateway;}
 public Payment create(PaymentRequest r){Optional<Payment> prior=payments.findByIdempotencyKey(r.idempotencyKey());if(prior.isPresent()){Payment p=prior.get();boolean samePlan=(p.getPlan()==null?r.planId()==null:r.planId()!=null&&p.getPlan().getId().equals(r.planId()));boolean sameAmount=r.purpose()!=PaymentPurpose.OUTSTANDING_FEE||r.amount()!=null&&p.getAmount().compareTo(r.amount())==0;if(!p.getMember().getId().equals(r.memberId())||p.getPurpose()!=r.purpose()||p.getMethod()!=r.method()||!samePlan||!sameAmount)throw new ConflictException("Idempotency key was already used for a different request");return p;}
  Member m=gym.member(r.memberId());Plan plan=null;BigDecimal amount;
  switch(r.purpose()){
   case MEMBERSHIP_PURCHASE,RENEWAL->{if(r.planId()==null)throw new IllegalArgumentException("A plan is required for membership payments");plan=gym.plan(r.planId());if(!plan.isActive())throw new ConflictException("Selected plan is inactive");amount=plan.getPrice();if(r.purpose()==PaymentPurpose.MEMBERSHIP_PURCHASE&&"ACTIVE".equals(m.getMembershipStatus()))throw new ConflictException("Member already has an active membership; use renewal");}
   case OUTSTANDING_FEE->{if(r.amount()==null||r.amount().signum()<=0)throw new IllegalArgumentException("A positive amount is required");if(r.amount().compareTo(m.getOutstandingBalance())>0)throw new IllegalArgumentException("Amount exceeds the outstanding balance");amount=r.amount();}
   default->throw new IllegalArgumentException("Unsupported payment purpose");
  }
  Payment p=payments.save(new Payment("TXN-"+UUID.randomUUID().toString().toUpperCase(),r.idempotencyKey(),m,plan,amount,r.method(),r.purpose()));events.save(new PaymentStatusEvent(p,PaymentStatus.PENDING,LocalDateTime.now(),"Payment request created"));return p;
 }
 public Payment process(Long id,boolean approved){Payment p=payments.lockById(id).orElseThrow(()->new NotFoundException("Payment not found"));if(p.getStatus()!=PaymentStatus.PENDING)throw new ConflictException("Payment has already been processed");MockPaymentGateway.Decision d=gateway.authorize(approved);LocalDateTime now=LocalDateTime.now();PaymentStatus status=d.approved()?PaymentStatus.SUCCESS:PaymentStatus.FAILED;p.complete(status,now,d.reference(),d.failureReason());events.save(new PaymentStatusEvent(p,status,now,d.approved()?"Approved by mock gateway":"Declined by mock gateway"));if(d.approved())applySuccessfulPayment(p);return p;}
 private void applySuccessfulPayment(Payment p){Member m=p.getMember();if(p.getPurpose()==PaymentPurpose.OUTSTANDING_FEE){m.reduceOutstandingBalance(p.getAmount());return;}LocalDate today=LocalDate.now();LocalDate start=today;if(p.getPurpose()==PaymentPurpose.RENEWAL&&m.getMembershipEnd()!=null&&!m.getMembershipEnd().isBefore(today))start=m.getMembershipEnd().plusDays(1);else if(p.getPurpose()==PaymentPurpose.RENEWAL&&m.getMembershipEnd()!=null)start=today;
  m.setPlan(p.getPlan());m.setMembershipStart(start);m.setMembershipEnd(start.plusDays(p.getPlan().getDurationDays()));}
 public Payment cancel(Long id){Payment p=payments.lockById(id).orElseThrow(()->new NotFoundException("Payment not found"));if(p.getStatus()!=PaymentStatus.PENDING)throw new ConflictException("Only pending payments can be cancelled");LocalDateTime now=LocalDateTime.now();p.cancel(now);events.save(new PaymentStatusEvent(p,PaymentStatus.CANCELLED,now,"Cancelled by staff"));return p;}
 @Transactional(readOnly=true) public List<Payment> list(Long memberId){if(memberId!=null){gym.member(memberId);return payments.findByMemberIdOrderByRequestedAtDesc(memberId);}return payments.findAllByOrderByRequestedAtDesc();}
 @Transactional(readOnly=true) public List<Payment> search(Long memberId,PaymentStatus status,PaymentMethod method,LocalDate from,LocalDate to,String reference){
  List<Payment> rows=list(memberId);return rows.stream().filter(p->status==null||p.getStatus()==status).filter(p->method==null||p.getMethod()==method)
   .filter(p->from==null||!p.getRequestedAt().toLocalDate().isBefore(from)).filter(p->to==null||!p.getRequestedAt().toLocalDate().isAfter(to))
   .filter(p->reference==null||reference.isBlank()||p.getTransactionReference().toLowerCase().contains(reference.toLowerCase())).toList();
 }
 @Transactional(readOnly=true) public Payment get(Long id){return payments.findById(id).orElseThrow(()->new NotFoundException("Payment not found"));}
 @Transactional(readOnly=true) public Payment byReference(String ref){return payments.findByTransactionReference(ref).orElseThrow(()->new NotFoundException("Transaction not found"));}
 @Transactional(readOnly=true) public BigDecimal revenue(){return payments.successfulTotal();}
 @Transactional(readOnly=true) public BigDecimal revenue(LocalDate from,LocalDate to){return payments.successfulBetween(from.atStartOfDay(),to.plusDays(1).atStartOfDay());}
 @Transactional(readOnly=true) public long count(PaymentStatus status){return payments.countByStatus(status);}
}
