package uk.co.scottishpower.smartreads.cucumberglue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.co.scottishpower.smartreads.dto.MeterReadDTO;
import uk.co.scottishpower.smartreads.dto.PostMeterReadsDTO;
import uk.co.scottishpower.smartreads.web.controlleradvice.Error;
import uk.co.scottishpower.smartreads.web.controlleradvice.ErrorResponseBody;

import java.util.Collections;
import java.util.List;

public class ValidationFailureSteps {

    public static final String VALIDATION_FAILURE_MESSAGE = "Validation failure";

    @LocalServerPort
    private String port;

    private ErrorResponseBody validationFailureResponse;
    private HttpStatusCode errorStatus;


    @When("the client posts to endpoint {string} with no account ID")
    public void whenClientPostsWithBadAccountId(String url) {
        final PostMeterReadsDTO dto = new PostMeterReadsDTO(null, Collections.emptyList(), Collections.emptyList());
        final HttpEntity<PostMeterReadsDTO> request = new HttpEntity<>(dto);

        postToEndpointWithBody(url, request);
    }

    @When("the client posts to endpoint {string} with no meter ID")
    public void whenClientPostsWithBadMeterId(String url) {
        final MeterReadDTO meterRead = new MeterReadDTO(null, null, 1000L, "2024-07-22");

        final PostMeterReadsDTO dto = new PostMeterReadsDTO(1L, List.of(meterRead), Collections.emptyList());
        final HttpEntity<PostMeterReadsDTO> request = new HttpEntity<>(dto);

        postToEndpointWithBody(url, request);
    }

    @When("the client posts to endpoint {string} with no meter reading")
    public void whenClientPostsWithBadMeterReading(String url) {
        final MeterReadDTO meterRead = new MeterReadDTO(null, 100L, null, "2024-07-22");

        final PostMeterReadsDTO dto = new PostMeterReadsDTO(1L, List.of(meterRead), Collections.emptyList());
        final HttpEntity<PostMeterReadsDTO> request = new HttpEntity<>(dto);

        postToEndpointWithBody(url, request);
    }

    @When("the client posts to endpoint {string} with no meter reading date")
    public void whenClientPostsWithBadMeterReadingDate(String url) {
        final MeterReadDTO meterRead = new MeterReadDTO(null, 100L, 1000L, null);

        final PostMeterReadsDTO dto = new PostMeterReadsDTO(1L, List.of(meterRead), Collections.emptyList());
        final HttpEntity<PostMeterReadsDTO> request = new HttpEntity<>(dto);

        postToEndpointWithBody(url, request);
    }

    private void postToEndpointWithBody(String url, HttpEntity<PostMeterReadsDTO> request) {
        try {
            new RestTemplate().exchange("http://localhost:" + port + url,
                            HttpMethod.POST,
                    request,
                            String.class);
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            errorStatus = ex.getStatusCode();
            validationFailureResponse = ex.getResponseBodyAs(ErrorResponseBody.class);
        }
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
}
