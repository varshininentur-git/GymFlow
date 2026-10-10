package com.gym.management.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "gym_users")
public class GymUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 80) private String username;
    @Column(nullable = false, length = 100) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Role role;
    @Column(nullable = false) private boolean enabled = true;
    @Column(nullable = false, updatable = false) private Instant createdAt = Instant.now();
    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id", unique = true) private Member member;

    protected GymUser() {}
    public GymUser(String username, String passwordHash, Role role) { this.username=username; this.passwordHash=passwordHash; this.role=role; }
    public GymUser(String username, String passwordHash, Member member) { this.username=username; this.passwordHash=passwordHash; this.role=Role.MEMBER; this.member=member; }
    public Long getId(){return id;} public String getUsername(){return username;} public String getPasswordHash(){return passwordHash;}
    public Role getRole(){return role;} public boolean isEnabled(){return enabled;} public Instant getCreatedAt(){return createdAt;} public Member getMember(){return member;}
    public void setRole(Role role){this.role=role;} public void setEnabled(boolean enabled){this.enabled=enabled;}
}
