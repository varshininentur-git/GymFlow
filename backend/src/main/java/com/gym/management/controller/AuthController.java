package com.gym.management.controller;

import com.gym.management.dto.*;
import jakarta.servlet.http.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.*;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import com.gym.management.security.PasswordPolicy;
import com.gym.management.service.MemberActivationService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;private final SecurityContextRepository contexts;private final CsrfTokenRepository csrfTokens;private final MemberActivationService activations;
    public AuthController(AuthenticationManager authenticationManager,SecurityContextRepository contexts,CsrfTokenRepository csrfTokens,MemberActivationService activations){this.authenticationManager=authenticationManager;this.contexts=contexts;this.csrfTokens=csrfTokens;this.activations=activations;}
    @GetMapping("/csrf") public Map<String,String> csrf(HttpServletRequest request,HttpServletResponse response){
        CsrfToken token=csrfTokens.loadToken(request);
        if(token==null){token=csrfTokens.generateToken(request);csrfTokens.saveToken(token,request,response);}
        return Map.of("headerName",token.getHeaderName(),"parameterName",token.getParameterName(),"token",token.getToken());
    }
    @PostMapping("/activate-member") public ResponseEntity<Void> activateMember(@Valid @RequestBody ActivateMemberRequest request){activations.activate(request);return ResponseEntity.noContent().build();}
    @PostMapping("/login") public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request,HttpServletRequest httpRequest,HttpServletResponse httpResponse){
        if(request.identifier()==null||request.identifier().isBlank()||PasswordPolicy.exceedsBcryptLimit(request.password()))return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status",401,"message","Invalid username or password"));
        try {
            Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.identifier(),request.password()));
            HttpSession oldSession=httpRequest.getSession(false);if(oldSession!=null)oldSession.invalidate();
            SecurityContext context=SecurityContextHolder.createEmptyContext();context.setAuthentication(authentication);SecurityContextHolder.setContext(context);contexts.saveContext(context,httpRequest,httpResponse);
            csrfTokens.saveToken(null,httpRequest,httpResponse);
            return ResponseEntity.ok(sessionUser(authentication));
        } catch(AuthenticationException ex){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status",401,"message","Invalid username or password"));}
    }
    @GetMapping("/me") public SessionUser me(Authentication authentication){return sessionUser(authentication);}
    @PostMapping("/logout") public ResponseEntity<Void> logout(HttpServletRequest request,HttpServletResponse response,Authentication authentication){new SecurityContextLogoutHandler().logout(request,response,authentication);csrfTokens.saveToken(null,request,response);return ResponseEntity.noContent().build();}
    public static SessionUser sessionUser(Authentication authentication){List<String> roles=authentication.getAuthorities().stream().map(a->a.getAuthority()).filter(a->a.startsWith("ROLE_")).map(a->a.substring(5)).sorted().toList();return new SessionUser(authentication.getName(),roles);}
}
