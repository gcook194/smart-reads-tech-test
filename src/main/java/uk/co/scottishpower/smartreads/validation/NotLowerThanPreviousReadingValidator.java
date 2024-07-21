package uk.co.scottishpower.smartreads.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import uk.co.scottishpower.smartreads.dto.MeterReadDTO;
import uk.co.scottishpower.smartreads.model.MeterRead;
import uk.co.scottishpower.smartreads.service.MeterReadsService;
import uk.co.scottishpower.smartreads.validation.annotations.NotLowerThanPreviousRead;

@RequiredArgsConstructor
public class NotLowerThanPreviousReadingValidator implements ConstraintValidator<NotLowerThanPreviousRead, Object> {

    private final MeterReadsService meterReadsService;

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        final MeterReadDTO dto = (MeterReadDTO) o;

        final long previousRead = meterReadsService.getMostRecentMeterReadByMeterId(dto.meterId())
                .map(MeterRead::getReading)
                .orElse(0L); // if there are no previous reads then treat previous read as zero for the sake of comparison.

        return dto.reading() > previousRead;
    }
}
