package com.gym.management.controller;
import com.gym.management.dto.*;
import com.gym.management.service.MemberActivationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/admin/member-activations")
public class AdminMemberActivationController {
 private final MemberActivationService activations;
 public AdminMemberActivationController(MemberActivationService activations){this.activations=activations;}
 @GetMapping("/available-members") public java.util.List<MemberAdminOptionDto> availableMembers(){return activations.availableMembers();}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public MemberActivationResponse issue(@Valid @RequestBody MemberActivationRequest request){return activations.issue(request);}
}
