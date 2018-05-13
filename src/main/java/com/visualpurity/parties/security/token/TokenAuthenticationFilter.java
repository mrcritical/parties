package com.visualpurity.parties.security.token;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class TokenAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return extract(request);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return extract(request);
    }

    private String extract(HttpServletRequest request) {
        return Optional
                .ofNullable(request.getHeader("Authorization"))
                .orElseGet(() -> Optional
                        .ofNullable(request.getCookies())
                        .flatMap(cookies -> Arrays.stream(cookies)
                                .filter(cookie -> "auth".equalsIgnoreCase(cookie.getName()))
                                .filter(cookie -> StringUtils.isNoneBlank(cookie.getValue()))
                                .map(Cookie::getValue)
                                .findFirst()
                        )
                        .orElse(null)
                );
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

}
