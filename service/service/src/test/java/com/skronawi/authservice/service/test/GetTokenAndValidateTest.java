package com.skronawi.authservice.service.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skronawi.tokenservice.jwt.api.Token;
import com.skronawi.tokenservice.jwt.api.TokenValidity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@WebAppConfiguration
@ContextConfiguration(classes = {TestWebConfig.class})
public class GetTokenAndValidateTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeClass
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void getAndValidate() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/token/basic")
                .accept(MediaType.APPLICATION_JSON);
        new BasicAuthenticator("foo", "bar").authenticate(requestBuilder);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        Token token = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Token.class);

        requestBuilder = MockMvcRequestBuilders
                .get("/token/validate").param("token", token.getToken())
                .accept(MediaType.APPLICATION_JSON);

        mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        TokenValidity tokenValidity = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                TokenValidity.class);
        Assert.assertTrue(tokenValidity.isValid());
    }
}
