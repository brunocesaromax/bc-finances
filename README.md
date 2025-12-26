# BC Finances
[![NPM](https://img.shields.io/npm/l/express)](https://github.com/brunocesaromax/lancamentos/blob/master/LICENSE) [![PR Checks](https://github.com/brunocesaromax/bc-finances/actions/workflows/pr-checks.yml/badge.svg?event=pull_request)](https://github.com/brunocesaromax/bc-finances/actions/workflows/pr-checks.yml)

# Sobre o projeto

A aplicação consiste no gerenciamento financeiro do usuário, através de lançamentos de débitos e créditos.

## Acesso 
Login: admin@algamoney.com   
Senha: admin

## Produção (Railway)

- URL: https://bc-finances.up.railway.app
- Deploy: push na branch `master` publica automaticamente em produção.
- Serviços: frontend, backend, PostgreSQL, Redis, OpenObserve e bucket de arquivos.

## Documentação Técnica

Para documentação detalhada da arquitetura e diagramas, consulte:

### Backend

#### Arquitetura
- **[Clean Architecture](docs/architecture/clean-architecture.md)** - Documentação completa da arquitetura atual com diagramas Mermaid

#### Diagramas e Modelagem  
- **[Modelo de Classes](docs/diagrams/class-diagram.md)** - Estrutura das entidades e relacionamentos
- **[Modelo de Dados](docs/diagrams/entity-relationship-diagram.md)** - Schema do banco PostgreSQL  

#### Segurança
- **[Autenticação](docs/security/authentication.md)** - Implementação JWT completa
- **[Autorização](docs/security/authorization.md)** - Sistema de permissões e roles
- **[Fluxo de Autenticação](docs/diagrams/authentication-flow.md)** - Diagramas de sequência
- **[Fluxo de Autorização](docs/diagrams/authorization-flow.md)** - Controle de acesso detalhado

### Frontend
- **[Guia do Frontend](docs/frontend/README.md)** - Padrões de arquitetura, código e convenções do projeto

### Releases
- **[Processo de Release](docs/release/release-process.md)** - Passo a passo para versionamento, criação de tags e publicação oficial

## Tecnologias utilizadas

### Back end
- Java 25
- Spring Boot 4.0.1
- JPA/ Hibernate 6.6.22
- Spring Security 6 com JWT stateless
- AWS SDK v2 para integração S3
- PostgreSQL 16 (database: bc-finances | username: postgres | password: postgres)
- Maven 

### Front end
- React 19 (Vite)
- TypeScript 5.9
- Tailwind CSS 4
- Axios, React Hook Form, Zod, React Router DOM

## Como executar o projeto

### Executando com Docker Compose (Recomendado)

Pré-requisitos: Docker e Docker Compose

  1 - Clonar o repositório: 
  
  ```bash 
  git clone https://github.com/brunocesaromax/bc-finances.git
  cd lancamentos
  ```
  
  2 - Subir toda a aplicação (PostgreSQL + Backend + Frontend + pgAdmin):
  
  ```bash 
  docker-compose up -d
  ```
  
  3 - Aguardar os serviços subirem e acessar:
  
  - **Frontend**: http://localhost:5173
  - **Backend API**: http://localhost:8080
  - **pgAdmin**: http://localhost:8081
  - **OpenObserve**: http://localhost:5080
  - **Health Check**: http://localhost:8080/actuator/health
  
  4 - Para parar todos os serviços:
  
  ```bash 
  docker-compose down
  ```
  
### Comandos Docker Úteis

  ```bash
  # Ver logs de todos os serviços
  docker-compose logs -f
  
  # Ver logs de um serviço específico
  docker-compose logs -f backend
  docker-compose logs -f frontend
  
  # Rebuild containers após mudanças no código
  docker-compose up -d --build
  
  # Parar e remover containers + volumes + networks
  docker-compose down -v --remove-orphans
  
  # Ver status dos containers
  docker-compose ps
  ```

## Observabilidade com OpenObserve

- O `docker-compose` sobe automaticamente o serviço `openobserve` (portas 5080/5081) e persiste dados no volume `openobserve_data`.
- O backend Spring Boot está instrumentado com **OpenTelemetry + Micrometer** para enviar **logs, traces e métricas (100%)** via OTLP diretamente para o OpenObserve.
- Variáveis relevantes (configure no `.env`): `OPENOBSERVE_USERNAME`, `OPENOBSERVE_PASSWORD`, `OPENOBSERVE_ORG`, `OPENOBSERVE_STREAM`, `OTEL_EXPORTER_OTLP_ENDPOINT`, `OTEL_EXPORTER_OTLP_HEADER_AUTHORIZATION`.
- **Gere um API Token** no OpenObserve (Settings › API Tokens) e use `echo -n '<org_id>:<api_token>' | base64` para montar o header. Exemplo: `OTEL_EXPORTER_OTLP_HEADER_AUTHORIZATION="Basic ZGVmYXVsdDphcGlfdG9rZW4"`; o uso de email/senha gera erro `401 Unauthorized`.
- Dashboards podem ser criados no OpenObserve consumindo o stream padrão `bcfinances`; todos os eventos possuem `service.name=bc-finances-backend` para fácil filtragem.

## Build de Imagens (Backend)

O Dockerfile do backend utiliza recursos do Docker BuildKit (cache de `/root/.m2`). Ao gerar a imagem local, habilite o BuildKit:

```bash
DOCKER_BUILDKIT=1 docker build -t bc-finances-backend ./bc-finances-backend
```

**Observação**: Sem o BuildKit a etapa `mvn dependency:go-offline` fica lenta porque baixa todas as dependências sem cache. Caso queira tornar permanente, habilite BuildKit no Docker Desktop (Settings › General) ou adicione `"features": {"buildkit": true}` ao arquivo `~/.docker/config.json`.

### Executando apenas Banco de Dados com Docker

  1 - Para subir apenas PostgreSQL + pgAdmin:
  
  ```bash 
  docker-compose up -d postgres pgadmin
  ```
  
  2 - Executar backend localmente:
  
  ```bash 
  cd bc-finances-backend
  mvn spring-boot:run
  ```

### Back end (Desenvolvimento Local)

Pré-requisitos: Java 25, PostgreSQL 16

  1 - Clonar o repositório: 
  
  ```bash 
  git clone https://github.com/brunocesaromax/bc-finances.git
  ```
  
  
  2 - Entrar na pasta do projeto back end: 
  
  ```bash 
  cd bc-finances-backend
  ```
  
  3 - Configurar banco PostgreSQL local e variáveis de ambiente no .env
  
  4 - Executar o projeto:
  
  
  ```bash 
  mvn spring-boot:run
  ```

  5 - Validações de qualidade (opcional):

  ```bash
  mvn test
  mvn checkstyle:check
  ./script/validate-migrations.sh
  ```

### Front end web (React)

**Pré-requisitos:** Node.js 20+ (LTS), npm 10+

1 - Clonar o repositório:

```bash
git clone https://github.com/brunocesaromax/bc-finances.git
```

2 - Entrar na pasta do frontend React:

```bash
cd bc-finances-frontend
```

3 - Instalar dependências

```bash
npm install
```

4 - Executar o projeto (desenvolvimento):

```bash
npm run dev
```

5 - Gerar build de produção:

```bash
npm run build
```

Para pré-visualizar o bundle:

```bash
npm run preview
```

## Autenticação e Autorização

A aplicação utiliza **JWT (JSON Web Token) stateless** para autenticação e autorização:

### Backend
- **Endpoint de login**: `POST /auth/login` (email/password → JWT)
- **Autorização**: Spring Security 6 com método `@PreAuthorize` 
- **JWT**: Token com authorities para controle de permissões
- **Logout**: Client-side (remove token do localStorage)

### Frontend
- **Login**: Envio de credenciais via JSON para `/auth/login`
- **Armazenamento**: Token JWT salvo no localStorage do navegador
- **Interceptação**: Token enviado automaticamente no header `Authorization: Bearer <token>`
- **Guards**: Proteção de rotas com verificação de token válido e permissões

**Credenciais padrão**: admin@algamoney.com / admin

Para documentação detalhada, consulte: `docs/security/`

# Autor

Bruno César Vicente

 <a href="https://www.linkedin.com/in/bruno-cesar-vicente" target="_blank"><img src="https://img.shields.io/badge/-LinkedIn-%230077B5?style=for-the-badge&logo=linkedin&logoColor=white" target="_blank"></a> 
