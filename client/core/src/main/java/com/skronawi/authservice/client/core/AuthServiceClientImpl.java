package com.skronawi.authservice.client.core;

import com.skronawi.authservice.client.api.AuthServiceClient;
import com.skronawi.authservice.client.api.AuthServiceFunctionality;
import com.skronawi.authservice.client.api.LifeCycle;
import com.skronawi.tokenservice.jwt.api.TokenService;

public class AuthServiceClientImpl implements AuthServiceClient {

    private TokenService tokenService;
    private LifeCycleImpl lifeCycle;
    private AuthServiceFunctionalityImpl authServiceCommunication;

    public AuthServiceClientImpl(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public LifeCycle getLifeCycle() {
        lifeCycle = new LifeCycleImpl(this);
        return lifeCycle;
    }

    public synchronized AuthServiceFunctionality getAuthServiceFunctionality() {
        if (this.authServiceCommunication == null) {
            authServiceCommunication = new AuthServiceFunctionalityImpl(lifeCycle.getAuthServiceClientConfig(),
                    tokenService);
        }
        return authServiceCommunication;
    }
}
