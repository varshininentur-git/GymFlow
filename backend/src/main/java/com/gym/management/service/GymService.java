package com.gym.management.service;

import com.gym.management.dto.*;
import com.gym.management.entity.*;
import com.gym.management.exception.ConflictException;
import com.gym.management.exception.NotFoundException;
import com.gym.management.repository.*;
import com.gym.management.entity.PaymentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class GymService {
    private final MemberRepository members;
    private final PlanRepository plans;
    private final PaymentRepository payments;
    private final AttendanceRepository attendance;
    private final GymUserRepository users;
    private final MemberActivationRepository activations;
    public GymService(MemberRepository members, PlanRepository plans, PaymentRepository payments, AttendanceRepository attendance, GymUserRepository users, MemberActivationRepository activations) { this.members = members; this.plans = plans; this.payments = payments; this.attendance = attendance; this.users=users; this.activations=activations; }
    public List<Member> members(String search) { return search == null || search.isBlank() ? members.findAll() : members.findByFullNameContainingIgnoreCaseOrMemberCodeContainingIgnoreCase(search, search); }
    public Member member(Long id) { return members.findById(id).orElseThrow(() -> new NotFoundException("Member not found")); }
    public Member lockMember(Long id) { return members.lockById(id).orElseThrow(() -> new NotFoundException("Member not found")); }
    public Member saveMember(Long id, MemberRequest r) {
        Member m = id == null ? new Member() : member(id);
        if (id == null && members.existsByEmailIgnoreCase(r.email()) || id != null && members.existsByEmailIgnoreCaseAndIdNot(r.email(),id) || users.existsByUsernameIgnoreCase(r.email())) throw new ConflictException("A member with this email already exists or conflicts with an account username");
        m.setFullName(r.fullName()); m.setEmail(r.email()); m.setPhone(r.phone()); m.setCategory(r.category());
        m.setDateOfBirth(r.dateOfBirth()); m.setAddress(r.address()); m.setJoiningDate(r.joiningDate());
        // Membership is provisioned only by a successful payment. Member CRUD keeps
        // the legacy request fields for client compatibility but never applies them.
        if (id == null) {
            m.setPlan(null);
            m.setMembershipStart(null);
            m.setMembershipEnd(null);
        }
        return members.save(m);
    }
    public void deleteMember(Long id) {
        Member m = member(id);
        if (payments.existsByMemberId(id) || attendance.existsByMemberId(id) || users.existsByMemberId(id) || activations.existsByMemberId(id)) throw new ConflictException("Member has attendance, payment, or account history and cannot be deleted");
        members.delete(m);
    }
    public com.gym.management.dto.FeeChargeResponse chargeFee(Long id, com.gym.management.dto.FeeChargeRequest request) {
        Member m = member(id); m.addOutstandingBalance(request.amount()); members.save(m);
        return com.gym.management.dto.FeeChargeResponse.from(m, request.reason());
    }
    public List<Plan> plans() { return plans.findAll(); }
    public Plan plan(Long id) { return plans.findById(id).orElseThrow(() -> new NotFoundException("Plan not found")); }
    public Plan savePlan(Long id, PlanRequest r) {
        Plan p = id == null ? new Plan(r.name(), r.durationDays(), r.price(), r.description()) : plan(id);
        p.setName(r.name()); p.setDurationDays(r.durationDays()); p.setPrice(r.price()); p.setDescription(r.description());
        if (r.active() != null) p.setActive(r.active());
        return plans.save(p);
    }
    public void deactivatePlan(Long id) { plan(id).setActive(false); }
    public Dashboard dashboard() {
        List<Member> all = members.findAll();
        long active = all.stream().filter(m -> "ACTIVE".equals(m.getMembershipStatus())).count();
        return new Dashboard(all.size(), active, all.size() - active, payments.successfulTotal(), payments.countByStatus(PaymentStatus.PENDING), attendance.countByAttendanceDate(LocalDate.now()));
    }
    public record Dashboard(long totalMembers, long activeMemberships, long expiredOrUnassigned, java.math.BigDecimal successfulPayments, long pendingPayments, long attendanceToday) {}
}
