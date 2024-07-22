package uk.co.scottishpower.smartreads.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import uk.co.scottishpower.smartreads.validation.annotations.NotDuplicateReading;
import uk.co.scottishpower.smartreads.validation.annotations.NotLowerThanPreviousRead;
import uk.co.scottishpower.smartreads.validation.groups.FieldChecks;

@NotDuplicateReading
@NotLowerThanPreviousRead
public record MeterReadDTO(
        Long id,

        @NotNull(
                message = "Meter ID must not be null",
                groups = FieldChecks.class
        )
        Long meterId,

        @NotNull(
                message = "Meter reading must not be null",
                groups = FieldChecks.class
        )
        Long reading,

        @NotEmpty(
                message = "Date must not be null",
                groups = FieldChecks.class
        )
        String date) {
}
