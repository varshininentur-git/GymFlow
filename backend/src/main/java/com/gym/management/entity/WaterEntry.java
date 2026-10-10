package com.gym.management.entity;
import jakarta.persistence.*;import java.time.*;
@Entity @Table(name="water_entries",indexes=@Index(name="idx_water_member_time",columnList="member_id,occurred_at"))
public class WaterEntry{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)Long id;@ManyToOne(fetch=FetchType.LAZY,optional=false)@JoinColumn(name="member_id",nullable=false)Member member;@Column(nullable=false)LocalDateTime occurredAt;@Column(nullable=false)int amountMl;@Column(nullable=false,unique=true,length=80)String idempotencyKey;
 protected WaterEntry(){}public WaterEntry(Member m,LocalDateTime at,int ml,String key){member=m;occurredAt=at;amountMl=ml;idempotencyKey=key;}public Long getId(){return id;}public Member getMember(){return member;}public LocalDateTime getOccurredAt(){return occurredAt;}public int getAmountMl(){return amountMl;}
}
