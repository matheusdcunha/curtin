package cloud.matheus.curtin.mapper;

import cloud.matheus.curtin.dto.UrlRequest;
import cloud.matheus.curtin.dto.UrlResponse;
import cloud.matheus.curtin.dto.UrlStatsResponse;
import cloud.matheus.curtin.entity.UrlEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UrlMapper {

    UrlEntity toEntity(UrlResponse urlResponse);

    UrlResponse toDto(UrlEntity urlEntity);

    @Mapping(source = "updatedAt", target = "lastAccess")
    UrlStatsResponse toStatsDto(UrlEntity urlEntity);
}
