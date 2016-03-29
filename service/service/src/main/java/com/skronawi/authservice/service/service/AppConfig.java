package com.skronawi.authservice.service.service;

import com.skronawi.authservice.service.bl.UserService;
import com.skronawi.configservice.api.ConfigService;
import com.skronawi.configservice.api.DefaultConfigServiceConfiguration;
import com.skronawi.configservice.api.PropertyReadAccess;
import com.skronawi.configservice.core.ConfigServiceImpl;
import com.skronawi.tokenservice.jwt.api.TokenService;
import com.skronawi.tokenservice.jwt.api.TokenServiceConfig;
import com.skronawi.tokenservice.jwt.core.JwtKeyUtil;
import com.skronawi.tokenservice.jwt.core.TokenServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Collections;

@Configuration
//@ComponentScan("com.skronawi.authservice.service")
public class AppConfig {

    private static final com.skronawi.configservice.api.Configuration AUTH_CONFIG =
            () -> "authservice";

    @Bean
    public UserService userService() {
        //TODO FIXME use a real user-service, e.g. the one from WP
        return new EverybodyIsACrawlerUserService();
    }

    @Bean
    public TokenService tokenService() throws IOException {
        TokenServiceConfig tokenServiceConfig = new TokenServiceConfig();
        PropertyReadAccess propertyReadAccess = configService().getPropertyReadAccess();
        tokenServiceConfig.setLifeTimeMinutes(propertyReadAccess.getIntByKeyOrDefault(
                "tokenservice.jwt.key.lifetime.minutes", 30).getValue());
        tokenServiceConfig.setRsaPrivateKey(JwtKeyUtil.fromPrivateString(propertyReadAccess.getByKey(
                "tokenservice.jwt.key.private").getValue()));
        tokenServiceConfig.setRsaPublicKey(JwtKeyUtil.fromPublicString(propertyReadAccess.getByKey(
                "tokenservice.jwt.key.public").getValue()));
        return new TokenServiceImpl(tokenServiceConfig);
    }

    private ConfigService configService() throws IOException {
        ConfigServiceImpl configService = new ConfigServiceImpl(); //inmemory key-value-service, reads only from classpath
        configService.getLifeCycle().init(new DefaultConfigServiceConfiguration());
        configService.getLifeCycle().load(Collections.singleton(AUTH_CONFIG));
        return configService;
    }

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
//        InputStream inputStream = getClass().getResourceAsStream("/authservice.properties");
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
