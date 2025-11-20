package cloud.matheus.curtin.service;

import cloud.matheus.curtin.dto.UrlRequest;
import cloud.matheus.curtin.dto.UrlResponse;
import cloud.matheus.curtin.entity.UrlEntity;
import cloud.matheus.curtin.mapper.UrlMapper;
import cloud.matheus.curtin.repository.UrlRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final UrlMapper urlMapper;

    public UrlService(UrlRepository urlRepository, UrlMapper urlMapper) {
        this.urlRepository = urlRepository;
        this.urlMapper = urlMapper;
    }

    public String generateRandomUrl(){
        return RandomStringUtils.secure().nextAlphanumeric(7);
    }


    public UrlResponse createRandomUrl(UrlRequest urlRequest){

        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setUrlCode(generateRandomUrl());
        urlEntity.setOriginalUrl(urlRequest.url());

        return urlMapper.toDto(urlRepository.save(urlEntity));
    }





}
