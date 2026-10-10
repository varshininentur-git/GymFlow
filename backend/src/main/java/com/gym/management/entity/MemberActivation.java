package com.gym.management.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "member_activations")
public class MemberActivation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "member_id", nullable = false, unique = true) private Member member;
    @Column(nullable = false, unique = true, length = 64) private String tokenHash;
    @Column(nullable = false, length = 80) private String username;
    @Column(nullable = false) private Instant expiresAt;
    private Instant usedAt;

    protected MemberActivation() {}
    public MemberActivation(Member member, String tokenHash, String username, Instant expiresAt) {
        this.member = member; this.tokenHash = tokenHash; this.username = username; this.expiresAt = expiresAt;
    }
    public Long getId() { return id; }
    public Member getMember() { return member; }
    public String getTokenHash() { return tokenHash; }
    public String getUsername() { return username; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getUsedAt() { return usedAt; }
    public void replace(String hash, String username, Instant expiry) { tokenHash=hash; this.username=username; expiresAt=expiry; usedAt=null; }
    public void markUsed(Instant at) { usedAt=at; }
}
