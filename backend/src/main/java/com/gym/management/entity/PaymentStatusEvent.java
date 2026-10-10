package com.gym.management.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="payment_status_events")
public class PaymentStatusEvent {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="payment_id",nullable=false) private Payment payment;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private PaymentStatus status;
 @Column(nullable=false) private LocalDateTime changedAt;
 @Column(length=300) private String detail;
 protected PaymentStatusEvent(){}
 public PaymentStatusEvent(Payment p,PaymentStatus s,LocalDateTime at,String detail){payment=p;status=s;changedAt=at;this.detail=detail;}
}
