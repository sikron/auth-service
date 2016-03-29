package com.skronawi.authservice.client.service;

import com.skronawi.authservice.client.api.AuthServiceClient;
import com.skronawi.configservice.api.ConfigService;
import com.skronawi.keyvalueservice.api.AlreadyInitializedException;
import com.skronawi.keyvalueservice.api.KeyValueService;
import com.skronawi.keyvalueservice.api.NotInitializedException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AuthServiceClientProviderImpl implements AuthServiceClientProvider {

    private AnnotationConfigApplicationContext annotationConfigApplicationContext;

    @Override
    public void init() throws AlreadyInitializedException{
        annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(AuthServiceClientProvisioning.class);
    }

    @Override
    public void teardown() {
        //TODO
//        annotationConfigApplicationContext.getBean(ConfigService.class).getLifeCycle().teardown();
//        annotationConfigApplicationContext.getBean(KeyValueService.class).getLifeCycle().teardown();
    }

    @Override
    public AuthServiceClient get() throws NotInitializedException{
        return annotationConfigApplicationContext.getBean(AuthServiceClient.class);
    }
}
