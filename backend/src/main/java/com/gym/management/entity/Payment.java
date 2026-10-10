package com.gym.management.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity @Table(name="payments",indexes={@Index(name="idx_payment_status",columnList="status"),@Index(name="idx_payment_requested",columnList="requested_at")})
public class Payment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,updatable=false) private String transactionReference;
 @Column(nullable=false,unique=true,updatable=false) private String idempotencyKey;
 @ManyToOne(fetch=FetchType.EAGER,optional=false) @JoinColumn(name="member_id",nullable=false) private Member member;
 @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="plan_id") private Plan plan;
 @Column(nullable=false,precision=10,scale=2) private BigDecimal amount;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private PaymentMethod method;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private PaymentPurpose purpose;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private PaymentStatus status=PaymentStatus.PENDING;
 @Column(name="requested_at",nullable=false) private LocalDateTime requestedAt=LocalDateTime.now();
 private LocalDateTime completedAt;
 private String gatewayReference;
 @Column(length=300) private String failureReason;
 protected Payment(){}
 public Payment(String ref,String idem,Member m,Plan p,BigDecimal amount,PaymentMethod method,PaymentPurpose purpose){transactionReference=ref;idempotencyKey=idem;member=m;plan=p;this.amount=amount;this.method=method;this.purpose=purpose;}
 public Long getId(){return id;} public String getTransactionReference(){return transactionReference;} public String getIdempotencyKey(){return idempotencyKey;} public Member getMember(){return member;} public Plan getPlan(){return plan;} public BigDecimal getAmount(){return amount;} public PaymentMethod getMethod(){return method;} public PaymentPurpose getPurpose(){return purpose;} public PaymentStatus getStatus(){return status;} public LocalDateTime getRequestedAt(){return requestedAt;} public LocalDateTime getCompletedAt(){return completedAt;} public String getGatewayReference(){return gatewayReference;} public String getFailureReason(){return failureReason;}
 public void complete(PaymentStatus status,LocalDateTime at,String gatewayRef,String reason){if(this.status!=PaymentStatus.PENDING)throw new IllegalStateException("Only pending payments can be processed");if(status!=PaymentStatus.SUCCESS&&status!=PaymentStatus.FAILED)throw new IllegalArgumentException("Gateway decision must be SUCCESS or FAILED");this.status=status;completedAt=at;gatewayReference=gatewayRef;failureReason=reason;}
 public void cancel(LocalDateTime at){if(status!=PaymentStatus.PENDING)throw new IllegalStateException("Only pending payments can be cancelled");status=PaymentStatus.CANCELLED;completedAt=at;}
}
