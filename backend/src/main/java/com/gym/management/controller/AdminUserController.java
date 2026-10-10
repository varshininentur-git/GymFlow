package com.gym.management.controller;
import com.gym.management.dto.*;import com.gym.management.service.AccountService;import jakarta.validation.Valid;import org.springframework.http.HttpStatus;import org.springframework.web.bind.annotation.*;import java.util.List;import org.springframework.security.core.Authentication;
@RestController @RequestMapping("/api/admin/users")
public class AdminUserController {
 private final AccountService accounts;public AdminUserController(AccountService accounts){this.accounts=accounts;}
 @GetMapping public List<AccountResponse> list(){return accounts.list().stream().map(AccountResponse::from).toList();}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public AccountResponse create(@Valid @RequestBody AccountRequest request){return AccountResponse.from(accounts.create(request));}
 @PutMapping("/{id}/enabled") public AccountResponse setEnabled(@PathVariable Long id,@Valid @RequestBody AccountEnabledRequest request,Authentication authentication){return AccountResponse.from(accounts.setEnabled(id,request.enabled(),authentication.getName()));}
}
