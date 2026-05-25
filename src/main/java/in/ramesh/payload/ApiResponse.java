package in.ramesh.payload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;

    private String message;

    private T data;

    private Object error;

    private String path;

    private LocalDateTime timestamp;

    private String statusCode;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .error(null)
                .statusCode("200")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, Object error, String statusCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .error(error)
                .statusCode(statusCode)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, Object error) {
        return error(message, error, "500");
    }
}
