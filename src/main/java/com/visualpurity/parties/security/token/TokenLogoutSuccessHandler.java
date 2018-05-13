package com.visualpurity.parties.security.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    public TokenLogoutSuccessHandler() {
        setDefaultTargetUrl("/admin");
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Cookie sessionCookie = new Cookie("auth", "");
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(0); // Set to expire now
        response.addCookie(sessionCookie);
        super.onLogoutSuccess(request, response, authentication);
    }
}
