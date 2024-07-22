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

        // allows us to provide custom error messages
        constraintValidatorContext.disableDefaultConstraintViolation();

        final MeterReadDTO dto = (MeterReadDTO) o;

        // TODO get validation groups working so we can remove these inline checks
        if (dto.meterId() == null) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Meter ID must not be null")
                    .addConstraintViolation();

            return false;
        }

        if (dto.reading() == null) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Meter reading must not be null")
                    .addConstraintViolation();

            return false;
        }

        final long previousRead = meterReadsService.getMostRecentMeterReadByMeterId(dto.meterId())
                .map(MeterRead::getReading)
                .orElse(0L); // if there are no previous reads then treat previous read as zero for the sake of comparison.

        final boolean isGreaterThanPrevious = dto.reading() > previousRead;

        if (!isGreaterThanPrevious) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Reading must be higher than the previous reading")
                    .addConstraintViolation();
        }

        return isGreaterThanPrevious;
    }
}
