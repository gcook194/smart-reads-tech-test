package uk.co.scottishpower.smartreads.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.co.scottishpower.smartreads.dto.GetMeterReadsDTO;
import uk.co.scottishpower.smartreads.dto.PostMeterReadsDTO;
import uk.co.scottishpower.smartreads.mapper.MeterReadingMapper;
import uk.co.scottishpower.smartreads.model.MeterRead;
import uk.co.scottishpower.smartreads.repository.MeterReadsRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeterReadsService {
    private final MeterReadingMapper meterReadingMapper;
    private final MeterReadsRepository meterReadsRepository;

    public GetMeterReadsDTO getMeterReadsByAccountNumber(final long accountNumber) {
        final List<MeterRead> readings = meterReadsRepository.findByAccountId(accountNumber);

        return meterReadingMapper.meterReadsToGetMeterReadDTO(accountNumber, readings);
    }

    public void saveMeterReads(final PostMeterReadsDTO meterReadsToSave) {
        final List<MeterRead> meterReads = meterReadingMapper.postMeterReadsDtoToMeterReads(meterReadsToSave);

        meterReadsRepository.saveAll(meterReads);
    }

    public Optional<MeterRead> getMostRecentMeterReadByMeterId(final long meterId) {
        return meterReadsRepository.findFirstByMeterIdOrderByDateDesc(meterId);
    }

    public boolean existsByMeterIdAndDate(final Long meterId, final LocalDate date) {
        return meterReadsRepository.existsByMeterIdAndDate(meterId, date);
    }
}















