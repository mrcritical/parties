package com.visualpurity.parties.cache;

import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public interface Cached {
    <Key, ReturnType> Mono<ReturnType> lookup(String cacheName,
                                              Key key,
                                              Class<ReturnType> expectedType,
                                              Supplier<Mono<ReturnType>> onCacheMiss);
}
