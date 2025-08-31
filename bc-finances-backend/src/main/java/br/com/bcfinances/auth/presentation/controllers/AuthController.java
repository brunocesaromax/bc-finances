package br.com.bcfinances.auth.presentation.controllers;

import br.com.bcfinances.auth.application.dto.LoginRequest;
import br.com.bcfinances.auth.application.dto.LoginResponse;
import br.com.bcfinances.auth.application.mappers.AuthMapper;
import br.com.bcfinances.auth.application.usecases.LoginUseCase;
import br.com.bcfinances.auth.application.usecases.LoginUseCase.LoginResult;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final AuthMapper authMapper;

    public AuthController(LoginUseCase loginUseCase, AuthMapper authMapper) {
        this.loginUseCase = loginUseCase;
        this.authMapper = authMapper;
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
}