package com.gym.management.service;

import com.gym.management.dto.AccountRequest;
import com.gym.management.entity.GymUser;
import com.gym.management.exception.ConflictException;
import com.gym.management.repository.GymUserRepository;
import com.gym.management.repository.MemberRepository;
import com.gym.management.security.PasswordPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @Transactional
public class AccountService {
    private final GymUserRepository users; private final MemberRepository members; private final PasswordEncoder passwords;
    public AccountService(GymUserRepository users,MemberRepository members,PasswordEncoder passwords){this.users=users;this.members=members;this.passwords=passwords;}
    public GymUser create(AccountRequest request){if(request.role()==com.gym.management.entity.Role.MEMBER)throw new IllegalArgumentException("Member accounts must be activated from an existing member record");String username=request.username().trim();if(PasswordPolicy.exceedsBcryptLimit(request.password()))throw new IllegalArgumentException("Password must be no more than 72 UTF-8 bytes");if(users.existsByUsernameIgnoreCase(username)||members.existsByEmailIgnoreCase(username))throw new ConflictException("Username is already in use");return users.save(new GymUser(username,passwords.encode(request.password()),request.role()));}
    public GymUser setEnabled(Long id,boolean enabled,String currentUsername){GymUser user=users.findById(id).orElseThrow(()->new com.gym.management.exception.NotFoundException("Account not found"));if(!enabled&&user.getUsername().equalsIgnoreCase(currentUsername))throw new ConflictException("You cannot deactivate your own account");user.setEnabled(enabled);return user;}
    @Transactional(readOnly=true) public List<GymUser> list(){return users.findAll();}
}
