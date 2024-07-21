package uk.co.scottishpower.smartreads.repository;

import org.springframework.data.repository.CrudRepository;
import uk.co.scottishpower.smartreads.model.MeterRead;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MeterReadsRepository extends CrudRepository<MeterRead, Long> {
    List<MeterRead> findByAccountId(long accountId);

    Optional<MeterRead> findFirstByMeterIdOrderByDateDesc(long accountId);

    boolean existsByMeterIdAndDate(Long meterId, LocalDate date);
}
