package cloud.matheus.curtin.service;

import cloud.matheus.curtin.dto.UrlRequest;
import cloud.matheus.curtin.dto.UrlResponse;
import cloud.matheus.curtin.dto.UrlStatsResponse;
import cloud.matheus.curtin.entity.UrlEntity;
import cloud.matheus.curtin.exception.UrlNotFoundException;
import cloud.matheus.curtin.mapper.UrlMapper;
import cloud.matheus.curtin.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlService.class);

    private final UrlRepository urlRepository;
    private final UrlMapper urlMapper;

    private String generateRandomUrl(){
        return RandomStringUtils.secure().nextAlphanumeric(7);
    }

    public UrlResponse createUrl(UrlRequest urlRequest){

        LOGGER.info("UrlService - Creating Url");

        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setUrlCode(generateRandomUrl());
        urlEntity.setOriginalUrl(urlRequest.url());

        UrlEntity result = urlRepository.save(urlEntity);

        LOGGER.info("UrlService - Url Created");

        return urlMapper.toDto(result);
    }

    public UrlResponse getOriginalUrlByCode(String code){

        LOGGER.info("UrlService - Getting Original Url");
        UrlEntity url  = urlRepository.findByUrlCode(code).orElseThrow(()-> new UrlNotFoundException("No url found with code " + code));

        LOGGER.info("UrlService - Url Found");

        urlRepository.incrementarAcesso(url.getId(), LocalDateTime.now());

        LOGGER.info("UrlService - Url Updated");

        return urlMapper.toDto(url);
    }

    public UrlStatsResponse getUrlStatsByCode(String code){
        UrlEntity url = urlRepository.findByUrlCode(code).orElseThrow(()-> new UrlNotFoundException("No url found with code " + code));

        LOGGER.info("UrlService - UrlStats Found");

        return urlMapper.toStatsDto(url);
    }

}
