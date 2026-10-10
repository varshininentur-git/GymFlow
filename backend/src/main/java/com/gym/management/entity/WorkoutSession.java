package com.gym.management.entity;
import jakarta.persistence.*;import java.time.*;import java.util.*;
@Entity @Table(name="workout_sessions",uniqueConstraints=@UniqueConstraint(name="uk_workout_session_idempotency",columnNames="idempotency_key"),indexes=@Index(name="idx_session_member_completed",columnList="member_id,completed_at"))
public class WorkoutSession{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)Long id;@ManyToOne(fetch=FetchType.LAZY,optional=false)@JoinColumn(name="member_id",nullable=false)Member member;@ManyToOne(fetch=FetchType.LAZY)@JoinColumn(name="plan_id")WorkoutPlan plan;@Column(length=100)String workoutName;@Column(nullable=false)LocalDateTime startedAt;LocalDateTime completedAt;Integer durationMinutes;@Column(name="idempotency_key",nullable=false,unique=true,length=80)String idempotencyKey;
 @OneToMany(mappedBy="session",cascade=CascadeType.ALL,orphanRemoval=true)List<WorkoutSessionExercise> exercises=new ArrayList<>();
 protected WorkoutSession(){}public WorkoutSession(Member m,WorkoutPlan p,String name,LocalDateTime started,String key){member=m;plan=p;workoutName=name;startedAt=started;idempotencyKey=key;}
 public Long getId(){return id;}public Member getMember(){return member;}public WorkoutPlan getPlan(){return plan;}public String getWorkoutName(){return workoutName;}public LocalDateTime getStartedAt(){return startedAt;}public LocalDateTime getCompletedAt(){return completedAt;}public Integer getDurationMinutes(){return durationMinutes;}public List<WorkoutSessionExercise> getExercises(){return exercises;}
 public void finish(LocalDateTime completed,Integer duration,List<WorkoutSessionExercise> list){completedAt=completed;durationMinutes=duration;exercises.clear();list.forEach(e->{e.setSession(this);exercises.add(e);});}
}
