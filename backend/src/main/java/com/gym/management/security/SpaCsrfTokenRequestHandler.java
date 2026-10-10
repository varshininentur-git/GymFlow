package com.gym.management.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.*;
import org.springframework.util.StringUtils;

/** Accepts the raw XSRF-TOKEN cookie value in the X-XSRF-TOKEN header for SPA requests. */
public final class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
    private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();
    private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
    @Override public void handle(HttpServletRequest request,HttpServletResponse response,java.util.function.Supplier<CsrfToken> token) {
        xor.handle(request,response,token);
        // The CSRF bootstrap controller loads/creates/saves the token itself. Avoid generating
        // an earlier deferred token on that one request, which would emit two cookies.
        if(!"/api/auth/csrf".equals(request.getServletPath()))token.get();
    }
    @Override public String resolveCsrfTokenValue(HttpServletRequest request,CsrfToken token) {
        String header=request.getHeader(token.getHeaderName());
        return (StringUtils.hasText(header)?plain:xor).resolveCsrfTokenValue(request,token);
    }
}
