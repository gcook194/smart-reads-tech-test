package uk.co.scottishpower.smartreads.dto;

import java.util.List;

public record GetMeterReadsDTO(long accountId, List<MeterReadDTO> gasReadings, List<MeterReadDTO> elecReadings) {
}
