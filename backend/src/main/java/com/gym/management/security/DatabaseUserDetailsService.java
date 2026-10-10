package com.gym.management.security;

import com.gym.management.repository.GymUserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    private final GymUserRepository users;
    public DatabaseUserDetailsService(GymUserRepository users) { this.users=users; }
    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String identifier=username.trim();
        var account=users.findByUsernameIgnoreCase(identifier).or(()->users.findMemberAccountByEmail(identifier)).orElseThrow(()->new UsernameNotFoundException("Account not found"));
        return User.withUsername(account.getUsername()).password(account.getPasswordHash()).roles(account.getRole().name()).disabled(!account.isEnabled()).build();
    }
}
