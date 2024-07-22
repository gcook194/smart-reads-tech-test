package uk.co.scottishpower.smartreads.cucumberglue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.co.scottishpower.smartreads.dto.GetMeterReadsDTO;
import uk.co.scottishpower.smartreads.dto.MeterReadDTO;
import uk.co.scottishpower.smartreads.dto.PostMeterReadsDTO;

import java.util.List;

public class SaveMeterReadingsSteps {

    @LocalServerPort
    private String port;
    private ResponseEntity<String> postResponse;
    private ResponseEntity<GetMeterReadsDTO> getResponse;

    private void postToEndpointWithBody(String url, HttpEntity<PostMeterReadsDTO> request) {
        try {
            postResponse = new RestTemplate().exchange("http://localhost:" + port + url,
                    HttpMethod.POST,
                    request,
                    String.class);
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
        }
    }

    @When("the client posts to endpoint {string} with a valid post body")
    public void whenClientPostsWithBadAccountId(String url) {
        final MeterReadDTO gasRead = new MeterReadDTO(null, 1234L, 1000L, "2024-07-22");
        final MeterReadDTO elecRead = new MeterReadDTO(null, 7765L, 1000L, "2024-07-22");
        final PostMeterReadsDTO dto = new PostMeterReadsDTO(1L, List.of(gasRead), List.of(elecRead));
        final HttpEntity<PostMeterReadsDTO> request = new HttpEntity<>(dto);

        postToEndpointWithBody(url, request);
    }

    @Then("post response status code is {int} with body {string}")
    public void thenPostResponseCode(int expectedCode, String expectedBody) {
        assertThat(postResponse).isNotNull();
        assertThat(postResponse.getStatusCode().value()).isEqualTo(expectedCode);
        assertThat(postResponse.getBody()).isEqualTo(expectedBody);
    }
}
