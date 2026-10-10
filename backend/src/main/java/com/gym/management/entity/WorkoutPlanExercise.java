package com.gym.management.entity;
import jakarta.persistence.*;
@Entity @Table(name="workout_plan_exercises",uniqueConstraints=@UniqueConstraint(name="uk_plan_exercise_position",columnNames={"plan_id","position"}))
public class WorkoutPlanExercise{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)Long id;@ManyToOne(fetch=FetchType.LAZY,optional=false)@JoinColumn(name="plan_id",nullable=false)WorkoutPlan plan;@ManyToOne(fetch=FetchType.EAGER,optional=false)@JoinColumn(name="exercise_id",nullable=false)Exercise exercise;@Column(nullable=false)int position;@Column(nullable=false)int targetSets;@Column(nullable=false)int targetReps;@Column(nullable=false)int restSeconds;
 protected WorkoutPlanExercise(){}public WorkoutPlanExercise(Exercise e,int position,int sets,int reps,int rest){exercise=e;this.position=position;targetSets=sets;targetReps=reps;restSeconds=rest;}void setPlan(WorkoutPlan p){plan=p;}public Long getId(){return id;}public Exercise getExercise(){return exercise;}public int getPosition(){return position;}public int getTargetSets(){return targetSets;}public int getTargetReps(){return targetReps;}public int getRestSeconds(){return restSeconds;}
}
