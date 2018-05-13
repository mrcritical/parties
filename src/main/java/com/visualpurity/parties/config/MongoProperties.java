package com.visualpurity.parties.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mongodb")
public class MongoProperties {
    private String databaseName = "reactive-mongo";
    private String uri;
}
