package cloud.matheus.curtin.controller;

import cloud.matheus.curtin.dto.UrlRequest;
import cloud.matheus.curtin.dto.UrlResponse;
import cloud.matheus.curtin.dto.UrlStatsResponse;
import cloud.matheus.curtin.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/shortener")
public class UrlController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlController.class);

    UrlService urlService;

    public UrlController(UrlService urlService){
        this.urlService = urlService;
    };

    @PostMapping
    public ResponseEntity<UrlResponse> createUrl(@RequestBody UrlRequest urlRequest){

        UrlResponse urlResponse = this.urlService.createUrl(urlRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(urlResponse);
    }

    @GetMapping("/{urlCode}")
    public ResponseEntity<Void> getUrl(@PathVariable String urlCode){
        LOGGER.info("UrlController - /{urlCode} request received for urlCode: {} at {}", urlCode, LocalDateTime.now());

        if ("favicon.ico".equals(urlCode)) {
            return ResponseEntity.notFound().build();
        }

        UrlResponse urlResponse = this.urlService.getOriginalUrlByCode(urlCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(urlResponse.originalUrl()));

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    @GetMapping("/{urlCode}/stats")
    public ResponseEntity<UrlStatsResponse> getUrlStats(@PathVariable String urlCode){
        LOGGER.info("UrlController - /{urlCode}/stats request received for urlCode: {} at {}", urlCode, LocalDateTime.now());

        UrlStatsResponse urlResponse = this.urlService.getUrlStatsByCode(urlCode);

        return ResponseEntity.status(HttpStatus.OK).body(urlResponse);
    }

}
