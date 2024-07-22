package uk.co.scottishpower.smartreads.cucumberglue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.co.scottishpower.smartreads.CucumberUtils;
import uk.co.scottishpower.smartreads.config.propertysource.BasicAuthProperties;
import uk.co.scottishpower.smartreads.dto.MeterReadDTO;
import uk.co.scottishpower.smartreads.dto.PostMeterReadsDTO;
import uk.co.scottishpower.smartreads.web.controlleradvice.Error;
import uk.co.scottishpower.smartreads.web.controlleradvice.ErrorResponseBody;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ValidationFailureSteps {

    public static final String VALIDATION_FAILURE_MESSAGE = "Validation failure";

    private final BasicAuthProperties authProperties;

    @LocalServerPort
    private String port;

    private ErrorResponseBody validationFailureResponse;
    private HttpStatusCode errorStatus;


    @When("the client posts to endpoint {string} with no account ID")
    public void whenClientPostsWithBadAccountId(String url) {
        final PostMeterReadsDTO dto = new PostMeterReadsDTO(null, Collections.emptyList(), Collections.emptyList());

        postToEndpointWithBody(url, dto);
    }

    @When("the client posts to endpoint {string} with no meter ID")
    public void whenClientPostsWithBadMeterId(String url) {
        final MeterReadDTO meterRead = new MeterReadDTO(null, null, 1000L, "2024-07-22");
        final PostMeterReadsDTO dto = new PostMeterReadsDTO(1L, List.of(meterRead), Collections.emptyList());

        postToEndpointWithBody(url, dto);
    }

    @When("the client posts to endpoint {string} with no meter reading")
    public void whenClientPostsWithBadMeterReading(String url) {
        final MeterReadDTO meterRead = new MeterReadDTO(null, 100L, null, "2024-07-22");
        final PostMeterReadsDTO dto = new PostMeterReadsDTO(1L, List.of(meterRead), Collections.emptyList());

        postToEndpointWithBody(url, dto);
    }

    @When("the client posts to endpoint {string} with no meter reading date")
    public void whenClientPostsWithBadMeterReadingDate(String url) {
        final MeterReadDTO meterRead = new MeterReadDTO(null, 100L, 1000L, null);
        final PostMeterReadsDTO dto = new PostMeterReadsDTO(1L, List.of(meterRead), Collections.emptyList());

        postToEndpointWithBody(url, dto);
    }

    @Then("error response status code is {int}")
    public void thenValidationFailureStatusCode(int expected) {
        assertThat(errorStatus).isNotNull();
        assertThat(errorStatus.value()).isEqualTo(expected);
    }

    @Then("response body should contain the error message {string} for field {string}")
    public void thenValidationErrorResponse(String errorMessage, String fieldName) {
        assertThat(validationFailureResponse).isNotNull();

        assertThat(validationFailureResponse.getMessage()).isEqualTo(VALIDATION_FAILURE_MESSAGE);

        final Error error = validationFailureResponse.getErrors().get(0);
        assertThat(error.getFieldName()).isEqualTo(fieldName);
        assertThat(error.getErrorMessage()).isEqualTo(errorMessage);
    }

    private void postToEndpointWithBody(String url, PostMeterReadsDTO requestBody) {
        final String token = CucumberUtils.getJwtForTesting(port, authProperties.username(), authProperties.password());
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization" , "Bearer " + token);

        final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity(requestBody, headers);

        try {
            new RestTemplate().exchange("http://localhost:" + port + url,
                    HttpMethod.POST,
                    entity,
                    String.class);

        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            errorStatus = ex.getStatusCode();
            validationFailureResponse = ex.getResponseBodyAs(ErrorResponseBody.class);
        }
    }
}
