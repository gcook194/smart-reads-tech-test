package uk.co.scottishpower.smartreads;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class CucumberUtils {

    public static final String getJwtForTesting(String port, String username, String password) {
        final RestTemplate rt = new RestTemplateBuilder()
                .basicAuthentication(username, password)
                .build();

        final ResponseEntity<String> response = rt.exchange("http://localhost:" + port + "/api/smart/token",
                        HttpMethod.POST,
                        null,
                        String.class);

        return response.getBody();
    }
}
