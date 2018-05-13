package com.visualpurity.parties.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public TokenLoginSuccessHandler() {
        setDefaultTargetUrl("/admin");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
            return;
        }
        TokenUserDetails principal = (TokenUserDetails) authentication.getPrincipal();
        Cookie sessionCookie = new Cookie("auth", principal.getToken());
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
