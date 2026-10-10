package com.gym.management.service;

import com.gym.management.dto.*;
import com.gym.management.entity.*;
import com.gym.management.exception.NotFoundException;
import com.gym.management.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service @Transactional(readOnly=true)
public class MemberPortalService {
    private final GymUserRepository users; private final AttendanceRepository attendance; private final PaymentRepository payments;
    public MemberPortalService(GymUserRepository users,AttendanceRepository attendance,PaymentRepository payments){this.users=users;this.attendance=attendance;this.payments=payments;}

    private GymUser account(String username){GymUser u=users.findByUsernameIgnoreCase(username).orElseThrow(()->new NotFoundException("Member account not found"));if(u.getRole()!=Role.MEMBER||u.getMember()==null)throw new NotFoundException("Member account not found");return u;}
    public MemberProfileDto profile(String username){GymUser u=account(username);return MemberProfileDto.from(u.getMember(),u.getUsername());}
    private boolean hasPendingMembershipPayment(Long memberId){return payments.existsPendingMembership(memberId);}
    public MemberMembershipDto membership(String username){GymUser u=account(username);return MemberMembershipDto.from(u.getMember(),hasPendingMembershipPayment(u.getMember().getId()));}
    public List<MemberAttendanceDto> attendance(String username){GymUser u=account(username);return attendance.findByMemberIdOrderByCheckInAtDesc(u.getMember().getId()).stream().map(MemberAttendanceDto::from).toList();}
    public List<MemberPaymentDto> payments(String username){GymUser u=account(username);return payments.findByMemberIdOrderByRequestedAtDesc(u.getMember().getId()).stream().map(MemberPaymentDto::from).toList();}
    public MemberDashboardDto dashboard(String username){
        GymUser u=account(username);Member m=u.getMember();Long id=m.getId();LocalDate today=LocalDate.now();LocalDate first=today.withDayOfMonth(1);LocalDate next=first.plusMonths(1);
        List<Attendance> visits=attendance.findTop10ByMemberIdOrderByCheckInAtDesc(id);List<Payment> latest=payments.findTop10ByMemberIdOrderByRequestedAtDesc(id);
        LocalDateTime recentSuccessful=payments.findFirstByMemberIdAndStatusOrderByCompletedAtDesc(id,PaymentStatus.SUCCESS).map(Payment::getCompletedAt).orElse(null);
        return new MemberDashboardDto(MemberProfileDto.from(m,u.getUsername()),MemberMembershipDto.from(m,hasPendingMembershipPayment(id)),attendance.countByMemberIdAndAttendanceDateGreaterThanEqualAndAttendanceDateLessThan(id,first,next),visits.isEmpty()?null:visits.get(0).getCheckInAt(),payments.countByMemberIdAndStatus(id,PaymentStatus.SUCCESS),payments.successfulTotalForMember(id),recentSuccessful,latest.stream().map(MemberPaymentDto::from).toList());
    }
    public List<MemberAttendanceDto> attendanceHistory(String username){return attendance(username);}
    public MemberAttendanceActivityDto attendanceActivity(String username,LocalDate from,LocalDate to){
        if(from==null||to==null||from.isAfter(to))throw new IllegalArgumentException("A valid attendance date range is required");
        LocalDate today=LocalDate.now();
        if(to.isAfter(today))throw new IllegalArgumentException("Attendance date range cannot include future dates");
        if(ChronoUnit.DAYS.between(from,to)>365)throw new IllegalArgumentException("Attendance date range cannot exceed 366 days");
        GymUser u=account(username);Long memberId=u.getMember().getId();
        List<Attendance> rows=attendance.findByMemberIdAndAttendanceDateGreaterThanEqualAndAttendanceDateLessThanEqualOrderByAttendanceDateAscCheckInAtAsc(memberId,from,to);
        Map<LocalDate,List<MemberAttendanceActivityDto.Visit>> grouped=new LinkedHashMap<>();
        for(Attendance row:rows)grouped.computeIfAbsent(row.getAttendanceDate(),d->new ArrayList<>()).add(new MemberAttendanceActivityDto.Visit(row.getCheckInAt(),row.getCheckOutAt()));
        List<MemberAttendanceActivityDto.Day> days=grouped.entrySet().stream().map(e->new MemberAttendanceActivityDto.Day(e.getKey(),e.getValue().size(),List.copyOf(e.getValue()))).toList();
        List<LocalDate> allDays=attendance.findDistinctAttendanceDateByMemberIdOrderByAttendanceDateDesc(memberId);
        LocalDate monthStart=today.withDayOfMonth(1);
        long monthly=attendance.countByMemberIdAndAttendanceDateGreaterThanEqualAndAttendanceDateLessThan(memberId,monthStart,monthStart.plusMonths(1));
        return new MemberAttendanceActivityDto(from,to,rows.size(),monthly,currentStreak(allDays,today),longestStreak(allDays),days);
    }
    private static int currentStreak(List<LocalDate> descending,LocalDate today){if(descending.isEmpty()||descending.get(0).isBefore(today.minusDays(1)))return 0;int n=1;for(int i=1;i<descending.size();i++){if(descending.get(i).equals(descending.get(i-1).minusDays(1)))n++;else break;}return n;}
    private static int longestStreak(List<LocalDate> descending){int best=0,run=0;LocalDate prior=null;for(int i=descending.size()-1;i>=0;i--){LocalDate day=descending.get(i);run=prior!=null&&day.equals(prior.plusDays(1))?run+1:1;best=Math.max(best,run);prior=day;}return best;}
    public Payment ownedPayment(String username,Long paymentId){GymUser u=account(username);return payments.findById(paymentId).filter(p->p.getMember().getId().equals(u.getMember().getId())).orElseThrow(()->new NotFoundException("Payment not found"));}
}
