package com.gym.management.security;

import com.gym.management.entity.*;
import com.gym.management.repository.GymUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrap {
    @Bean ApplicationRunner initializeLocalAdmin(GymUserRepository users,PasswordEncoder passwords,
        @Value("${GYM_ADMIN_USERNAME:}") String username,@Value("${GYM_ADMIN_PASSWORD:}") String password){
        return args->{
            if(username.isBlank()&&password.isBlank())return;
            if(username.isBlank()||password.isBlank())throw new IllegalStateException("Set both GYM_ADMIN_USERNAME and GYM_ADMIN_PASSWORD to initialize the local administrator");
            if(password.length()<12)throw new IllegalStateException("GYM_ADMIN_PASSWORD must contain at least 12 characters");
            if(PasswordPolicy.exceedsBcryptLimit(password))throw new IllegalStateException("GYM_ADMIN_PASSWORD must contain no more than 72 UTF-8 bytes");
            if(!users.existsByUsernameIgnoreCase(username.trim()))users.save(new GymUser(username.trim(),passwords.encode(password),Role.ADMIN));
        };
    }
}
