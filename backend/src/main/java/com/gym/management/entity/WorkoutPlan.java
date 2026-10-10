package com.gym.management.entity;
import jakarta.persistence.*;import java.util.*;
@Entity @Table(name="workout_plans",indexes=@Index(name="idx_workout_plan_member",columnList="member_id"))
public class WorkoutPlan{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)Long id;@ManyToOne(fetch=FetchType.LAZY,optional=false)@JoinColumn(name="member_id",nullable=false)Member member;@Column(nullable=false,length=100)String name;@Column(length=1000)String description;@Column(nullable=false)boolean active=true;
 @OneToMany(mappedBy="plan",cascade=CascadeType.ALL,orphanRemoval=true)@OrderBy("position ASC")List<WorkoutPlanExercise> exercises=new ArrayList<>();
 protected WorkoutPlan(){}public WorkoutPlan(Member m,String name,String description){member=m;this.name=name;this.description=description;}
 public Long getId(){return id;}public Member getMember(){return member;}public String getName(){return name;}public String getDescription(){return description;}public List<WorkoutPlanExercise> getExercises(){return exercises;}public boolean isActive(){return active;}
 public void update(String n,String d){name=n;description=d;}public void replaceExercises(List<WorkoutPlanExercise> items){exercises.clear();items.forEach(e->{e.setPlan(this);exercises.add(e);});}public void deactivate(){active=false;}
}
