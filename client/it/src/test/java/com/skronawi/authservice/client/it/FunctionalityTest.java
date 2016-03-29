package com.skronawi.authservice.client.it;

import com.skronawi.authservice.client.api.AuthServiceClient;
import com.skronawi.authservice.client.api.AuthServiceClientConfig;
import com.skronawi.authservice.client.api.Credentials;
import com.skronawi.tokenservice.jwt.api.TokenValidity;
import mockit.NonStrictExpectations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(classes = {AuthServiceTestProvisioning.class})
public class FunctionalityTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    private AuthServiceClientConfig config;

    @Test
    public void getAndValidateToken_client() throws Exception {

        String token = getAToken();

        expectValidateSelf(true);

        TokenValidity tokenValidity = authServiceClient.getAuthServiceFunctionality().validateToken(token);
        Assert.assertTrue(tokenValidity.isValid());
    }

    @Test
    public void getAndValidateToken_service() throws Exception {

        String token = getAToken();

        expectValidateSelf(false);

        TokenValidity tokenValidity = authServiceClient.getAuthServiceFunctionality().validateToken(token);
        Assert.assertTrue(tokenValidity.isValid());
    }

    private String getAToken() {
        Credentials credentials = new Credentials("foo", "bar");
        authServiceClient.getLifeCycle().init(config);
        return authServiceClient.getAuthServiceFunctionality().getToken(credentials);
    }

    private void expectValidateSelf(boolean validateSelf) {
        new NonStrictExpectations(config) {
            {
                config.validateSelf();
                result = validateSelf;
                times = 1;
            }
        };
    }
}
