package com.gym.management.entity;
import jakarta.persistence.*;
@Entity @Table(name="attendance_goals",uniqueConstraints=@UniqueConstraint(name="uk_attendance_goal_member_period",columnNames={"member_id","period"}))
public class AttendanceGoal{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)Long id;@OneToOne(fetch=FetchType.LAZY,optional=false)@JoinColumn(name="member_id",nullable=false,unique=true)Member member;@Column(nullable=false,length=12)String period="WEEKLY";@Column(nullable=false)int targetVisits;
 protected AttendanceGoal(){}public AttendanceGoal(Member m,String p,int target){member=m;period=p;targetVisits=target;}public Long getId(){return id;}public Member getMember(){return member;}public String getPeriod(){return period;}public int getTargetVisits(){return targetVisits;}public void update(String p,int t){period=p;targetVisits=t;}
}
