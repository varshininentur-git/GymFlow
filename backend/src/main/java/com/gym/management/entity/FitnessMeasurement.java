package com.gym.management.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name="fitness_measurements", indexes=@Index(name="idx_measurement_member_date", columnList="member_id,measurement_date"))
public class FitnessMeasurement {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="member_id",nullable=false) private Member member;
 @Column(name="measurement_date",nullable=false) private LocalDate measurementDate;
 @Column(precision=5,scale=2) private BigDecimal weightKg;
 @Column(precision=5,scale=2) private BigDecimal waistCm;
 @Column(precision=5,scale=2) private BigDecimal chestCm;
 @Column(precision=5,scale=2) private BigDecimal hipsCm;
 @Column(precision=5,scale=2) private BigDecimal armsCm;
 @Column(precision=5,scale=2) private BigDecimal thighsCm;
 protected FitnessMeasurement(){}
 public FitnessMeasurement(Member member,LocalDate date,BigDecimal weight,BigDecimal waist,BigDecimal chest,BigDecimal hips,BigDecimal arms,BigDecimal thighs){this.member=member;this.measurementDate=date;this.weightKg=weight;this.waistCm=waist;this.chestCm=chest;this.hipsCm=hips;this.armsCm=arms;this.thighsCm=thighs;}
 public Long getId(){return id;} public Member getMember(){return member;} public LocalDate getMeasurementDate(){return measurementDate;} public BigDecimal getWeightKg(){return weightKg;} public BigDecimal getWaistCm(){return waistCm;} public BigDecimal getChestCm(){return chestCm;} public BigDecimal getHipsCm(){return hipsCm;} public BigDecimal getArmsCm(){return armsCm;} public BigDecimal getThighsCm(){return thighsCm;}
 public void update(LocalDate date,BigDecimal weight,BigDecimal waist,BigDecimal chest,BigDecimal hips,BigDecimal arms,BigDecimal thighs){measurementDate=date;weightKg=weight;waistCm=waist;chestCm=chest;hipsCm=hips;armsCm=arms;thighsCm=thighs;}
}
