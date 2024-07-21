package uk.co.scottishpower.smartreads.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import uk.co.scottishpower.smartreads.dto.MeterReadDTO;
import uk.co.scottishpower.smartreads.service.MeterReadsService;
import uk.co.scottishpower.smartreads.validation.annotations.NotDuplicateReading;

import java.time.LocalDate;

@RequiredArgsConstructor
public class NotDuplicateReadingValidator implements ConstraintValidator<NotDuplicateReading, Object> {

    private final MeterReadsService meterReadsService;

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        final MeterReadDTO dto = (MeterReadDTO) o;
        final LocalDate date = LocalDate.parse(dto.date());

        return !meterReadsService.existsByMeterIdAndDate(dto.meterId(), date);
    }
}
