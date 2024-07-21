package uk.co.scottishpower.smartreads.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PostMeterReadsDTO(
        @NotNull(message = "Account ID must not be null")
        Long accountId,

        @Valid List<MeterReadDTO> gasReadings,
        @Valid List<MeterReadDTO> elecReadings) {
}
