package com.spring.authsvr.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.spring.authsvr.exception.AuthException;
import com.spring.authsvr.exception.AuthExceptionEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

/**
 * <p>  </p>
 *
 * @author ZShuo
 * @description
 * @date 2025/7/15 16:22
 */
@Component
public class TokenUtil {

    private static Logger logger = LogManager.getLogger(TokenUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    public static String getSHA256(String password, byte[] salt){
        MessageDigest sha256 = null;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        sha256.update(salt);
        byte[] hash = sha256.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    public static byte[] getSecureRandom(int length) {
        byte[] salt = new byte[length];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    public String generateAccessToken(Long userId, String username) {

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim("user_id",userId)
                .withClaim("username",username)
                .withExpiresAt(Instant.now().plusSeconds(3600))
                .sign(algorithm);
    }

    public String generateRefreshToken(Long userId, String username) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim("user_id",userId)
                .withClaim("username",username)
                .withExpiresAt(Instant.now().plusSeconds(12 * 3600))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Long verifyAccessToken(String token) {
        DecodedJWT jwt;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            jwt = verifier.verify(token);

            return jwt.getClaim("user_id").asLong();
        }catch (JWTVerificationException e) {
            logger.info("JWT verification failed,{}", e.getMessage());
            throw new AuthException(AuthExceptionEnum.AUTH_FAILED);
        }
    }

}
