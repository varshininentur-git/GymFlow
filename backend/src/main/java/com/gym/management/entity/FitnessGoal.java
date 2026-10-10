package com.gym.management.entity;
import jakarta.persistence.*;import java.math.BigDecimal;import java.time.*;
@Entity @Table(name="fitness_goals",indexes=@Index(name="idx_goal_member_status",columnList="member_id,status"))
public class FitnessGoal{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false)@JoinColumn(name="member_id",nullable=false)Member member;
 @Column(nullable=false,length=80)String title;@Column(length=1000)String description;
 @Column(nullable=false,length=24)String goalType;BigDecimal targetValue;BigDecimal startingValue;@Column(length=24)String unit;LocalDate targetDate;
 @Column(nullable=false,length=16)String status="ACTIVE";LocalDateTime completedAt;
 protected FitnessGoal(){} public FitnessGoal(Member m,String title,String description,String type,BigDecimal target,BigDecimal start,String unit,LocalDate date){this.member=m;this.title=title;this.description=description;this.goalType=type;this.targetValue=target;this.startingValue=start;this.unit=unit;this.targetDate=date;}
 public Long getId(){return id;}public Member getMember(){return member;}public String getTitle(){return title;}public String getDescription(){return description;}public String getGoalType(){return goalType;}public BigDecimal getTargetValue(){return targetValue;}public BigDecimal getStartingValue(){return startingValue;}public String getUnit(){return unit;}public LocalDate getTargetDate(){return targetDate;}public String getStatus(){return status;}public LocalDateTime getCompletedAt(){return completedAt;}
 public void update(String t,String d,BigDecimal v,String u,LocalDate date){title=t;description=d;targetValue=v;unit=u;targetDate=date;}public void complete(){status="COMPLETED";completedAt=LocalDateTime.now();}public void reopen(){status="ACTIVE";completedAt=null;}
}
