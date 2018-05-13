package com.visualpurity.parties.security.token;

import com.visualpurity.parties.security.token.model.TokenResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenController {

    @GetMapping(value = "/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TokenResource getToken(@AuthenticationPrincipal TokenUserDetails principal) {
        return TokenResource
                .builder()
                .token(principal.getToken())
                .profileName(principal.getProfileName())
                .build();
    }

}
