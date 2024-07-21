package uk.co.scottishpower.smartreads.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uk.co.scottishpower.smartreads.dto.GetMeterReadsDTO;
import uk.co.scottishpower.smartreads.dto.MeterReadDTO;
import uk.co.scottishpower.smartreads.dto.PostMeterReadsDTO;
import uk.co.scottishpower.smartreads.enumerator.MeterReadingType;
import uk.co.scottishpower.smartreads.model.MeterRead;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MeterReadingMapper {

    public abstract MeterReadDTO meterReadToMeterReadDTO(MeterRead read);

    @Named("meterReadDTOToGasRead")
    @Mapping(target = "type", constant = "GAS")
    @Mapping(source = "date", target = "date")
    public abstract MeterRead meterReadDTOToGasRead(MeterReadDTO dto);

    @Named("meterReadDTOToGasRead")
    @Mapping(target = "type", constant = "ELECTRICITY")
    @Mapping(source = "date", target = "date")
    public abstract MeterRead meterReadDTOToElectricRead(MeterReadDTO dto);

    public GetMeterReadsDTO meterReadsToGetMeterReadDTO(long accountNumber, List<MeterRead> meterReads) {
        final List<MeterReadDTO> gasReadings = meterReads.stream()
                .filter(reading -> reading.getType().equals(MeterReadingType.GAS))
                .map(this::meterReadToMeterReadDTO)
                .toList();

        final List<MeterReadDTO> elecReadings = meterReads.stream()
                .filter(reading -> reading.getType().equals(MeterReadingType.ELECTRICITY))
                .map(this::meterReadToMeterReadDTO)
                .toList();

        return new GetMeterReadsDTO(accountNumber, gasReadings, elecReadings);
    }

    public List<MeterRead> postMeterReadsDtoToMeterReads(PostMeterReadsDTO dto) {
        final List<MeterRead> meterReads = new ArrayList<>();

        if (dto.gasReadings() != null) {
            dto.gasReadings().forEach(r -> {
                final MeterRead meterRead = meterReadDTOToGasRead(r);
                meterRead.setAccountId(dto.accountId());

                meterReads.add(meterRead);
            });
        }

        if (dto.elecReadings() != null) {
            dto.elecReadings().forEach(r -> {
                final MeterRead meterRead = meterReadDTOToElectricRead(r);
                meterRead.setAccountId(dto.accountId());

                meterReads.add(meterRead);
            });
        }

        return meterReads;
    }
}
