package com.gym.management.dto;
import jakarta.validation.constraints.NotBlank;
public record LoginRequest(String username, String usernameOrEmail, @NotBlank String password){
    public LoginRequest(String username,String password){this(username,null,password);}
    public String identifier(){return usernameOrEmail==null||usernameOrEmail.isBlank()?username:usernameOrEmail;}
    @Override public String toString(){return "LoginRequest[identifier=[REDACTED], password=[REDACTED]]";}
}
