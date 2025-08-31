package br.com.bcfinances.auth.application.mappers;

import br.com.bcfinances.auth.application.dto.LoginResponse;
import br.com.bcfinances.auth.application.usecases.LoginUseCase.LoginResult;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public LoginResponse toResponse(LoginResult loginResult) {
        if (loginResult == null) {
            return null;
        }
        
        return new LoginResponse(
            loginResult.accessToken(),
            loginResult.tokenType(),
            loginResult.expiresIn(),
            loginResult.userName()
        );
    }
}