package in.ramesh.payload;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private String message;
    private int status;
    private LocalDateTime timestamp;
    private Map<String, String> errors; // field -> error message
}