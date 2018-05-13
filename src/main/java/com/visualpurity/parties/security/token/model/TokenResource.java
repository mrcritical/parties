package com.visualpurity.parties.security.token.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class TokenResource implements Serializable {
    private static final long serialVersionUID = 5649248633876800768L;
    private String token;
    private String profileName;
}
