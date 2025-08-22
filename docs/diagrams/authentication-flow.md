# Fluxo de Autenticação - BC Finances

## Visão Geral

Este documento descreve o fluxo completo de autenticação JWT no sistema BC Finances, desde o login do usuário até o acesso a recursos protegidos.

## Fluxo de Login

```mermaid
sequenceDiagram
    participant U as User
    participant F as Angular Frontend
    participant A as AuthController
    participant AM as AuthManager
    participant US as UserService
    participant DB as PostgreSQL
    participant JWT as JwtService
    participant LS as LocalStorage

    U->>F: Digita email/senha no form
    F->>F: Validação básica do form
    
    F->>A: POST /auth/login
    Note over F,A: { email: "admin@algamoney.com", password: "admin" }
    
    A->>AM: authenticate(email, password)
    AM->>US: loadUserByUsername(email)
    US->>DB: SELECT * FROM users WHERE email = ?
    DB-->>US: User data + permissions
    US-->>AM: UserDetails with authorities
    
    AM->>AM: Validate password with BCrypt
    
    alt Password invalid
        AM-->>A: BadCredentialsException
        A-->>F: 401 Unauthorized
        F-->>U: "Usuário ou senha inválidos!"
    else Password valid
        AM-->>A: Authentication object
        
        A->>JWT: generateToken(userDetails)
        JWT->>JWT: Create JWT with authorities
        JWT-->>A: JWT token string
        
        A-->>F: LoginResponse
        Note over A,F: { accessToken: "eyJ...", tokenType: "Bearer", expiresIn: 3600000 }
        
        F->>LS: Store token in localStorage
        F-->>U: Redirect to dashboard
    end
```

## Fluxo de Acesso a Recursos Protegidos

```mermaid
sequenceDiagram
    participant F as Angular Frontend
    participant I as HTTP Interceptor
    participant AG as AuthGuard
    participant C as Spring Controller
    participant S as Spring Security
    participant JWT as JWT Decoder
    participant M as Business Method

    F->>AG: Route navigation
    AG->>AG: Check token validity
    
    alt Token invalid/expired
        AG->>F: Redirect to /login
    else Token valid
        AG->>AG: Check route permissions
        
        alt Insufficient permissions
            AG->>F: Redirect to /not-authorized
        else Authorized
            AG-->>F: Allow navigation
            
            F->>I: HTTP request
            I->>I: Add Authorization header
            Note over I: Authorization: Bearer eyJ...
            
            I->>C: API request with JWT
            C->>S: @PreAuthorize verification
            S->>JWT: Decode and validate token
            JWT-->>S: Authorities list
            
            S->>S: Check hasAuthority('ROLE_...')
            
            alt No permission
                S-->>C: AccessDeniedException
                C-->>I: 403 Forbidden
                I-->>F: Error handled
            else Authorized
                S->>M: Execute business logic
                M-->>C: Result
                C-->>I: Success response
                I-->>F: Data displayed
            end
        end
    end
```

## Fluxo de Logout

```mermaid
sequenceDiagram
    participant U as User
    participant F as Angular Frontend
    participant LS as LocalStorage
    participant AS as AuthService

    U->>F: Click logout button
    F->>AS: logout()
    AS->>LS: Remove token
    AS->>AS: Clear jwtPayload
    AS-->>F: Promise resolved
    F->>F: Redirect to /login
    F-->>U: Login page displayed
```

## Detalhamento dos Componentes

### 1. AuthController (Backend)

```java
@PostMapping("/login")
public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    // 1. Autenticar usuário
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(),
            loginRequest.getPassword()
        )
    );

    // 2. Gerar JWT
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    String token = jwtService.generateToken(userDetails);

    // 3. Retornar resposta
    LoginResponse response = new LoginResponse(token, "Bearer", jwtService.getExpirationTime());
    return ResponseEntity.ok(response);
}
```

### 2. AuthService (Frontend)

```typescript
login(email: string, password: string): Observable<any> {
    const body = { email, password };
    
    return this.httpClient.post(this.authLoginUrl, body, {headers})
        .pipe(
            tap((response: any) => this.storeToken(response.accessToken)),
            catchError(exception => {
                if (exception.status === 401) {
                    return throwError('Usuário ou senha inválidos!');
                }
                return throwError(exception);
            })
        );
}
```

### 3. AuthGuard (Frontend)

```typescript
canActivate(route: ActivatedRouteSnapshot): boolean {
    // Verificar token válido
    if (this.auth.isAccessTokenInvalid()) {
        this.router.navigate(['/login']);
        return false;
    }
    
    // Verificar permissões da rota
    if (route.data.roles && !this.auth.hasAnyPermission(route.data.roles)) {
        this.router.navigate(['/not-authorized']);
        return false;
    }
    
    return true;
}
```

### 4. Spring Security Configuration

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/login").permitAll()
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
        .sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .build();
}
```

## Estados do Token

### Token Válido
- ✅ Formato JWT correto (3 partes)
- ✅ Assinatura RS256 válida
- ✅ Não expirado (exp > now)
- ✅ Authorities presentes

### Token Inválido
- ❌ Formato incorreto
- ❌ Assinatura inválida
- ❌ Expirado
- ❌ Claims ausentes

## Tratamento de Erros

### Frontend
```typescript
// AuthService
catchError(exception => {
    if (exception.status === 401) {
        return throwError('Usuário ou senha inválidos!');
    }
    return throwError(exception);
})

// HTTP Interceptor
if (error.status === 401) {
    this.router.navigate(['/login']);
} else if (error.status === 403) {
    this.router.navigate(['/not-authorized']);
}
```

### Backend
```java
// Exception Handler
@ExceptionHandler(BadCredentialsException.class)
public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body("Credenciais inválidas");
}

@ExceptionHandler(AccessDeniedException.class)
public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body("Acesso negado");
}
```

## Segurança Implementada

### Proteções Backend
- ✅ CORS configurado
- ✅ CSRF desabilitado (stateless)
- ✅ JWT com assinatura RS256
- ✅ Passwords com BCrypt
- ✅ Session stateless

### Proteções Frontend
- ✅ Token no localStorage (HTTPS only em prod)
- ✅ Guards em todas as rotas protegidas
- ✅ Interceptação automática de 401/403
- ✅ Limpeza de token em logout