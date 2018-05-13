package com.visualpurity.parties.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
@Profile("default")
@RequiredArgsConstructor
@AutoConfigureAfter(EmbeddedMongoAutoConfiguration.class)
@EnableConfigurationProperties(MongoProperties.class)
public class DefaultMongoConfig extends AbstractReactiveMongoConfiguration {

    @NonNull
    private final Environment environment;

    @NonNull
    private final MongoProperties mongoProperties;

    @Override
    protected String getDatabaseName() {
        return mongoProperties.getDatabaseName();
    }

    @Bean
    @DependsOn("embeddedMongoServer")
    @Override
    public MongoClient reactiveMongoClient() {
        int port = environment.getProperty("local.mongo.port", Integer.class);
        return MongoClients.create(String.format("mongodb://localhost:%d", port));
    }

}
