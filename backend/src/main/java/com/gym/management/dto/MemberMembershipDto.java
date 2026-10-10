package com.gym.management.dto;
import com.gym.management.entity.Member;
import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
public record MemberMembershipDto(String status,String planName,Integer planDurationDays,BigDecimal planPrice,LocalDate startDate,LocalDate expiryDate,long daysRemaining) {
 public static MemberMembershipDto from(Member m,boolean pending){LocalDate today=LocalDate.now();boolean active=m.getMembershipEnd()!=null&&!m.getMembershipEnd().isBefore(today);String status=active?"ACTIVE":pending?"PENDING":m.getMembershipEnd()!=null?"EXPIRED":"NOT SUBSCRIBED";var plan=m.getPlan();long remaining=m.getMembershipEnd()==null?0:Math.max(0,ChronoUnit.DAYS.between(today,m.getMembershipEnd()));return new MemberMembershipDto(status,plan==null?null:plan.getName(),plan==null?null:plan.getDurationDays(),plan==null?null:plan.getPrice(),m.getMembershipStart(),m.getMembershipEnd(),remaining);}
}
