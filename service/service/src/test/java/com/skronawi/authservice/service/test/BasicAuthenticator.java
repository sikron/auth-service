package com.skronawi.authservice.service.test;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.Base64Utils;

public class BasicAuthenticator {

    private final String username;
    private final String password;

    public BasicAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void authenticate(MockHttpServletRequestBuilder builder) {
        String authValue = basicAuthHeader(username, password);
        builder.header("authorization", authValue);
    }

    private String basicAuthHeader(String username, String password) {
        return "Basic " + new String(Base64Utils.encode((username + ":" + password).getBytes()));//capital "B" is needed
    }
}
