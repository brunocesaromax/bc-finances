# 🤖 CLAUDE.md - AI Development Agent Instructions

**Projeto:** Sistema de Gestão Financeira - Modernização  
**Repositório:** https://github.com/brunocesaromax/lancamentos  
**Versão das Instruções:** 1.0  
**Última Atualização:** Agosto 2025

---

## 🎯 CONTEXTO DO PROJETO

Você é o **AI Development Agent** responsável por auxiliar na modernização de um sistema legado de gestão financeira pessoal. Este projeto tem como objetivo principal criar um **portfolio técnico impressionante** que demonstre conhecimentos avançados de arquitetura de software.

### Objetivos Principais
1. **Portfolio Técnico:** Demonstrar conhecimentos de pós-graduação em arquitetura
2. **Modernização Técnica:** Migrar de tecnologias obsoletas para stack moderna
3. **Clean Architecture:** Implementar padrões arquiteturais enterprise-grade
4. **Qualidade de Código:** Manter padrões profissionais rigorosos

---

## 📚 DOCUMENTAÇÃO OBRIGATÓRIA

**SEMPRE consulte a pasta `docs/` antes de qualquer implementação:**

```
docs/
├── architecture/
│   ├── FRD-Modernizacao.md         # 📋 DOCUMENTO PRINCIPAL - Leia PRIMEIRO
│   ├── clean-architecture.md       # Estrutura e padrões
│   ├── ddd-contexts.md             # Domain-Driven Design
│   └── security-guidelines.md      # Diretrizes de segurança
├── api/
│   ├── rest-api-standards.md       # Padrões de API
│   ├── openapi-spec.yml           # Especificação OpenAPI
│   └── error-handling.md          # Tratamento de erros
├── deployment/
│   ├── docker-guide.md            # Containerização
│   ├── cicd-pipeline.md           # Pipeline CI/CD
│   └── environment-setup.md       # Setup de ambientes
└── development/
    ├── coding-standards.md         # Padrões de código
    ├── testing-strategy.md         # Estratégia de testes
    ├── git-workflow.md            # Fluxo Git
    └── code-review-checklist.md   # Checklist para review
```

---

## 🏗️ ARQUITETURA E PADRÕES

### Clean Architecture (Obrigatório)
```
src/main/java/com/financial/
├── modules/
│   ├── transaction/              # Core Business
│   │   ├── domain/              # Entities, Value Objects, Domain Services
│   │   ├── application/         # Use Cases, Ports
│   │   ├── infrastructure/      # Adapters, Repositories
│   │   └── web/                # Controllers, DTOs
│   ├── identity/               # Autenticação
│   ├── registry/               # Cadastros
│   ├── analytics/              # Dashboard
│   ├── document/               # Upload/Export
│   └── notification/           # Alertas
└── shared/
    ├── kernel/                 # Domain primitives
    └── infrastructure/         # Cross-cutting concerns
```

### Princípios Fundamentais
1. **Dependency Inversion:** Dependências sempre apontam para dentro
2. **Single Responsibility:** Uma responsabilidade por classe
3. **Interface Segregation:** Interfaces específicas e coesas
4. **Domain-Driven Design:** Linguagem ubíqua e bounded contexts
5. **CQRS:** Separação clara entre Commands e Queries

---

## 💻 STACK TECNOLÓGICA

### Backend (OBRIGATÓRIO)
```yaml
Core:
  - Java 21 (LTS) - Usar features modernas
  - Spring Boot 3.3.x - Configuração declarativa
  - Spring Security 6 - JWT + Redis session
  - Spring Data JPA - Repository pattern

Database:
  - PostgreSQL 16 - Primary database
  - Redis - Cache L2 + Sessions
  - Flyway - Database migrations

Messaging:
  - RabbitMQ - Domain events
  - Spring Cloud Stream - Event handling

Testing:
  - JUnit 5 - Unit tests
  - TestContainers - Integration tests
  - WireMock - External service mocking
  - JaCoCo - Coverage ≥ 80%

Build:
  - Maven 3.9+ - Dependency management
  - Docker - Containerização
```

### Frontend (OBRIGATÓRIO)
```yaml
Core:
  - Vue.js 3.5+ - Composition API only
  - TypeScript 5+ - Strict mode
  - Vite 5+ - Build tool

State & Routing:
  - Pinia - State management
  - Vue Router 4 - SPA routing

UI/UX:
  - Tailwind CSS - Utility-first CSS
  - HeadlessUI - Accessible components
  - Chart.js - Data visualization

Testing:
  - Vitest - Unit testing
  - Cypress - E2E testing (critical paths only)
```

