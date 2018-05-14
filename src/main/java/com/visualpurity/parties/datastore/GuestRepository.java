package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.profile.Guest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CacheConfig(cacheNames = "guests")
public interface GuestRepository extends ReactiveCrudRepository<Guest, String> {

    Flux<Guest> findAllByAccountId(String accountId);

    @Override
    @CacheEvict
    Mono<Void> deleteById(String s);

    @Override
    @CacheEvict(key = "#entity.id")
    <S extends Guest> Mono<S> save(S entity);
}
