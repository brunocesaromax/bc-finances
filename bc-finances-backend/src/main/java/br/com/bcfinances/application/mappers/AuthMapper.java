package br.com.bcfinances.application.mappers;

import br.com.bcfinances.application.dto.LoginResponse;
import br.com.bcfinances.application.usecases.LoginUseCase.LoginResult;
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