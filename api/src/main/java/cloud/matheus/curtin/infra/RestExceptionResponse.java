package cloud.matheus.curtin.infra;

import java.time.LocalDateTime;

public record RestExceptionResponse(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        String path
) {
    public RestExceptionResponse(Integer status, String error, String message, String path) {

        this(LocalDateTime.now(), status, error, message, path);
    }
}
