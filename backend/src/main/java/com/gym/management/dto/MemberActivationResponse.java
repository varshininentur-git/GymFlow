package com.gym.management.dto;
import java.time.Instant;
public record MemberActivationResponse(String token, String username, String email, Instant expiresAt) {
 @Override public String toString(){return "MemberActivationResponse[token=[REDACTED], username="+username+", email="+email+", expiresAt="+expiresAt+"]";}
}
