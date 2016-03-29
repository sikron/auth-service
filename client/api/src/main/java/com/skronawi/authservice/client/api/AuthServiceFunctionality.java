package com.skronawi.authservice.client.api;

import com.skronawi.tokenservice.jwt.api.TokenValidity;

public interface AuthServiceFunctionality {

    String getToken(Credentials credentials);

    TokenValidity validateToken(String token);
}
