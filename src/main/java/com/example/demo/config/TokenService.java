package com.example.demo.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class TokenService {

    // Em um projeto real, esta chave deve ser armazenada de forma segura
    private String secretKey = "SuaChaveSecretaDeVerdade";

    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + 1800000)) // 30 minutos
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getSubject(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}