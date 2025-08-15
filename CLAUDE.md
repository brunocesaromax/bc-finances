# CLAUDE.md

Este arquivo fornece orientação para o Claude Code (claude.ai/code) ao trabalhar com código neste repositório.

## OBRIGATÓRIO: Leitura Inicial de Contexto

**A CADA INÍCIO DE INTERAÇÃO, DEVE-SE LER OBRIGATORIAMENTE:**

1. **README.md** - Visão geral e instruções básicas do projeto
2. **TODO.md** - Estado atual das tarefas e planejamento
3. **CHANGELOG.md** - Histórico de mudanças e decisões técnicas
4. **Diretório ./docs/** - TODAS as documentações e decisões importantes do projeto

### Diretório ./docs como Centralizador
O diretório `./docs/` é o **repositório oficial** de documentações técnicas e decisões de arquitetura:
- **Architecture Decision Records (ADRs)** - Decisões arquiteturais importantes
- **Especificações técnicas** - Documentos detalhados de funcionalidades
- **Diagramas** - Modelos conceituais, fluxos e arquitetura
- **Guias de desenvolvimento** - Padrões específicos do projeto
- **Documentação de APIs** - Contratos e especificações de endpoints

**Todo conteúdo em ./docs/ deve ser utilizado como contexto obrigatório para compreender:**
- Decisões já tomadas no projeto
- Padrões estabelecidos
- Arquitetura atual
- Especificações de funcionalidades

## Visão Geral do Projeto

Lançamentos é uma aplicação full-stack de gerenciamento financeiro construída com Spring Boot (backend) e Angular 9 (frontend). O sistema gerencia entradas financeiras (lançamentos) com débitos e créditos, apresentando segurança OAuth2, armazenamento de arquivos AWS S3, notificações por email e geração de relatórios em PDF.

## Arquitetura

**Backend (lancamentos-api/):**
- Spring Boot 2.3.7 com Java 8
- Autenticação OAuth2 + JWT com suporte duplo de cliente (web + mobile)
- Banco de dados PostgreSQL 16 com migrações Flyway
- JasperReports para geração de PDF
- Integração AWS S3 para anexos de arquivos
- Notificações por email com templates Thymeleaf

**Frontend (lancamentos-ui/):**
- Angular 9 com componentes UI PrimeNG
- Gerenciamento de tokens JWT com @auth0/angular-jwt
- Chart.js para visualização de dados
- Deploy Heroku pronto com servidor Express

## Comandos Comuns de Desenvolvimento

### Desenvolvimento Backend
```bash
# Iniciar servidor de desenvolvimento (de lancamentos-api/)
./mvnw spring-boot:run

# Executar testes
./mvnw test

# Empacotar aplicação
./mvnw package

# Executar com perfil específico
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

### Desenvolvimento Frontend
```bash
# Instalar dependências (de lancamentos-ui/)
npm install

# Iniciar servidor de desenvolvimento
ng serve

# Build para produção
ng build --prod

# Executar testes
ng test

# Executar linting
ng lint

# Executar testes e2e
ng e2e
```

### Docker Compose (Recomendado)
```bash
# Subir PostgreSQL + pgAdmin
docker-compose up -d

# Verificar status dos containers
docker-compose ps

# Parar os serviços
docker-compose down

# Acessar pgAdmin: http://localhost:8081
# Email: admin@lancamentos.com | Senha: admin
```

## Configuração de Ambiente

A aplicação usa configuração baseada em ambiente com arquivos .env (dependência spring-dotenv):

**Variáveis de Ambiente Obrigatórias:**
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` - Conexão banco de dados PostgreSQL 16
- `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD` - Configuração de email
- `AWS_S3_ACCESS_KEY_ID`, `AWS_S3_SECRET_ACCESS_KEY`, `AWS_S3_BUCKET` - Armazenamento S3
- `FRONT_END_CLIENT`, `FRONT_END_PASSWORD` - Credenciais cliente OAuth2 frontend
- `MOBILE_CLIENT`, `MOBILE_PASSWORD` - Credenciais cliente OAuth2 mobile

**Perfis:**
- `dev` - Desenvolvimento com PostgreSQL (padrão: localhost:5432/bc-finances)
- `prod` - Configuração de produção
- `oauth-security` - Segurança OAuth2 (padrão)
- `basic-security` - Autenticação HTTP básica (alternativa)

## Padrões Principais do Código

**Configuração de Segurança:**
- Clientes OAuth2 duplos em `AuthorizationServerConfig` para web e mobile
- Tokens JWT com aprimoramento customizado em `CustomTokenEnhancer`
- Configuração CORS em `CorsFilter` para integração frontend

**Camada de Dados:**
- Entidades JPA em `/model` com anotações Lombok
- Consultas customizadas de repositório em `/repository/query` com Criteria API
- Migrações Flyway em `/resources/db/migration`

**Camada de Serviço:**
- Lógica de negócio em pacotes `/service`
- Tratamento de exceções com exceções customizadas em `/service/exception`
- Serviços de email em `/mail` com templates HTML

**Arquitetura Frontend:**
- Módulos de funcionalidades (launchs, persons, categories, reports, dashboard)
- Módulo compartilhado com componentes comuns
- Guards de rota para autenticação em `/security`
- Serviços para comunicação com API seguindo padrões Angular

## Schema do Banco de Dados

Entidades principais:
- `Launch` - Entradas financeiras com valor, tipo (DÉBITO/CRÉDITO), data de vencimento, pessoa, categoria
- `Person` - Usuários/entidades envolvidas nas transações financeiras
- `Category` - Classificação para lançamentos
- `User` - Usuários do sistema com funções e permissões
- `State`/`City` - Dados de localização de endereço

## Testes e Qualidade

**Backend:**
- Testes JUnit com Spring Boot Test
- Testes de integração de repositório
- Testes unitários da camada de serviço

**Frontend:**
- Jasmine/Karma para testes unitários
- Protractor para testes e2e
- TSLint para qualidade de código

Credenciais padrão: admin@algamoney.com / admin

## Padrões de Desenvolvimento

### Princípios de Código
- **SOLID e Clean Code**: Aplicar rigorosamente os princípios SOLID em todas as implementações
- **Responsabilidade Única**: Cada classe/módulo deve ter apenas uma razão para mudar
- **Aberto/Fechado**: Entidades devem estar abertas para extensão, fechadas para modificação
- **Substituição de Liskov**: Objetos de subclasses devem substituir objetos da classe base
- **Segregação de Interface**: Clientes não devem depender de interfaces que não usam
- **Inversão de Dependência**: Depender de abstrações, não de concretizações

### Documentação Obrigatória

#### Arquivos Globais do Projeto
- **TODO.md**: Arquivo único na raiz do projeto para todas as tarefas
- **CHANGELOG.md**: Arquivo único na raiz do projeto para histórico de versões

#### Estrutura do TODO.md
```markdown
# TODO - Lançamentos

## Sprint Atual (Branch: nome-da-branch)
- [ ] Tarefas da sprint/branch atual
- [ ] Tarefas em desenvolvimento ativo

## Próximas Funcionalidades
### Backend
- [ ] Tarefas planejadas para API
### Frontend  
- [ ] Tarefas planejadas para UI

## Bugs Conhecidos
- [ ] Problemas identificados pendentes
- [ ] Issues reportados

## Concluído
- [x] Tarefas finalizadas com referência de versão
```

#### Estrutura do CHANGELOG.md
```markdown
# Changelog

## [Versão] - Data (Branch: nome-da-branch)
### Backend
- Mudanças na API
- Correções no servidor

### Frontend
- Mudanças na UI
- Melhorias de UX

### Adicionado/Modificado/Corrigido/Removido
- Categorização clara das mudanças
```

#### Regras de Manutenção
1. **Atualizar TODO.md**: A cada nova tarefa, mudança de contexto ou conclusão
2. **Atualizar CHANGELOG.md**: A cada merge para branch principal ou release
3. **Referenciar branches**: Sempre indicar qual branch gerou as mudanças  
4. **Sincronizar com Git tags**: Versões devem corresponder a tags do Git
5. **Arquivar periodicamente**: Mover tarefas antigas concluídas para seção de arquivo
6. **Organizar por contexto**: Usar seções para diferentes áreas (Backend/Frontend/DevOps)

#### Quando Criar Arquivos Separados
Apenas em casos excepcionais de módulos completamente independentes:
- `lancamentos-api/TODO.md` e `lancamentos-ui/TODO.md` (se necessário)
- Nunca por branch ou feature individual

### Padrões de Comentários
- **Comentários Mínimos**: Usar apenas quando necessário para explicar o "porquê"
- **Código Auto-Explicativo**: O código deve expressar claramente o "o que" está sendo implementado
- **Documentação de Negócio**: Comentar apenas regras de negócio complexas ou decisões arquiteturais

### Padrões de Formatação
- **Proibido**: Uso de emoticons em código e documentações técnicas
- **Permitido**: Emoticons apenas em scripts shell ou Python para fins de UX do terminal
- **Consistência**: Manter formatação uniforme em todo o projeto

### Sincronização de IA
Este arquivo deve estar **100% sincronizado** com:
- `GEMINI.md` - Instruções para Google Gemini
- `.amazonq/rules` - Regras para Amazon Q
- Outros arquivos de configuração de IA que serão criados

## Notas de Deploy

**Deploy Heroku:**
- Frontend inclui `server.js` para hospedagem Express
- Script `postinstall` constrói bundle de produção
- Node.js 10.19.0 / npm 6.14.11 especificados nos engines
- Procfile pronto para deploy do backend