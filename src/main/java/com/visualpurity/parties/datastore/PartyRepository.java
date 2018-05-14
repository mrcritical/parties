package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.Party;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CacheConfig(cacheNames = "parties")
public interface PartyRepository extends ReactiveCrudRepository<Party, String> {

    Flux<Party> findAllByAccountId(String accountId);

    @Override
    @CacheEvict
    Mono<Void> deleteById(String s);

    @Override
    @CacheEvict(key = "#entity.id")
    <S extends Party> Mono<S> save(S entity);
}
