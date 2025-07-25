package com.raii.jwtauth.security;

import com.raii.jwtauth.security.props.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;

    private ECPublicKey publicKey;
    private ECPrivateKey privateKey;

    @PostConstruct
    public void initKeys() throws IOException {
        this.publicKey = jwtProperties.publicKey();
        this.privateKey = jwtProperties.privateKey();
    }

    private JwtBuilder buildToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(privateKey, Jwts.SIG.ES512);
    }

    public String issueAccessToken(UserDetails userDetails) {
        return buildToken(userDetails)
                .claim("typ", "access")
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiry() * 1000))
                .compact();
    }

    public String issueRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails)
                .claim("typ", "refresh")
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpiry() * 1000))
                .compact();
    }

    public Claims validateToken(String token) throws io.jsonwebtoken.JwtException, IllegalArgumentException {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
