package uk.co.scottishpower.smartreads.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.scottishpower.smartreads.dto.GetMeterReadsDTO;
import uk.co.scottishpower.smartreads.dto.MeterReadDTO;
import uk.co.scottishpower.smartreads.dto.PostMeterReadsDTO;
import uk.co.scottishpower.smartreads.service.MeterReadsService;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MeterReadsControllerTest {

    @Mock
    private MeterReadsService meterReadsService;

    @InjectMocks
    private MeterReadsController controllerUnderTest;

    @Test
    void getMeterReads_ReturnsExpectedResults_WhenReadingsFound() {
        final long accountNumber = 1L;

        final MeterReadDTO elecReading = new MeterReadDTO(100L, 101L, 5000L, LocalDateTime.now().toString());
        final MeterReadDTO gasReading = new MeterReadDTO(200L, 201L, 6000L, LocalDateTime.now().toString());

        final GetMeterReadsDTO dto = new GetMeterReadsDTO(
                accountNumber,
                List.of(gasReading),
                List.of(elecReading)
        );

        when(meterReadsService.getMeterReadsByAccountNumber(accountNumber))
                .thenReturn(dto);

        final ResponseEntity<GetMeterReadsDTO> methodResponse = controllerUnderTest.getMeterReads(accountNumber);

        verify(meterReadsService).getMeterReadsByAccountNumber(accountNumber);
        assertThat(methodResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(methodResponse.getBody()).isEqualTo(dto);
    }

    @Test
    void postMeterRead_SavesObjects_AndReturnsOk() {
        final long accountId = 1L;
        final MeterReadDTO gasRead = new MeterReadDTO(1L, 1000L, 1000L,"2024-07-21");
        final MeterReadDTO elecRead = new MeterReadDTO(1L, 1002L, 3400L,"2024-07-20");

        final PostMeterReadsDTO dto = new PostMeterReadsDTO(accountId, List.of(gasRead), List.of(elecRead));

        final ResponseEntity<String> methodResponse = controllerUnderTest.postMeterRead(dto);

        verify(meterReadsService).saveMeterReads(dto);
        assertThat(methodResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(methodResponse.getBody()).isEqualTo("saved");
    }
}