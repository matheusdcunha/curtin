package cloud.matheus.curtin.dto;

import java.time.LocalDateTime;

public record UrlStatsResponse(
        Long id,
        String originalUrl,
        String urlCode,
        Long accessCount,
        LocalDateTime createdAt,
        LocalDateTime lastAccess
) {
}
