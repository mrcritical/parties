package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.profile.User;
import org.reactivestreams.Publisher;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@CacheConfig(cacheNames = "users")
public interface UserRepository extends ReactiveCrudRepository<User, String> {

    Flux<User> findAllByAccountId(String accountId);

    Mono<User> findByEmailAddress(String emailAddress);

    @Override
    @CacheEvict
    Mono<Void> deleteById(String id);

    @Override
    @CacheEvict(key = "#entity.id")
    <S extends User> Mono<S> save(S entity);
}
