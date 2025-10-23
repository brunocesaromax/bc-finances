# Autenticação - BC Finances

## Visão Geral

O BC Finances utiliza um modelo híbrido de autenticação com **JWT (JSON Web Token)** validado pelo backend e **controle de sessão em Redis** para permitir revogação antecipada de tokens.

## Arquitetura JWT

### Características
- **JWT assinado**: Tokens possuem claims necessários e são assinados com RS256 (RSA + SHA-256)
- **Sessão cacheada**: Cada login gera uma sessão (`AuthSession`) persistida no Redis com o mesmo TTL do token
- **Expiração controlada**: Sessões e tokens expiram simultaneamente; sessões inválidas são removidas do cache
- **Revogação via logout**: O endpoint de logout remove a sessão do Redis e invalida o token associado

## Fluxo de Autenticação

```mermaid
sequenceDiagram
    participant Frontend as Angular Frontend
    participant Backend as Spring Boot API
    participant DB as PostgreSQL
    participant JWT as JWT Service

    Frontend->>Backend: POST /auth/login
    Note over Frontend,Backend: { email, password }
    
    Backend->>DB: Verificar credenciais
    DB-->>Backend: Usuário encontrado
    
    Backend->>JWT: Gerar token JWT
    JWT-->>Backend: Token assinado
    Backend->>Redis: Persistir AuthSession com TTL
    
    Backend-->>Frontend: LoginResponse
    Note over Backend,Frontend: { accessToken, tokenType, expiresIn }
    
    Frontend->>Frontend: Armazenar token no localStorage
    
    Frontend->>Backend: GET /api/resource
    Note over Frontend,Backend: Authorization: Bearer <token>
    
    Backend->>JWT: Validar token
    JWT-->>Backend: Token válido + authorities
    Backend->>Redis: Buscar sessão pelo jti
    Redis-->>Backend: Sessão encontrada (ou expirada)
    
    Backend-->>Frontend: Recurso protegido
```

## Implementação Backend

### Endpoint de Login

**URL**: `POST /auth/login`

**Request Body**:
```json
{
  "email": "admin@algamoney.com",
  "password": "admin"
}
```

**Response**:
```json
{
  "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600000
}
```

### Configuração Spring Security

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class JwtSecurityConfig {
    
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
}
```

### Geração do Token JWT

```java
@Service
public class JwtService {
    
    public String generateToken(UserDetails userDetails) {
        return Jwt.withTokenValue("token")
            .header("alg", "RS256")
            .claim("sub", userDetails.getUsername())
            .claim("authorities", extractAuthorities(userDetails))
            .claim("iat", Instant.now())
            .claim("exp", Instant.now().plusSeconds(expirationTime))
            .build()
            .getTokenValue();
    }
}
```

## Implementação Frontend

### AuthService

```typescript
@Injectable()
export class AuthService {
  
  login(email: string, password: string): Observable<any> {
    const body = { email, password };
    
    return this.httpClient.post(this.authLoginUrl, body, {headers})
      .pipe(
        tap((response: any) => this.storeToken(response.accessToken))
      );
  }
  
  private storeToken(token: string) {
    this.jwtPayload = this.jwtHelperService.decodeToken(token);
    localStorage.setItem(TOKEN_NAME, token);
  }
}
```

### AuthGuard

```typescript
@Injectable()
export class AuthGuard implements CanActivate {
  
  canActivate(route: ActivatedRouteSnapshot): boolean {
    if (this.auth.isAccessTokenInvalid()) {
      this.router.navigate(['/login']);
      return false;
    }
    
    if (route.data.roles && !this.auth.hasAnyPermission(route.data.roles)) {
      this.router.navigate(['/not-authorized']);
      return false;
    }
    
    return true;
  }
}
```

## Interceptação HTTP

O token JWT é automaticamente incluído em todas as requisições HTTP através do `JwtModule`:

```typescript
JwtModule.forRoot({
  config: {
    tokenGetter: () => localStorage.getItem('token'),
    allowedDomains: environment.tokenAllowedDomains,
    disallowedRoutes: environment.tokenDisallowedRoutes
  }
})
```

## Logout

### Backend
- **Endpoint**: `DELETE /auth/logout`
- **Autenticação**: Obrigatória (token válido no header)
- **Comportamento**:
  1. Extrai o `jti` (ID do token) do JWT autenticado
  2. Remove a sessão correspondente do Redis
  3. Responde com `204 No Content`
  4. Se o token já estiver expirado ou a sessão não existir, o fluxo continua idempotente

### Frontend

```typescript
logout() {
  return this.httpClient.delete<void>(`${environment.apiUrl}/auth/logout`)
    .pipe(
      catchError(error => [401, 403, 404].includes(error?.status) ? of(null) : throwError(error))
    )
    .toPromise()
    .finally(() => this.auth.clearAccessToken());
}
```

- A promessa é resolvida mesmo quando o backend indica que a sessão já estava inválida (401/403/404), garantindo UX consistente.
- `AuthService.clearAccessToken()` remove o token do `localStorage` e limpa o payload carregado em memória.

## Configuração de Ambiente

### Development
```typescript
export const environment = {
  apiUrl: 'http://localhost:8080',
  tokenAllowedDomains: [new RegExp('localhost:8080')],
  tokenDisallowedRoutes: [new RegExp('/auth/login')]
};
```

### Production
```typescript
export const environment = {
  apiUrl: 'https://launchs-api.herokuapp.com',
  tokenAllowedDomains: [new RegExp('launchs-api.herokuapp.com')],
  tokenDisallowedRoutes: [new RegExp('/auth/login')]
};
```

## Segurança

### Proteções Implementadas
- **HTTPS obrigatório** em produção
- **CORS configurado** para domínios específicos
- **Tokens com expiração** limitada
- **Validação de assinatura** RS256
- **localStorage seguro** (não cookies por simplicidade)

### Credenciais Padrão
- **Email**: admin@algamoney.com
- **Senha**: admin

## Troubleshooting

### Token inválido
- Verificar formato JWT (3 partes separadas por ponto)
- Validar expiração do token
- Confirmar assinatura RS256

### Erro 401
- Credenciais incorretas
- Token expirado
- Token malformado

### Erro 403
- Usuário sem permissão para o recurso
- Authority incorreta no token
