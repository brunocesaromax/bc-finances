package br.com.bcfinances.auth.presentation.controllers;

import br.com.bcfinances.auth.application.dto.LoginRequest;
import br.com.bcfinances.auth.application.dto.LoginResponse;
import br.com.bcfinances.auth.application.mappers.AuthMapper;
import br.com.bcfinances.auth.application.usecases.LoginUseCase;
import br.com.bcfinances.auth.application.usecases.LoginUseCase.LoginResult;
import br.com.bcfinances.auth.application.usecases.LogoutUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final AuthMapper authMapper;
    private final LogoutUseCase logoutUseCase;

    public AuthController(LoginUseCase loginUseCase,
                          AuthMapper authMapper,
                          LogoutUseCase logoutUseCase) {
        this.loginUseCase = loginUseCase;
        this.authMapper = authMapper;
        this.logoutUseCase = logoutUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResult loginResult = loginUseCase.execute(
                loginRequest.getEmail(), 
                loginRequest.getPassword()
            );
            
            LoginResponse response = authMapper.toResponse(loginResult);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Jwt jwt) {
        String sessionId = jwt != null ? jwt.getId() : null;
        logoutUseCase.execute(sessionId);
        return ResponseEntity.noContent().build();
    }
}
