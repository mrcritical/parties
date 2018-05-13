package com.visualpurity.parties.security.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -3338984367893086936L;

    private final UserDetails principal;

    @Getter
    private final String token;

    public JWTAuthenticationToken(String token, UserDetails principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

}
