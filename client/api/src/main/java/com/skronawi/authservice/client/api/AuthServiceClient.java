package com.skronawi.authservice.client.api;

public interface AuthServiceClient {

    LifeCycle getLifeCycle();

    AuthServiceFunctionality getAuthServiceFunctionality();
}
