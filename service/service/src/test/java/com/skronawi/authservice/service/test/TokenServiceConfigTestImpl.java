package com.skronawi.authservice.service.test;

import com.skronawi.tokenservice.jwt.api.TokenServiceConfig;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class TokenServiceConfigTestImpl extends TokenServiceConfig {

    private final RsaJsonWebKey rsaJsonWebKey;

    public TokenServiceConfigTestImpl() {
        try {
            rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        } catch (JoseException e) {
            throw new IllegalStateException("problem with RsaJwkGenerator", e);
        }
    }

    public long getLifeTimeMinutes() {
        return Duration.of(1, ChronoUnit.MINUTES).toMinutes();
    }

    public RSAPrivateKey getRsaPrivateKey() {
        return rsaJsonWebKey.getRsaPrivateKey();
    }

    public RSAPublicKey getRsaPublicKey() {
        return rsaJsonWebKey.getRsaPublicKey();
    }
}
