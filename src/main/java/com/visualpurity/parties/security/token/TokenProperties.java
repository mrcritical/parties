package com.visualpurity.parties.security.token;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "security.token")
public class TokenProperties {
    private long maxAgeSeconds;
    private String secret;
}
