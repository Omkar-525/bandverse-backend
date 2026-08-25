package com.bandverse.bandverse_backend.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Value("${jwt.private-key-path}")
    private Resource privateKeyResource;

    @Value("${jwt.public-key-path}")
    private Resource publicKeyResource;

    @Value("${jwt.issuer}")
    private String issuer;

    @Bean
    public RSAPrivateKey jwtPrivateKey() {

        try {

            String key = readKey(privateKeyResource)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded =
                    Base64.getDecoder().decode(key);

            PKCS8EncodedKeySpec keySpec =
                    new PKCS8EncodedKeySpec(decoded);

            return (RSAPrivateKey) KeyFactory
                    .getInstance("RSA")
                    .generatePrivate(keySpec);

        } catch (Exception exception) {

            throw new IllegalStateException(
                    "Failed to load JWT private key",
                    exception
            );
        }
    }

    @Bean
    public RSAPublicKey jwtPublicKey() {

        try {

            String key = readKey(publicKeyResource)
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded =
                    Base64.getDecoder().decode(key);

            X509EncodedKeySpec keySpec =
                    new X509EncodedKeySpec(decoded);

            return (RSAPublicKey) KeyFactory
                    .getInstance("RSA")
                    .generatePublic(keySpec);

        } catch (Exception exception) {

            throw new IllegalStateException(
                    "Failed to load JWT public key",
                    exception
            );
        }
    }

    @Bean
    public JwtEncoder jwtEncoder(
            RSAPublicKey jwtPublicKey,
            RSAPrivateKey jwtPrivateKey
    ) {

        return NimbusJwtEncoder
                .withKeyPair(
                        jwtPublicKey,
                        jwtPrivateKey
                )
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(
            RSAPublicKey jwtPublicKey
    ) {

        NimbusJwtDecoder decoder =
                NimbusJwtDecoder
                        .withPublicKey(jwtPublicKey)
                        .build();

        OAuth2TokenValidator<Jwt> validator =
                JwtValidators.createDefaultWithIssuer(
                        issuer
                );

        decoder.setJwtValidator(validator);

        return decoder;
    }

    private String readKey(Resource resource)
            throws IOException {

        return new String(
                resource.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );
    }
}