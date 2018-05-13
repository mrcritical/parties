package com.visualpurity.parties.security.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.visualpurity.parties.datastore.model.profile.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@EnableConfigurationProperties(TokenProperties.class)
public class TokenService {

    private final TokenProperties properties;

    private final String issuer;

    private final Algorithm algorithm;

    private final JWTVerifier verifier;

    @Autowired
    public TokenService(TokenProperties properties, @Value("${spring.application.name}") String issuer) throws UnsupportedEncodingException {
        this.properties = properties;
        this.issuer = issuer;
        this.algorithm = Algorithm.HMAC256(properties.getSecret());
        this.verifier = JWT.require(algorithm).acceptExpiresAt(0).build();
    }

    DecodedJWT decode(String token) {
        return this.verifier.verify(token);
    }

    public String encode(User user) {
        LocalDateTime now = LocalDateTime.now();
        try {
            return JWT
                    .create()
                    .withIssuer(issuer)
                    .withSubject(user.getEmailAddress())
                    .withIssuedAt(Date
                            .from(now.atZone(ZoneId.systemDefault())
                                    .toInstant())
                    )
                    .withExpiresAt(Date
                            .from(now.plusSeconds(properties.getMaxAgeSeconds())
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant())
                    )
                    .withArrayClaim("role", user
                            .getRoles()
                            .stream()
                            .map(User.Role::toString)
                            .toArray(String[]::new)
                    )
                    .withClaim("usr", user.getUserName())
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            log.error("Cannot properly create token", ex);
            throw new TokenCreationException("Cannot properly create token", ex);
        }
    }

}
