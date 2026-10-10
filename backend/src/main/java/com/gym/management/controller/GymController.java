package com.gym.management.controller;

import com.gym.management.dto.*;
import com.gym.management.entity.*;
import com.gym.management.service.GymService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GymController {
    private final GymService service;
    public GymController(GymService service) { this.service = service; }
    @GetMapping("/members") public List<Member> members(@RequestParam(required = false) String search) { return service.members(search); }
    @GetMapping("/members/{id}") public Member member(@PathVariable Long id) { return service.member(id); }
    @PostMapping("/members") @ResponseStatus(HttpStatus.CREATED) public Member createMember(@Valid @RequestBody MemberRequest r) { return service.saveMember(null, r); }
    @PutMapping("/members/{id}") public Member updateMember(@PathVariable Long id, @Valid @RequestBody MemberRequest r) { return service.saveMember(id, r); }
    @DeleteMapping("/members/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteMember(@PathVariable Long id) { service.deleteMember(id); }
    @PostMapping("/members/{id}/fees") @ResponseStatus(HttpStatus.CREATED) public FeeChargeResponse chargeFee(@PathVariable Long id, @Valid @RequestBody FeeChargeRequest r) { return service.chargeFee(id, r); }
    @GetMapping("/plans") public List<Plan> plans() { return service.plans(); }
    @PostMapping("/plans") @ResponseStatus(HttpStatus.CREATED) public Plan createPlan(@Valid @RequestBody PlanRequest r) { return service.savePlan(null, r); }
    @PutMapping("/plans/{id}") public Plan updatePlan(@PathVariable Long id, @Valid @RequestBody PlanRequest r) { return service.savePlan(id, r); }
    @DeleteMapping("/plans/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deletePlan(@PathVariable Long id) { service.deactivatePlan(id); }
    @GetMapping("/reports/dashboard") public GymService.Dashboard dashboard() { return service.dashboard(); }
}
