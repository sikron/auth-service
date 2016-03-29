package com.skronawi.authservice.client.api;

public interface AuthServiceClientConfig {

    String getServiceUrl();

    boolean validateSelf();
}
