package com.gym.management.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="trainers", indexes=@Index(name="idx_trainer_name", columnList="name"))
public class Trainer {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @NotBlank @Column(nullable=false) private String name;
 @NotBlank @Email @Column(nullable=false, unique=true) private String email;
 @NotBlank @Column(nullable=false) private String phone;
 @NotBlank @Column(nullable=false) private String specialization;
 @NotBlank @Column(nullable=false) private String availability;
 @Column(nullable=false) private boolean active=true;
 public Trainer(){}
 public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;} public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
 public String getSpecialization(){return specialization;} public void setSpecialization(String v){specialization=v;} public String getAvailability(){return availability;} public void setAvailability(String v){availability=v;}
 public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
