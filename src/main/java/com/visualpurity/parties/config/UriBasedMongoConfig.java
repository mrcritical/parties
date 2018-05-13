package com.visualpurity.parties.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
@Profile({"local", "prod"})
@RequiredArgsConstructor
@EnableConfigurationProperties(MongoProperties.class)
public class UriBasedMongoConfig extends AbstractReactiveMongoConfiguration {

    @NonNull
    private final MongoProperties mongoProperties;

    @Override
    protected String getDatabaseName() {
        return mongoProperties.getDatabaseName();
    }

    @Bean
    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(mongoProperties.getUri());
    }

}
