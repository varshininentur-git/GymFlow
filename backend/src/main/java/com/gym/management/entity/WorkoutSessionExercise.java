package com.gym.management.entity;
import jakarta.persistence.*;import java.math.BigDecimal;
@Entity @Table(name="workout_session_exercises")
public class WorkoutSessionExercise{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)Long id;@ManyToOne(fetch=FetchType.LAZY,optional=false)@JoinColumn(name="session_id",nullable=false)WorkoutSession session;@ManyToOne(fetch=FetchType.EAGER,optional=false)@JoinColumn(name="exercise_id",nullable=false)Exercise exercise;@Column(nullable=false)int completedSets;@Column(nullable=false)int repsPerSet;@Column(precision=6,scale=2)BigDecimal weightKg;
 protected WorkoutSessionExercise(){}public WorkoutSessionExercise(Exercise e,int sets,int reps,BigDecimal weight){exercise=e;completedSets=sets;repsPerSet=reps;weightKg=weight;}void setSession(WorkoutSession s){session=s;}public Long getId(){return id;}public Exercise getExercise(){return exercise;}public int getCompletedSets(){return completedSets;}public int getRepsPerSet(){return repsPerSet;}public BigDecimal getWeightKg(){return weightKg;}
}
