package cloud.matheus.curtin.dto;

import java.time.LocalDateTime;

public record UrlResponse(
        Long id,
        String originalUrl,
        String urlCode,
        Long acessCount,
        LocalDateTime createdAt
) {
}
