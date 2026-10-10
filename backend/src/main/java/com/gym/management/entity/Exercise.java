package com.gym.management.entity;
import jakarta.persistence.*;
@Entity @Table(name="exercises",uniqueConstraints=@UniqueConstraint(name="uk_exercise_name",columnNames="name"))
public class Exercise{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)Long id;@Column(nullable=false,length=100)String name;@Column(nullable=false,length=60)String muscleGroup;@Column(nullable=false,length=60)String category;@Column(length=80)String equipment;@Column(length=1000)String instructions;@Column(nullable=false)boolean active=true;
 protected Exercise(){}public Exercise(String name,String muscle,String category,String equipment,String instructions){this.name=name;this.muscleGroup=muscle;this.category=category;this.equipment=equipment;this.instructions=instructions;}
 public Long getId(){return id;}public String getName(){return name;}public String getMuscleGroup(){return muscleGroup;}public String getCategory(){return category;}public String getEquipment(){return equipment;}public String getInstructions(){return instructions;}public boolean isActive(){return active;}
}
