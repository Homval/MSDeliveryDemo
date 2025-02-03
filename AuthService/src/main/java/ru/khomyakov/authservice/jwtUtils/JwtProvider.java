package ru.khomyakov.authservice.jwtUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.khomyakov.authservice.models.dto.JwtAuthenticationDto;
import ru.khomyakov.authservice.models.entities.Role;
import ru.khomyakov.authservice.models.entities.User;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class JwtProvider {
    private final SecretKey jwtAccessSecretKey;
    private final SecretKey jwtRefreshSecretKey;
    private final long jwtAccessExpirationTime;
    private final long jwtRefreshExpirationTime;
    private final ObjectMapper objectMapper;

    public JwtProvider(@Value("${jwt.access.secret}") SecretKey jwtAccessSecretKey,
                       @Value("${jwt.refresh.secret}") SecretKey jwtRefreshSecretKey,
                       @Value("${jwt.access.expiration}") long jwtAccessExpirationTime,
                       @Value("${jwt.refresh.expiration}") long jwtRefreshExpirationTime,
                       ObjectMapper objectMapper) {
        this.jwtAccessSecretKey = jwtAccessSecretKey;
        this.jwtRefreshSecretKey = jwtRefreshSecretKey;
        this.jwtAccessExpirationTime = jwtAccessExpirationTime;
        this.jwtRefreshExpirationTime = jwtRefreshExpirationTime;
        this.objectMapper = objectMapper;
    }


    public String createAccessToken(User user) {
        return Jwts.builder()
                .signWith(jwtAccessSecretKey)
                .setPayload(createPayload(user, jwtAccessExpirationTime))
                .compact();
    }

    public String createRefreshToken(User user) {
        return Jwts.builder()
                .signWith(jwtRefreshSecretKey)
                .setPayload(createPayload(user, jwtRefreshExpirationTime))
                .compact();
    }

    private String createPayload(User user, long jwtAccessExpirationTime) {
        try {
            return objectMapper.writeValueAsString(
                    JwtAuthenticationDto.builder()
                            .userUuid(user.getId())
                            .roles(getRolesNames(user.getRoles()))
                            .createDate(getCreateDate())
                            .expirationDate(getExpirationDate(jwtAccessExpirationTime))
                            .build()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Date getExpirationDate(long jwtAccessExpirationTime) {
        return Date.from(LocalDateTime.now().plusSeconds(jwtAccessExpirationTime).atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date getCreateDate() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    private List<String> getRolesNames(Set<Role> roles) {
        return roles.stream().map(Role::getName).toList();
    }


}
