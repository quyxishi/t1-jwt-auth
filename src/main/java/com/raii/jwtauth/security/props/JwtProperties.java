package com.raii.jwtauth.security.props;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private Long expiry;
    private Long refreshExpiry;

    private Resource privateKeyRes;
    private Resource publicKeyRes;

    public ECPrivateKey privateKey() throws IOException {
        try (InputStreamReader reader = new InputStreamReader(privateKeyRes.getInputStream())) {
            final var pemParser = new PEMParser(reader);
            final var converter = new JcaPEMKeyConverter();

            final var keyPair = (PEMKeyPair) pemParser.readObject();
            return (ECPrivateKey) converter.getPrivateKey(keyPair.getPrivateKeyInfo());
        }
    }

    public ECPublicKey publicKey() throws IOException {
        try (InputStreamReader reader = new InputStreamReader(publicKeyRes.getInputStream())) {
            final var pemParser = new PEMParser(reader);
            final var converter = new JcaPEMKeyConverter();

            final var publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject());
            return (ECPublicKey) converter.getPublicKey(publicKeyInfo);
        }
    }
}
