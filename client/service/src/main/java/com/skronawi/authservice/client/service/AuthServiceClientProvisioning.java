package com.skronawi.authservice.client.service;

import com.skronawi.authservice.client.api.AuthServiceClient;
import com.skronawi.authservice.client.core.AuthServiceClientImpl;
import com.skronawi.configservice.api.ConfigService;
import com.skronawi.configservice.api.PropertyReadAccess;
import com.skronawi.configservice.core.ConfigServiceImpl;
import com.skronawi.tokenservice.jwt.api.TokenService;
import com.skronawi.tokenservice.jwt.api.TokenServiceConfig;
import com.skronawi.tokenservice.jwt.core.JwtKeyUtil;
import com.skronawi.tokenservice.jwt.core.TokenServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Collections;

@Configuration
public class AuthServiceClientProvisioning {

    private static final com.skronawi.configservice.api.Configuration AUTH_CONFIG =
            () -> "authserviceclient";

    @Bean
    public AuthServiceClient authServiceClient() throws IOException {
        return new AuthServiceClientImpl(tokenService());
    }

    public TokenService tokenService() throws IOException {
        TokenServiceConfig tokenServiceConfig = new TokenServiceConfig();
        PropertyReadAccess propertyReadAccess = configService().getPropertyReadAccess();
        tokenServiceConfig.setRsaPublicKey(JwtKeyUtil.fromPublicString(propertyReadAccess.getByKey(
                "tokenservice.jwt.key.public").getValue()));
        return new TokenServiceImpl(tokenServiceConfig);
    }

    private ConfigService configService() throws IOException {
        ConfigServiceImpl configService = new ConfigServiceImpl();
        configService.getLifeCycle().load(Collections.singleton(AUTH_CONFIG));
        return configService;
    }

//    private ConfigService configService() throws IOException {
//        ConfigServiceImpl configService = new ConfigServiceImpl(keyValueService());
//        configService.getLifeCycle().init(configSourceConfiguration());
//        configService.getLifeCycle().load(Collections.singleton(AUTH_CONFIG));
//        return configService;
//    }
//
//    public KeyValueService keyValueService() throws IOException {
//        KeyValueServiceImpl keyValueService = new KeyValueServiceImpl();
//        keyValueService.getLifeCycle().init(keyValueServiceConfiguration());
//        return keyValueService;
//    }
//
//    private ConfigServiceConfiguration configSourceConfiguration() throws IOException {
//        return ConfigServiceConfiguration.Builder.from(getAuthserviceProperties());
//    }
//
//    private KeyValueServiceConfiguration keyValueServiceConfiguration() throws IOException {
//        return KeyValueServiceConfiguration.Builder.from(getAuthserviceProperties());
//    }
//
//    private Map<String, String> getAuthserviceProperties() throws IOException {
//        InputStream inputStream = getClass().getResourceAsStream("/authserviceclient.properties");
//        Properties properties = new Properties();
//        properties.load(inputStream);
//
//        Map<String, String> keyValues = new HashMap<>();
//        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
//            keyValues.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
//        }
//        return keyValues;
//    }
}
