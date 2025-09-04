package br.com.bcfinances.auth.application.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {
    
    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private String userName;

    public LoginResponse(String accessToken, String tokenType, Long expiresIn, String userName) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.userName = userName;
    }

}