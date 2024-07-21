package uk.co.scottishpower.smartreads.web.controlleradvice;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class ErrorResponseBody {
    private String message;

    @Builder.Default
    private List<Error> errors = new ArrayList<>();
    private Object invalidData;
}
