package com.gym.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "members", indexes = @Index(name = "idx_member_name", columnList = "full_name"))
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, updatable = false) private String memberCode;
    @NotBlank @Column(name = "full_name", nullable = false) private String fullName;
    @NotBlank @Email @Column(nullable = false, unique = true) private String email;
    @NotBlank @Column(nullable = false) private String phone;
    @NotNull @Enumerated(EnumType.STRING) @Column(nullable = false) private MemberCategory category;
    private LocalDate dateOfBirth;
    private String address;
    @NotNull @Column(nullable = false) private LocalDate joiningDate;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "plan_id") private Plan plan;
    private LocalDate membershipStart;
    private LocalDate membershipEnd;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal outstandingBalance = BigDecimal.ZERO;

    public Member() {}
    @PrePersist void createCode() { if (memberCode == null) memberCode = "GYM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(); }
    public Long getId() { return id; }
    public String getMemberCode() { return memberCode; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public MemberCategory getCategory() { return category; }
    public void setCategory(MemberCategory category) { this.category = category; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }
    public Plan getPlan() { return plan; }
    public void setPlan(Plan plan) { this.plan = plan; }
    public LocalDate getMembershipStart() { return membershipStart; }
    public void setMembershipStart(LocalDate membershipStart) { this.membershipStart = membershipStart; }
    public LocalDate getMembershipEnd() { return membershipEnd; }
    public void setMembershipEnd(LocalDate membershipEnd) { this.membershipEnd = membershipEnd; }
    public BigDecimal getOutstandingBalance() { return outstandingBalance; }
    public void addOutstandingBalance(BigDecimal amount) { outstandingBalance = outstandingBalance.add(amount); }
    public void reduceOutstandingBalance(BigDecimal amount) { outstandingBalance = outstandingBalance.subtract(amount); }
    @Transient public String getMembershipStatus() { return membershipEnd == null ? "NO MEMBERSHIP" : membershipEnd.isBefore(LocalDate.now()) ? "EXPIRED" : "ACTIVE"; }
}