---

## 🔒 DIRETRIZES DE SEGURANÇA

### Autenticação (OBRIGATÓRIO)
```yaml
JWT Strategy:
  - Access Token: 15 minutos
  - Refresh Token: 7 dias com rotação
  - Algoritmo: RS256
  - Storage: Redis para blacklist

Implementação:
  - Spring Security 6
  - @PreAuthorize para autorização
  - BCrypt strength 12 para senhas
  - Rate limiting com Bucket4j
```

### Validação em Camadas
```yaml
1. Controller: Bean Validation (@Valid, @NotNull)
2. Application: Business rules validation
3. Domain: Entity invariants
4. Infrastructure: Database constraints
```

### Dados Sensíveis
```yaml
- NUNCA logar senhas ou tokens
- Mascarar PII nos logs
- Usar @JsonIgnore para campos sensíveis
- Validar TODOS os inputs
- Sanitizar outputs
```

---

## 📝 PADRÕES DE CÓDIGO

### Convenções Gerais
```yaml
Idioma: 100% inglês (código, comentários, commits)
Nomenclatura: 
  - Classes: PascalCase
  - Métodos/variáveis: camelCase
  - Constantes: UPPER_SNAKE_CASE
  - Packages: lowercase

Estrutura:
  - Máximo 20 linhas por método
  - Máximo 200 linhas por classe
  - Evitar nested if > 3 níveis
  - Usar early return
```

### Domain Modeling
```java
// ✅ CORRETO - Value Object
public record Money(BigDecimal amount, Currency currency) {
    public Money {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }
}

// ✅ CORRETO - Aggregate Root
@Entity
public class Transaction {
    @Id
    private TransactionId id;
    private Money amount;
    private TransactionDate date;
    
    // Domain logic aqui, não nos services
    public void categorize(Category category) {
        this.category = category;
        this.registerEvent(new TransactionCategorizedEvent(this.id));
    }
}
```

### Use Cases (Application Layer)
```java
// ✅ CORRETO - Use Case
@Component
@Transactional
public class CreateTransactionUseCase {
    
    public TransactionDto execute(CreateTransactionCommand command) {
        // 1. Validate command
        // 2. Load aggregates
        // 3. Execute business logic
        // 4. Persist changes
        // 5. Publish events
        // 6. Return result
    }
}
```

---

## 🧪 ESTRATÉGIA DE TESTES

### Pirâmide de Testes
```yaml
Unit Tests (70%):
  - Domain entities
  - Use cases
  - Value objects
  - Mappers

Integration Tests (20%):
  - Repository tests com @DataJpaTest
  - Controller tests com @WebMvcTest
  - Message handling

E2E Tests (10%):
  - Critical user journeys
  - Happy path scenarios
  - Cypress para frontend crítico
```

### Test Naming Convention
```java
// ✅ CORRETO
@Test
void should_CreateTransaction_When_ValidDataProvided() {
    // Given
    // When
    // Then
}

@Test
void should_ThrowException_When_NegativeAmountProvided() {
    // Given
    // When & Then
}
```

### TestContainers (Obrigatório para Integration Tests)
```java
@DataJpaTest
@Testcontainers
class TransactionRepositoryIT {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    
    // Tests here
}
```

---

## 🔄 DESENVOLVIMENTO E GIT

### Branch Strategy
```yaml
main: Código de produção estável
develop: Branch de integração
feature/*: Features específicas
hotfix/*: Correções urgentes
release/*: Preparação para release
```

### Commit Convention
```
type(scope): description

Types: feat, fix, docs, style, refactor, test, chore
Scope: module name (transaction, identity, etc.)

Examples:
feat(transaction): add create transaction use case
fix(security): resolve JWT token validation issue
docs(api): update OpenAPI specification
test(transaction): add unit tests for money value object
```

### Pull Request Checklist
```yaml
Code Quality:
  ✓ Follows clean architecture principles
  ✓ No code smells or duplication
  ✓ Proper error handling
  ✓ Tests coverage ≥ 80%

Security:
  ✓ Input validation implemented
  ✓ No sensitive data in logs
  ✓ Authorization checks in place
  ✓ SQL injection protection

Documentation:
  ✓ API documentation updated
  ✓ README updated if needed
  ✓ Architecture docs updated
  ✓ Code comments for complex logic
```

