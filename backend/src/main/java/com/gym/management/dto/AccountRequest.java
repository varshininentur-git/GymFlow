package com.gym.management.dto;
import com.gym.management.entity.Role;
import jakarta.validation.constraints.*;
public record AccountRequest(@NotBlank @Size(max=80) String username,@NotBlank @Size(min=12,max=100) String password,@NotNull Role role){
    @Override public String toString(){return "AccountRequest[username="+username+", password=[REDACTED], role="+role+"]";}
}
