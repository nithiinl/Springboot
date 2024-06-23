package com.webhub.config;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

@Configuration
public class SquidexConfig {

    private final Logger logger = LoggerFactory.getLogger(SquidexConfig.class);

    @Value("${squidex.token.url}")
    private String squidexTokenUrl;

    @Value("${squidex.token.params}")
    private String squidexTokenParams;

    private SquidexTokenResponse squidexToken = null;//its gonna contain token

    private Instant refreshBeforeTime = null;//refresh token
    
    
    @Autowired
    private RestTemplate restTemplatee;

    public String getToken() { //this method 
        if (Instant.now().isAfter(this.refreshBeforeTime)) {
            this.refreshToken();
        }

        return this.squidexToken.getAccessToken();
    }

    @PostConstruct
    public void refreshToken() {
       final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        HttpEntity<String> requestEntity = new HttpEntity<String>(this.squidexTokenParams, tokenHeaders);
       
       //this.squidexToken=restTemplatee.exchange(squidexTokenUrl, HttpMethod.POST, requestEntity, SquidexTokenResponse.class).getBody();
        
        this.squidexToken = restTemplate.exchange(squidexTokenUrl, HttpMethod.POST, requestEntity, SquidexTokenResponse.class).getBody();//it will get the token from SQUIDEX and sets into squidexToken
        this.refreshBeforeTime = Instant.now().plusSeconds((long) (this.squidexToken.getExpiresIn() * 0.9)); // it will refresh the token, by checking expiration time
        logger.info("REFRESHED SQUIDEX TOKEN: {}", this.squidexToken.getAccessToken());
        logger.info("WILL REFRESH AT: {}", this.refreshBeforeTime);
    }

}
