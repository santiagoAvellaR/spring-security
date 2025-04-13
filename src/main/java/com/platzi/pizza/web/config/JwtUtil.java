package com.platzi.pizza.web.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private static  String SECRET_KEY = "pl4tz1-p1zz3r14";
    private static Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

    public String create(String userName){
        return JWT.create()
                .withSubject(userName)
                .withIssuer("platzi-pizzeria")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15)))
                .sign(ALGORITHM);
    }

    public boolean isValid(String jwt){
        try {
            JWT.require(ALGORITHM)
                    .withIssuer("platzi-pizzeria")
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUserName(String jwt){
        return JWT.require(ALGORITHM)
                .withIssuer("platzi-pizzeria")
                .build()
                .verify(jwt)
                .getSubject();
    }
}
