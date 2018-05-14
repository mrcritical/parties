package com.visualpurity.parties.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.cache.CacheMono;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class CacheManagerCached implements Cached {

    private final CacheManager cacheManager;

    @Override
    public <Key, ReturnType> Mono<ReturnType> lookup(String cacheName,
                                                     Key key,
                                                     Class<ReturnType> expectedType,
                                                     Supplier<Mono<ReturnType>> onCacheMiss) {
        Cache cache = cacheManager.getCache(cacheName);
        return CacheMono
                .lookup(
                        s -> Mono
                                .justOrEmpty(
                                        cache
                                                .get(s, expectedType)
                                )
                                .map(Signal::next)
                        , key
                )
                .onCacheMissResume(onCacheMiss)
                .andWriteWith((s, signal) -> {
                    cache.put(s, signal.get());
                    return Mono.empty();
                });
    }

}
