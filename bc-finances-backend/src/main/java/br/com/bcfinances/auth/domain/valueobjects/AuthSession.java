package br.com.bcfinances.auth.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class AuthSession implements Serializable {

    @Serial
    private static final long serialVersionUID = -5936782146557648241L;

    private final String id;
    private final Long userId;
    private final String email;
    private final String name;
    private final List<String> authorities;
    private final Instant issuedAt;
    private final Instant expiresAt;

    @JsonCreator
    public AuthSession(
            @JsonProperty("id") String id,
            @JsonProperty("userId") Long userId,
            @JsonProperty("email") String email,
            @JsonProperty("name") String name,
            @JsonProperty("authorities") List<String> authorities,
            @JsonProperty("issuedAt") Instant issuedAt,
            @JsonProperty("expiresAt") Instant expiresAt
    ) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.authorities = authorities;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    public String getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
