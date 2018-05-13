package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.profile.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface UserRepository extends ReactiveCrudRepository<User, String> {

    Flux<User> findAllByAccountId(String accountId);

    Mono<User> findByEmailAddress(String emailAddress);

}