---

## 🚨 O QUE NUNCA FAZER

### ❌ PROIBIDO
```yaml
Arquitetura:
  - Misturar responsabilidades entre camadas
  - Domain logic nos controllers
  - Business logic nos repositories
  - Dependências cíclicas

Código:
  - Hardcoded values (usar @ConfigurationProperties)
  - System.out.println (usar logger)
  - Catch genérico sem tratamento
  - Métodos com mais de 5 parâmetros

Segurança:
  - Logs com dados sensíveis
  - SQL queries concatenadas
  - Validação apenas no frontend
  - Tokens em URLs ou logs

Database:
  - Queries N+1
  - Falta de indices
  - Transações desnecessárias
  - Migrations sem rollback
```

---

## 📋 CHECKLIST DE IMPLEMENTAÇÃO

### Para Cada Feature
```yaml
Backend:
  ✓ Domain entity com business logic
  ✓ Value objects quando apropriado
  ✓ Repository interface
  ✓ Use case bem definido
  ✓ Controller RESTful
  ✓ DTO para entrada/saída
  ✓ Validation em todas as camadas
  ✓ Error handling apropriado
  ✓ Unit tests ≥ 80%
  ✓ Integration tests críticos

Frontend:
  ✓ Composable para lógica
  ✓ Store para estado global
  ✓ Type-safe com TypeScript
  ✓ Responsive design
  ✓ Error boundary
  ✓ Loading states
  ✓ Accessibility (a11y)

DevOps:
  ✓ Docker build funcionando
  ✓ CI/CD pipeline verde
  ✓ Health checks
  ✓ Monitoring logs
```

---

## 🎯 MÉTRICAS DE QUALIDADE

### Code Quality Gates
```yaml
Obrigatório para merge:
  ✓ Build success
  ✓ Tests passing
  ✓ Coverage ≥ 80%
  ✓ SonarCloud quality gate
  ✓ Zero critical/blocker issues
  ✓ Security scan clean
  ✓ Performance regression check
```

### Performance Targets
```yaml
API Response Time:
  - GET: < 200ms (95th percentile)
  - POST/PUT: < 500ms (95th percentile)
  - Complex queries: < 1s

Database:
  - Connection pool: 5-20 connections
  - Query timeout: 30s
  - Index usage mandatory

Frontend:
  - First Contentful Paint: < 1.5s
  - Largest Contentful Paint: < 2.5s
  - Cumulative Layout Shift: < 0.1
```

---

## 🔧 FERRAMENTAS E CONFIGURAÇÃO

### IDE Setup (Recomendado)
```yaml
IntelliJ IDEA:
  - SonarLint plugin
  - CheckStyle plugin
  - SpotBugs plugin
  - Docker plugin

VS Code:
  - Java Extension Pack
  - Spring Boot Extension
  - Vue.js extensions
  - GitLens
  - SonarLint
```

### Local Development
```bash
# Setup inicial
./scripts/dev-setup.sh

# Rodar aplicação local
docker-compose -f docker-compose.dev.yml up

# Rodar testes
./scripts/test-all.sh

# Reset database
./scripts/db-reset.sh
```

---

## 📞 QUANDO SOLICITAR AJUDA

### Consulte o Desenvolvedor Principal Quando:
```yaml
Decisões Arquiteturais:
  - Mudanças na estrutura de módulos
  - Novos bounded contexts
  - Breaking changes na API
  - Performance crítica

Tecnologia:
  - Adicionar novas dependências
  - Mudanças no stack
  - Configurações de infraestrutura
  - Security concerns

Negócio:
  - Dúvidas sobre regras de negócio
  - Priorização de features
  - UX/UI decisions
  - Integração com sistemas externos
```

---

## 🎉 FILOSOFIA DE DESENVOLVIMENTO

### Princípios
1. **Qualidade sobre Quantidade:** Código bem feito > código rápido
2. **Simplicidade:** A solução mais simples que funciona
3. **Documentação:** Código autodocumentado + docs quando necessário
4. **Testes:** Se não tem teste, não funciona
5. **Segurança First:** Segurança não é opcional
6. **Performance Conscious:** Otimizar onde importa

### Lema do Projeto
> "Clean code that works"

---

**LEMBRE-SE:** Este projeto é um portfolio técnico. Cada linha de código será avaliada. Mantenha os padrões profissionais mais altos possíveis.

**📧 Dúvidas?** Consulte sempre a documentação em `docs/` antes de implementar qualquer feature.