package cloud.matheus.curtin.service;

import cloud.matheus.curtin.dto.UrlRequest;
import cloud.matheus.curtin.dto.UrlResponse;
import cloud.matheus.curtin.entity.UrlEntity;
import cloud.matheus.curtin.exception.UrlNotFoundException;
import cloud.matheus.curtin.mapper.UrlMapper;
import cloud.matheus.curtin.repository.UrlRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final UrlMapper urlMapper;

    public UrlService(UrlRepository urlRepository, UrlMapper urlMapper) {
        this.urlRepository = urlRepository;
        this.urlMapper = urlMapper;
    }

    private String generateRandomUrl(){
        return RandomStringUtils.secure().nextAlphanumeric(7);
    }

    public UrlResponse createUrl(UrlRequest urlRequest){

        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setUrlCode(generateRandomUrl());
        urlEntity.setOriginalUrl(urlRequest.url());

        return urlMapper.toDto(urlRepository.save(urlEntity));
    }

    public UrlResponse getOriginalUrlByCode(String code){
        UrlEntity url  = urlRepository.findByUrlCode(code).orElseThrow(()-> new UrlNotFoundException("No url found with code " + code));

        urlRepository.incrementarAcesso(url.getId(), LocalDateTime.now());

        return urlMapper.toDto(url);
    }






}
