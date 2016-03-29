package com.skronawi.authservice.client.api;

public interface LifeCycle {

    void init(AuthServiceClientConfig authServiceClientConfig);

    void teardown();
}
