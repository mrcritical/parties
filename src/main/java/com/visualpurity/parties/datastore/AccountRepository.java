package com.visualpurity.parties.datastore;

import com.visualpurity.parties.datastore.model.Account;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

@CacheConfig(cacheNames = "accounts")
public interface AccountRepository extends ReactiveCrudRepository<Account, String> {

    @Override
    @CacheEvict
    Mono<Void> deleteById(String id);

    @Override
    @CacheEvict(key = "#entity.id")
    <S extends Account> Mono<S> save(S entity);

}
