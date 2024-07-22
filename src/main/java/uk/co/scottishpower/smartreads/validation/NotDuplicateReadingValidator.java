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
        constraintValidatorContext.disableDefaultConstraintViolation();

        final MeterReadDTO dto = (MeterReadDTO) o;

        //TODO get validation groups working so we can remove this inline check
        if (dto.date() == null) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Date must not be null")
                    .addConstraintViolation();

            return false;
        }

        final LocalDate date = LocalDate.parse(dto.date());
        final boolean readingExistsForMeterAndDate = meterReadsService.existsByMeterIdAndDate(dto.meterId(), date);

        if (readingExistsForMeterAndDate) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("A reading for this date already exists")
                    .addConstraintViolation();
        }

        return !readingExistsForMeterAndDate;
    }
}
