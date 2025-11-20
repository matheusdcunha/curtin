package cloud.matheus.curtin.controller;

import cloud.matheus.curtin.dto.UrlRequest;
import cloud.matheus.curtin.dto.UrlResponse;
import cloud.matheus.curtin.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping
public class UrlController {

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
    public ResponseEntity<Void> getUrl(@PathVariable String urlCode, HttpServletResponse response) throws IOException {


        UrlResponse urlResponse = this.urlService.getOriginalUrlByCode(urlCode);

        response.sendRedirect(urlResponse.originalUrl());

        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).body(null);
    }

}
