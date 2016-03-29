package com.skronawi.authservice.service.communication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan("com.skronawi.authservice.service.communication")
public class WebConfig extends WebMvcConfigurerAdapter {
}
