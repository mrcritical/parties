package com.visualpurity.parties.security.user;

import com.visualpurity.parties.datastore.UserRepository;
import com.visualpurity.parties.datastore.model.profile.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @NonNull
    private final UserRepository repository;

    public User findByEmail(String email) {
        return Optional
                .ofNullable(repository
                        .findByEmailAddress(email)
                        .block()
                )
                .orElseThrow(UserNotFoundException::new);
    }

}
