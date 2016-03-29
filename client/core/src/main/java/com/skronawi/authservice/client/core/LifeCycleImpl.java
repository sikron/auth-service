package com.skronawi.authservice.client.core;

import com.skronawi.authservice.client.api.AuthServiceClientConfig;
import com.skronawi.authservice.client.api.LifeCycle;

public class LifeCycleImpl implements LifeCycle {

    private final AuthServiceClientImpl authServiceClient;
    private AuthServiceClientConfig authServiceClientConfig;

    public LifeCycleImpl(AuthServiceClientImpl authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    public void init(AuthServiceClientConfig authServiceClientConfig) {
        this.authServiceClientConfig = authServiceClientConfig;
    }

    //TODO FIXME encapsulate a inner AuthServiceCommunication, so not to have to work directly on the functionality here?
    public void teardown() {
        //TODO what about teardown of tokenService?
        ((AuthServiceFunctionalityImpl)authServiceClient.getAuthServiceFunctionality()).teardown();
    }

    public AuthServiceClientConfig getAuthServiceClientConfig() {
        return authServiceClientConfig;
    }
}
