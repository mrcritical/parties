package com.visualpurity.parties.security.user;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "security.password")
public class UserSecurityProperties {
    private Integer strength = 10;
}
