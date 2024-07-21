package uk.co.scottishpower.smartreads.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.scottishpower.smartreads.dto.GetMeterReadsDTO;
import uk.co.scottishpower.smartreads.dto.MeterReadDTO;
import uk.co.scottishpower.smartreads.dto.PostMeterReadsDTO;
import uk.co.scottishpower.smartreads.enumerator.MeterReadingType;
import uk.co.scottishpower.smartreads.mapper.MeterReadingMapper;
import uk.co.scottishpower.smartreads.model.MeterRead;
import uk.co.scottishpower.smartreads.repository.MeterReadsRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MeterReadsServiceTest {

    @Mock
    private MeterReadingMapper meterReadingMapper;

    @Mock
    private MeterReadsRepository meterReadsRepository;

    @InjectMocks
    private MeterReadsService serviceUnderTest;

    @Test
    void getMeterReadsByAccountNumber_ReturnsExpectedMeterReads() {
        final long accountId = 100L;
        final MeterRead meterReading = MeterRead.builder()
                .id(1L)
                .accountId(accountId)
                .meterId(158L)
                .reading(55000)
                .date(LocalDate.now())
                .type(MeterReadingType.GAS)
                .build();
        final List<MeterRead> meterReadings = List.of(meterReading);

        final MeterReadDTO gasReading = new MeterReadDTO(
                meterReading.getId(),
                meterReading.getMeterId(),
                meterReading.getReading(),
                meterReading.getDate().toString()
        );

        final GetMeterReadsDTO dto = new GetMeterReadsDTO(
                accountId,
                List.of(gasReading),
                null
        );

        when(meterReadsRepository.findByAccountId(accountId))
                .thenReturn(meterReadings);

        when(meterReadingMapper.meterReadsToGetMeterReadDTO(accountId, meterReadings))
                .thenReturn(dto);

        final ArgumentCaptor<List<MeterRead>> captor = ArgumentCaptor.forClass(List.class);
        final GetMeterReadsDTO methodResponse = serviceUnderTest.getMeterReadsByAccountNumber(accountId);

        verify(meterReadsRepository).findByAccountId(accountId);
        verify(meterReadingMapper).meterReadsToGetMeterReadDTO(eq(accountId), captor.capture());

        assertThat(captor.getValue()).isEqualTo(meterReadings);
        assertThat(methodResponse).isEqualTo(dto);
    }

    @Test
    void postMeterReadsDtoToMeterReads_MapsDTO_AndSavesNewObjects() {
        final long accountId = 1L;

        final MeterReadDTO gasReadDto = new MeterReadDTO(1L, 1000L, 1000L,"2024-07-21");
        final MeterReadDTO elecReadDto = new MeterReadDTO(1L, 1002L, 3400L,"2024-07-20");
        final PostMeterReadsDTO dto = new PostMeterReadsDTO(accountId, List.of(gasReadDto), List.of(elecReadDto));

        final MeterRead gasRead = MeterRead.builder()
                .id(gasReadDto.id())
                .reading(gasReadDto.reading())
                .date(LocalDate.parse(gasReadDto.date()))
                .meterId(gasReadDto.meterId())
                .build();

        final MeterRead elecRead = MeterRead.builder()
                .id(elecReadDto.id())
                .reading(elecReadDto.reading())
                .date(LocalDate.parse(elecReadDto.date()))
                .meterId(elecReadDto.meterId())
                .build();
        final List<MeterRead> readings = List.of(gasRead, elecRead);

        when(meterReadingMapper.postMeterReadsDtoToMeterReads(dto))
                .thenReturn(readings);

        serviceUnderTest.saveMeterReads(dto);

        verify(meterReadingMapper).postMeterReadsDtoToMeterReads(dto);
        verify(meterReadsRepository).saveAll(readings);
    }

    @Test
    void getMostRecentMeterReadByMeterId_ReturnsExpectedMeterRead() {
        final long meterId = 100L;
        final Optional<MeterRead> meterRead = Optional.of(
                MeterRead.builder()
                        .id(200L)
                        .build()
        );

        when(meterReadsRepository.findFirstByMeterIdOrderByDateDesc(meterId))
                .thenReturn(meterRead);

        final Optional<MeterRead> methodResponse = serviceUnderTest.getMostRecentMeterReadByMeterId(meterId);

        verify(meterReadsRepository).findFirstByMeterIdOrderByDateDesc(meterId);
        assertThat(methodResponse).isEqualTo(meterRead);
    }

    @Test
    void getMostRecentMeterReadByMeterId_ReturnsOptionalEmpty_IfNotFound() {
        final long meterId = 100L;

        when(meterReadsRepository.findFirstByMeterIdOrderByDateDesc(meterId))
                .thenReturn(Optional.empty());

        final Optional<MeterRead> methodResponse = serviceUnderTest.getMostRecentMeterReadByMeterId(meterId);

        verify(meterReadsRepository).findFirstByMeterIdOrderByDateDesc(meterId);
        assertThat(methodResponse).isEqualTo(Optional.empty());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void existsByMeterIdAndDate_ReturnsExpectedResult(boolean expectedResponse) {
        final long meterId = 100L;
        final LocalDate date = LocalDate.now();

        when(meterReadsRepository.existsByMeterIdAndDate(meterId, date))
                .thenReturn(expectedResponse);

        final boolean methodResponse = serviceUnderTest.existsByMeterIdAndDate(meterId, date);

        assertThat(methodResponse).isEqualTo(expectedResponse);
    }
}