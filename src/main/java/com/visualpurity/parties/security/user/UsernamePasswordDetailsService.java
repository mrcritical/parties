package com.visualpurity.parties.security.user;

import com.visualpurity.parties.datastore.model.profile.User;
import com.visualpurity.parties.security.token.TokenService;
import com.visualpurity.parties.security.token.TokenUserDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsernamePasswordDetailsService implements UserDetailsService {

    @NonNull
    private final UserService userService;

    @NonNull
    private final TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Trying to authenticate ", email);
        try {
            return getUserDetails(userService.findByEmail(email));
        } catch (UserNotFoundException ex) {
            throw new UsernameNotFoundException("Account for '" + email + "' not found", ex);
        } catch (Exception e) {
            throw e;
        }
    }

    private TokenUserDetails getUserDetails(User user) {
        return new TokenUserDetails(
                user.getEmailAddress(),
                user.getUserName(),
                user.getPassword(),
                tokenService.encode(user),
                user,
                user.isEnabled(),
                getAuthorities(user.getRoles())
        );
    }

    private List<SimpleGrantedAuthority> getAuthorities(List<User.Role> roles) {
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList());
    }

}
