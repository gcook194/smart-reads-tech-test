package uk.co.scottishpower.smartreads.cucumberglue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.co.scottishpower.smartreads.CucumberUtils;
import uk.co.scottishpower.smartreads.config.propertysource.BasicAuthProperties;
import uk.co.scottishpower.smartreads.dto.GetMeterReadsDTO;

import java.util.Collections;

@RequiredArgsConstructor
public class EmptyAccountSteps {

    private final BasicAuthProperties authProperties;

    @LocalServerPort
    private String port;

    private ResponseEntity<GetMeterReadsDTO> getResponse;

    @When("the client calls endpoint {string}")
    public void whenClientCalls(String url) {
        final String token = CucumberUtils.getJwtForTesting(port, authProperties.username(), authProperties.password());
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization" , "Bearer " + token);

        final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(headers);

        try {
            getResponse = new RestTemplate()
                    .exchange("http://localhost:" + port + url,
                            HttpMethod.GET,
                            entity,
                            GetMeterReadsDTO.class);

        } catch (HttpClientErrorException httpClientErrorException) {
            httpClientErrorException.printStackTrace();
        }
    }

    @Then("response status code is {int}")
    public void thenStatusCodee(int expected) {
        assertThat(getResponse).isNotNull();
        assertThat(getResponse.getStatusCode().value()).isEqualTo(expected);
    }

    @Then("response body should contain an empty response for account {long}")
    public void thenEmptyResponseBody(long accountId) {
        final GetMeterReadsDTO expectedResponse = new GetMeterReadsDTO(accountId, Collections.emptyList(), Collections.emptyList());

        assertThat(getResponse).isNotNull();

        final GetMeterReadsDTO responseBody = getResponse.getBody();

        assertThat(responseBody).isEqualTo(expectedResponse);
    }
}
