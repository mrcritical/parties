package com.visualpurity.parties.api.admin;

import com.visualpurity.parties.api.admin.model.AccountUpdateResource;
import com.visualpurity.parties.api.admin.model.SignUpResource;
import com.visualpurity.parties.datastore.AccountRepository;
import com.visualpurity.parties.datastore.UserRepository;
import com.visualpurity.parties.datastore.model.Account;
import com.visualpurity.parties.datastore.model.profile.Name;
import com.visualpurity.parties.datastore.model.profile.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

@RestController
@RequestMapping("/api/admin/accounts")
@RequiredArgsConstructor
public class AccountAdminController {

    @NonNull
    private final AccountRepository accountRepository;

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final PasswordEncoder passwordEncoder;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Flux<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @PostMapping(value = "/signUp", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<Account> signUp(@RequestBody SignUpResource signUp) {
        return accountRepository
                .save(Account
                        .builder()
                        .timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()).getID())
                        .build()
                )
                .flatMap(account -> {
                    User user = User
                            .builder()
                            .accountId(account.getId())
                            .userName(signUp.getUserName())
                            .emailAddress(signUp.getEmailAddress())
                            .password(passwordEncoder.encode(signUp.getPassword()))
                            .enabled(true)
                            .name(Name
                                    .builder()
                                    .first(signUp.getName().getFirst())
                                    .last(signUp.getName().getLast())
                                    .build()
                            )
                            .role(User.Role.ADMIN)
                            .build();

                    return userRepository
                            .save(user)
                            .map(user1 -> account);
                });
    }

    @PutMapping("/{id}")
    public Mono<Account> updateAccount(@PathVariable String id, @Valid AccountUpdateResource accountUpdate) {
        return accountRepository
                .findById(id)
                .map(account -> {
                    // TODO update account
                    return account;
                })
                .flatMap(accountRepository::save);
    }

    @DeleteMapping("/{id}")
    public Mono<Account> cancelAccount(@PathVariable String id) {
        return accountRepository
                .findById(id)
                .map(account -> {
                    account.setCancelledAt(LocalDateTime.now());
                    return account;
                })
                .flatMap(accountRepository::save);
    }

}
