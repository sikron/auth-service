package com.skronawi.authservice.client.service;

import com.skronawi.authservice.client.api.AuthServiceClient;

public interface AuthServiceClientProvider {

    void init();

    void teardown();

    AuthServiceClient get();
}
