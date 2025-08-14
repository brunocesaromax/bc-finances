# GitHub Copilot Code Review Instructions

Este arquivo define as instruções específicas para o GitHub Copilot realizar code reviews automáticos eficazes no projeto Lançamentos.

## Visão Geral do Projeto

Lançamentos é uma aplicação full-stack de gerenciamento financeiro com:
- **Backend**: Spring Boot 2.3.7 + Java 8, OAuth2/JWT, MySQL, AWS S3, JasperReports
- **Frontend**: Angular 9 + PrimeNG, Chart.js, deployment Heroku

## Princípios de Code Review

### 1. Arquitetura e Design
- Verificar aderência aos princípios SOLID
- Validar separação de responsabilidades (Controller → Service → Repository)
- Confirmar uso correto de padrões estabelecidos
- Avaliar impacto na arquitetura existente

### 2. Segurança
- **OAuth2/JWT**: Verificar implementação correta de autenticação e autorização
- **Dados Sensíveis**: Garantir que credenciais não sejam expostas em logs ou código
- **SQL Injection**: Validar uso de prepared statements e JPA Query Methods
- **CORS**: Verificar configurações adequadas para integração frontend/backend
- **Validação de Input**: Confirmar validação adequada de dados de entrada

### 3. Qualidade de Código

#### Backend (Java/Spring Boot)
- **Annotations**: Uso correto de `@Service`, `@Repository`, `@Controller`, `@RestController`
- **Exception Handling**: Tratamento adequado com exceções customizadas em `/service/exception`
- **Lombok**: Uso consistente para reduzir boilerplate
- **JPA**: Consultas eficientes e relacionamentos corretos
- **Flyway**: Migrações de banco de dados seguindo convenções

#### Frontend (Angular/TypeScript)
- **Módulos**: Organização correta em módulos de funcionalidade
- **Services**: Comunicação com API seguindo padrões Angular
- **Guards**: Implementação adequada de proteção de rotas
- **Components**: Responsabilidade única e reutilização
- **RxJS**: Uso correto de Observables e operators

### 4. Performance
- **Database**: Evitar N+1 queries, usar fetch joins adequadamente
- **Frontend**: Lazy loading de módulos, OnPush change detection quando apropriado
- **Caching**: Verificar implementação de cache quando necessário
- **Bundle Size**: Avaliar impacto no tamanho do bundle frontend

### 5. Testes
- **Cobertura**: Verificar testes unitários para lógica de negócio crítica
- **Backend**: JUnit com Spring Boot Test para integração
- **Frontend**: Jasmine/Karma para testes unitários
- **Mocking**: Uso adequado de mocks para isolamento de testes

## Padrões Específicos do Projeto

### Estrutura de Código
```
Backend (lancamentos-api/):
├── /model - Entidades JPA com Lombok
├── /repository - Interfaces JPA + /query para consultas customizadas
├── /service - Lógica de negócio + /exception para exceções
├── /resource - Controllers REST
├── /config - Configurações (Security, CORS, OAuth2)
└── /mail - Templates e serviços de email

Frontend (lancamentos-ui/):
├── /app/core - Módulo principal e configurações
├── /app/shared - Componentes e serviços compartilhados
├── /app/[feature] - Módulos de funcionalidade
└── /app/security - Guards e interceptors
```

### Convenções de Nomenclatura
- **Entidades**: PascalCase (ex: `LaunchCategory`)
- **Services**: Sufixo `Service` (ex: `LaunchService`)
- **Repositories**: Sufixo `Repository` (ex: `LaunchRepository`)
- **Controllers**: Sufixo `Resource` (ex: `LaunchResource`)
- **Exceptions**: Sufixo `Exception` (ex: `PersonInexistenteException`)

### Validações Obrigatórias
- **Bean Validation**: Uso de annotations (`@NotNull`, `@Valid`, etc.)
- **DTO**: Separação entre entidades e DTOs para API
- **Response Patterns**: Uso consistente de ResponseEntity
- **Error Handling**: ExceptionHandler global para tratamento de erros

## Critérios de Aprovação

### ✅ Aprovar quando:
- Código segue padrões SOLID e Clean Code
- Testes adequados estão presentes
- Não há vazamentos de segurança
- Performance não é impactada negativamente
- Documentação está atualizada (se necessário)
- Migrações de banco seguem convenções Flyway

### ❌ Solicitar mudanças quando:
- Violações de segurança (credenciais expostas, SQL injection, etc.)
- Código quebra princípios SOLID
- Ausência de testes para lógica crítica
- Performance degradada sem justificativa
- Padrões de nomenclatura não seguidos
- Responsabilidades misturadas entre camadas

## Pontos de Atenção Especiais

### Configurações Sensíveis
- Verificar uso de variáveis de ambiente para:
  - `DB_*` (conexão banco)
  - `MAIL_*` (configuração email)
  - `AWS_S3_*` (credenciais S3)
  - `*_CLIENT`, `*_PASSWORD` (OAuth2)

### Compatibilidade
- **Java 8**: Evitar features de versões posteriores
- **Angular 9**: Considerar limitações de versão
- **Browser Support**: Manter compatibilidade definida

### Deploy e CI/CD
- **Heroku**: Verificar impacto em configurações de deploy
- **Maven**: Validar dependências e profiles
- **npm**: Verificar scripts de build e dependencies

## Feedback Guidelines

### Tom e Estilo
- Feedback construtivo e educativo
- Explicar o "porquê" das sugestões
- Oferecer alternativas quando aplicável
- Reconhecer boas práticas implementadas

### Priorização
1. **Crítico**: Segurança, quebra de funcionalidade
2. **Alto**: Performance, arquitetura, padrões
3. **Médio**: Legibilidade, manutenibilidade
4. **Baixo**: Convenções menores, sugestões de melhoria

## Recursos de Referência
- [CLAUDE.md](../CLAUDE.md) - Instruções detalhadas do projeto
- [docs/](../docs/) - Documentação técnica e ADRs
- [README.md](../README.md) - Visão geral e setup
- [TODO.md](../TODO.md) - Tarefas e planejamento atual