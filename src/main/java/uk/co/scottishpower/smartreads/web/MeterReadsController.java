package uk.co.scottishpower.smartreads.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.scottishpower.smartreads.dto.GetMeterReadsDTO;
import uk.co.scottishpower.smartreads.dto.PostMeterReadsDTO;
import uk.co.scottishpower.smartreads.service.MeterReadsService;

@RequiredArgsConstructor
@RequestMapping("reads")
@RestController
public class MeterReadsController {
    private final MeterReadsService meterReadsService;

    @GetMapping("{accountNumber}")
    public ResponseEntity<GetMeterReadsDTO> getMeterReads(@PathVariable final long accountNumber) {
        return ResponseEntity.ok(meterReadsService.getMeterReadsByAccountNumber(accountNumber));
    }

    @PostMapping
    public ResponseEntity<String> postMeterRead(@Valid @RequestBody final PostMeterReadsDTO meterReads) {
        meterReadsService.saveMeterReads(meterReads);
        return ResponseEntity.ok("saved");
    }
}



















