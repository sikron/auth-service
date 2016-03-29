package com.skronawi.authservice.client.core;

import com.skronawi.authservice.client.api.AuthServiceClientConfig;
import com.skronawi.authservice.client.api.AuthServiceFunctionality;
import com.skronawi.authservice.client.api.Credentials;
import com.skronawi.tokenservice.jwt.api.Token;
import com.skronawi.tokenservice.jwt.api.TokenService;
import com.skronawi.tokenservice.jwt.api.TokenValidity;
import org.springframework.http.*;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

public class AuthServiceFunctionalityImpl implements AuthServiceFunctionality {

    private final AuthServiceClientConfig authServiceClientConfig;
    private final TokenService tokenService;
    private RestTemplate restTemplate;

    public AuthServiceFunctionalityImpl(AuthServiceClientConfig authServiceClientConfig, TokenService tokenService) {
        this.authServiceClientConfig = authServiceClientConfig;
        this.tokenService = tokenService;
        restTemplate = new RestTemplate();
    }

    public String getToken(Credentials credentials) {

        String authHeaderValue = basicAuthHeader(credentials.getUsername(), credentials.getPassword());
        String serviceUrl = authServiceClientConfig.getServiceUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", authHeaderValue);

        ResponseEntity<Token> responseEntity = restTemplate.exchange(serviceUrl + "/token/basic", HttpMethod.GET,
                new HttpEntity<Void>(headers), Token.class);

        return responseEntity.getBody().getToken();
    }

    public TokenValidity validateToken(String token) {
        if (authServiceClientConfig.validateSelf()) {
            return validateByClient(token);
        }
        return validateByService(token);
    }

    private TokenValidity validateByClient(String token) {
        return tokenService.validateToken(token);
    }

    private TokenValidity validateByService(String token) {

        String serviceUrl = authServiceClientConfig.getServiceUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        URI completeUri = UriComponentsBuilder.fromHttpUrl(serviceUrl + "/token/validate")
                .queryParam("token", token).build().encode().toUri();

        ResponseEntity<TokenValidity> responseEntity = restTemplate.exchange(completeUri,
                HttpMethod.GET, new HttpEntity<Void>(headers), TokenValidity.class);

        return responseEntity.getBody();
    }

    public void teardown() {
        restTemplate = null;
    }

    private String basicAuthHeader(String username, String password) {
        return "Basic " + new String(Base64Utils.encode((username + ":" + password).getBytes()));//capital "B" is needed
    }
}
