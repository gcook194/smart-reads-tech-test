package uk.co.scottishpower.smartreads.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.scottishpower.smartreads.service.TokenService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;

    @PostMapping("/token")
    public String getToken(Authentication authentication) {
        log.debug("Token requested for user: '{}'", authentication.getName());
        final String token = tokenService.generateToken(authentication);
        log.debug("Token granted: {}", token);

        return token;
    }

}
