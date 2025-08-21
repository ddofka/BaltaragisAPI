package org.codeacademy.baltaragisapi.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    private static final int HS256_KEY_BYTES = 32; // 256 bits
    private static final long ACCESS_TOKEN_TTL_SECONDS = 15 * 60; // 15 minutes

    private final String configuredSecret;
    private byte[] secret;

    public JwtService(@Value("${jwt.secret:}") String configuredSecret) {
        this.configuredSecret = configuredSecret;
    }

    @PostConstruct
    public void init() {
        if (configuredSecret != null && !configuredSecret.isBlank()) {
            secret = Base64.getDecoder().decode(configuredSecret);
        } else {
            // Generate strong random secret for dev fallback
            SecureRandom random = new SecureRandom();
            byte[] randomKey = new byte[HS256_KEY_BYTES];
            random.nextBytes(randomKey);
            secret = randomKey;
            String base64Secret = Base64.getEncoder().encodeToString(secret);
            log.warn("\n\n*** WARNING: No jwt.secret configured! Using a random dev secret. ***\n" +
                    "This is NOT safe for production. Set 'jwt.secret' to a strong Base64-encoded value.\n" +
                    "Dev fallback secret: {}\n", base64Secret);
        }
    }

    public String createToken(String subject, Map<String, Object> claims, String role) {
        try {
            Instant now = Instant.now();
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                    .subject(subject)
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(now.plusSeconds(ACCESS_TOKEN_TTL_SECONDS)))
                    .claim("role", role);
            if (claims != null) {
                claims.forEach(builder::claim);
            }
            JWTClaimsSet claimsSet = builder.build();
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            SignedJWT jwt = new SignedJWT(header, claimsSet);
            jwt.sign(new MACSigner(secret));
            return jwt.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create JWT", e);
        }
    }

    public JWTClaimsSet validateToken(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            if (!jwt.verify(new MACVerifier(secret))) {
                throw new JOSEException("Invalid signature");
            }
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            Date now = new Date();
            if (claims.getExpirationTime() == null || now.after(claims.getExpirationTime())) {
                throw new JOSEException("Token expired");
            }
            return claims;
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired JWT: " + e.getMessage(), e);
        }
    }
}
