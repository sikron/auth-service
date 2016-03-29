package com.skronawi.authservice.service.test;

import com.skronawi.authservice.service.bl.UserService;
import com.skronawi.authservice.service.service.AppConfig;
import com.skronawi.authservice.service.service.EverybodyIsACrawlerUserService;
import com.skronawi.authservice.service.service.WebAppInitializer;
import com.skronawi.authservice.service.communication.WebConfig;
import com.skronawi.tokenservice.jwt.api.TokenService;
import com.skronawi.tokenservice.jwt.api.TokenServiceConfig;
import com.skronawi.tokenservice.jwt.core.TokenServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.skronawi.authservice.service",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {AppConfig.class, WebAppInitializer.class, WebConfig.class}))
public class TestWebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public UserService userService() {
        return new EverybodyIsACrawlerUserService();
    }

    @Bean
    public TokenServiceConfig tokenServiceConfig() {
        return new TokenServiceConfigTestImpl();
    }

    @Bean
    public TokenService tokenService() {
        return new TokenServiceImpl(tokenServiceConfig());
    }
}
