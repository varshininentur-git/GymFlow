package com.gym.management.service;

import com.gym.management.dto.*;
import com.gym.management.entity.*;
import com.gym.management.exception.ConflictException;
import com.gym.management.repository.*;
import com.gym.management.security.PasswordPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.*;
import java.util.Base64;

@Service @Transactional
public class MemberActivationService {
    private final MemberRepository members;
    private final GymUserRepository users;
    private final MemberActivationRepository activations;
    private final PasswordEncoder passwords;
    private final SecureRandom random = new SecureRandom();

    public MemberActivationService(MemberRepository members, GymUserRepository users, MemberActivationRepository activations, PasswordEncoder passwords) {
        this.members=members; this.users=users; this.activations=activations; this.passwords=passwords;
    }

    public MemberActivationResponse issue(MemberActivationRequest request) {
        Member member=members.findById(request.memberId()).orElseThrow(()->new com.gym.management.exception.NotFoundException("Member not found"));
        if(users.existsByMemberId(member.getId())) throw new ConflictException("This member already has a login account");
        String username=request.username().trim();
        if(username.isBlank()||users.existsByUsernameIgnoreCase(username)) throw new ConflictException("Username is unavailable");
        if(members.existsByEmailIgnoreCase(username)||users.existsByUsernameIgnoreCase(member.getEmail())) throw new ConflictException("Username or registered email is unavailable");
        byte[] bytes=new byte[32]; random.nextBytes(bytes);
        String token=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        String hash=sha256(token); Instant expires=Instant.now().plus(Duration.ofHours(24));
        MemberActivation activation=activations.findByMemberId(member.getId()).orElse(null);
        if(activation==null) activations.save(new MemberActivation(member,hash,username,expires)); else activation.replace(hash,username,expires);
        return new MemberActivationResponse(token,username,member.getEmail(),expires);
    }

    @Transactional(readOnly=true) public java.util.List<MemberAdminOptionDto> availableMembers(){return members.findAllWithoutLoginAccount();}

    public void activate(ActivateMemberRequest request) {
        if(PasswordPolicy.exceedsBcryptLimit(request.password())) throw new IllegalArgumentException("Password must be no more than 72 UTF-8 bytes");
        MemberActivation activation=activations.lockByTokenHash(sha256(request.token())).orElseThrow(()->new IllegalArgumentException("Activation link is invalid or expired"));
        Instant now=Instant.now();
        if(activation.getUsedAt()!=null||!activation.getExpiresAt().isAfter(now)) throw new IllegalArgumentException("Activation link is invalid or expired");
        Member member=activation.getMember();
        if(users.existsByMemberId(member.getId())||users.existsByUsernameIgnoreCase(activation.getUsername())) throw new ConflictException("Member activation is no longer available");
        users.save(new GymUser(activation.getUsername(),passwords.encode(request.password()),member));
        activation.markUsed(now);
    }

    private String sha256(String value) {
        try { return java.util.HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))); }
        catch(NoSuchAlgorithmException impossible) { throw new IllegalStateException(impossible); }
    }
}
