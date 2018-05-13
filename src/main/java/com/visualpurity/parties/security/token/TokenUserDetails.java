package com.visualpurity.parties.security.token;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class TokenUserDetails extends User {

    private static final long serialVersionUID = -8555629532146001798L;

    private final String token;

    private final String profileName;

    private final com.visualpurity.parties.datastore.model.profile.User user;

    public TokenUserDetails(String username,
                            String profileName,
                            String password,
                            String token,
                            com.visualpurity.parties.datastore.model.profile.User user,
                            boolean enabled,
                            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
        this.profileName = profileName;
        this.token = token;
        this.user = user;
    }

}
