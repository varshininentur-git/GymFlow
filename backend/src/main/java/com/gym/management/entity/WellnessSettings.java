package com.gym.management.entity;
import jakarta.persistence.*;import java.math.BigDecimal;
@Entity @Table(name="wellness_settings")
public class WellnessSettings{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)Long id;@OneToOne(fetch=FetchType.LAZY,optional=false)@JoinColumn(name="member_id",nullable=false,unique=true)Member member;Integer waterTargetMl;BigDecimal calorieTarget;BigDecimal proteinTargetGrams;BigDecimal carbohydrateTargetGrams;BigDecimal fatTargetGrams;
 protected WellnessSettings(){}public WellnessSettings(Member m){member=m;}public Long getId(){return id;}public Member getMember(){return member;}public Integer getWaterTargetMl(){return waterTargetMl;}public BigDecimal getCalorieTarget(){return calorieTarget;}public BigDecimal getProteinTargetGrams(){return proteinTargetGrams;}public BigDecimal getCarbohydrateTargetGrams(){return carbohydrateTargetGrams;}public BigDecimal getFatTargetGrams(){return fatTargetGrams;}public void update(Integer water,BigDecimal calories,BigDecimal protein,BigDecimal carbs,BigDecimal fat){waterTargetMl=water;calorieTarget=calories;proteinTargetGrams=protein;carbohydrateTargetGrams=carbs;fatTargetGrams=fat;}
}
