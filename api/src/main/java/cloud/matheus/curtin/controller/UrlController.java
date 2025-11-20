package cloud.matheus.curtin.controller;

import cloud.matheus.curtin.dto.UrlRequest;
import cloud.matheus.curtin.dto.UrlResponse;
import cloud.matheus.curtin.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UrlController {

    UrlService urlService;

    public UrlController(UrlService urlService){
        this.urlService = urlService;
    };

    @PostMapping
    public ResponseEntity<UrlResponse> createUrl(@RequestBody UrlRequest urlRequest){

        UrlResponse urlResponse = this.urlService.createRandomUrl(urlRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(urlResponse);
    }

}
