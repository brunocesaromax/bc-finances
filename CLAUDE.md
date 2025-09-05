# CLAUDE.md

Este arquivo fornece orientação para o Claude Code (claude.ai/code) ao trabalhar com código neste repositório.

## OBRIGATÓRIO: Leitura Inicial de Contexto

**A CADA INÍCIO DE INTERAÇÃO, DEVE-SE LER OBRIGATORIAMENTE:**

1. **README.md** - Visão geral e instruções básicas do projeto
2. **TODO.md** - Estado atual das tarefas e planejamento
3. **CHANGELOG.md** - Histórico de mudanças e decisões técnicas
4. **Diretório ./docs/diagrams** - Diagramas importantes do projeto
   5. Diagrama de classes
   6. Diagrama de entidade relacionamento

## 🚨 REGRA CRÍTICA: DOCUMENTAÇÃO OBRIGATÓRIA

**TODO.md e CHANGELOG.md DEVEM SER CRIADOS E ATUALIZADOS A CADA DEMANDA/BRANCH:**

- **TODO.md**: OBRIGATÓRIO no início de qualquer nova demanda/branch
  - Checar se existe no início, após primeira interação com usuário, caso não existir criar segundo a demanda passada
- **CHANGELOG.md**: OBRIGATÓRIO para registrar todas as mudanças
  - Checar se existe no início, após primeira interação com usuário, caso não existir criar segundo a demanda passada
- **Atualização contínua**: A cada tarefa concluída ou mudança significativa
  - Com termo 'tarefa' entenda: uma pequena etapa finalizada dentro da demanda maior passada, onde já se deve pausar a implementação 
  e atualizar o TODO.md e CHANGELOG.md, e aguardar até o usuário pedir para continuar
- **Foco estrito na branch atual**: Documentar EXCLUSIVAMENTE a demanda da branch ativa, sem incluir funcionalidades futuras ou outras branches
- **Prompt de reforço**: SEMPRE verificar se estes arquivos existem e estão atualizados

## Visão Geral do Projeto

BC Finances é uma aplicação full-stack de gerenciamento financeiro construída com Spring Boot (backend) e Angular 9 (frontend). O sistema gerencia entradas financeiras (lançamentos) com débitos e créditos, apresentando segurança OAuth2, armazenamento de arquivos AWS S3, notificações por email e geração de relatórios em PDF.

## Arquitetura

**Backend (bc-finances-backend/):**
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
# Comando obrigatório de build (SEMPRE executar para verificar compilação)
mvn clean compile

# Iniciar servidor de desenvolvimento (de bc-finances-backend/) - SOMENTE COM PERMISSÃO DO USUÁRIO
mvn spring-boot:run

# Executar testes
mvn test

# Empacotar aplicação
mvn package

# Executar com perfil específico
mvn spring-boot:run -Dspring.profiles.active=dev
```

### Desenvolvimento Frontend
**IMPORTANTE:** Requer Node.js 10.x e npm 6.x (verificar com `node --version` e `npm --version`)

```bash
# Instalar dependências (de bc-finances-frontend/)
npm install

# Iniciar servidor de desenvolvimento (OBRIGATÓRIO para desenvolvimento local)
npx ng serve

# ATENÇÃO: npm start executa versão de produção (server.js)
# Para desenvolvimento LOCAL sempre usar: npx ng serve

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
- Migrações Flyway em `/resources/db/migration` seguindo padrões específicos do projeto

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

## Padrões de Migrations (Flyway)

### Estrutura de Diretórios
```
src/main/resources/db/migration/
├── 2025/
│   ├── 08/
│   │   ├── 202508142205__create_categories_table.sql
│   │   ├── 202508142210__create_users_table.sql
│   │   └── 202508142215__create_transactions_table.sql
│   └── 09/
│       └── 202509011200__add_index_to_users.sql
└── 2026/
    └── 01/
        └── 202601151030__new_feature_migration.sql
```

### Nomenclatura de Arquivos
- **Formato obrigatório**: `YYYYMMDDHHMM__description.sql`
- **Ano/Mês/Dia/Hora/Minuto**: Timestamp exato da criação
- **Descrição**: Snake_case, descritiva e concisa
- **Exemplos**:
  - `202508142205__create_categories_table.sql`
  - `202508142210__add_email_index_to_users.sql`
  - `202508142215__alter_transactions_add_attachment_column.sql`

### Padrões de SQL
- **Palavras-chave SQL**: SEMPRE em UPPERCASE (`CREATE`, `TABLE`, `INSERT`, `SELECT`, `WHERE`, etc.)
- **Nomes de tabelas**: SEMPRE no plural (`users`, `categories`, `transactions`, `permissions`)
- **Nomes de colunas**: snake_case minúsculo (`user_id`, `created_at`, `full_name`)
- **Constraints**: Nomenclatura clara (`fk_transactions_user_id`, `idx_users_email`)

### Padrões de Tabelas
- **Nomes no plural**: `user` → `users`, `category` → `categories`, `launch` → `transactions`
- **Chaves primárias**: `id BIGSERIAL PRIMARY KEY`
- **Chaves estrangeiras**: `table_id BIGINT NOT NULL`
- **Timestamps**: `created_at TIMESTAMP DEFAULT NOW()`, `updated_at TIMESTAMP`

### Estrutura de Migration
```sql
-- Create users table with basic authentication fields
-- Date: 2025-08-14
-- Author: [Developer name]

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(150) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_users_email ON users(email);

INSERT INTO users (name, email, password) VALUES 
('Administrador', 'admin@algamoney.com', 'admin'),
('Maria Silva', 'maria@algamoney.com', 'maria');

-- ROLLBACK (SQL to undo this migration):
-- DROP TABLE IF EXISTS users;
-- DELETE FROM flyway_schema_history WHERE version = '202508142210';
```

### Comentários de Reversão Obrigatórios
**Toda migration DEVE terminar com comentários de reversão EM INGLÊS contendo:**
1. **SQL de reversão**: Comandos para desfazer completamente a migration
2. **Remoção do Flyway**: `DELETE FROM flyway_schema_history WHERE version = 'VERSION';`
3. **Ordem reversa**: Se criar tabela A depois B, reverter B depois A

### Regras Críticas
1. **NUNCA modificar migrations já executadas em produção**
2. **SEMPRE testar reversão em ambiente de desenvolvimento**
3. **Uma migration = uma responsabilidade** (criação de tabela, adição de coluna, etc.)
4. **Usar transações quando necessário** (`BEGIN; ... COMMIT;`)
5. **Validar dados antes de alterações destrutivas**

### Exemplos de Reversão
```sql
-- For CREATE TABLE:
-- DROP TABLE IF EXISTS table_name;

-- For ALTER TABLE ADD COLUMN:
-- ALTER TABLE table_name DROP COLUMN IF EXISTS column_name;

-- For INSERT:
-- DELETE FROM table_name WHERE condition;

-- For CREATE INDEX:
-- DROP INDEX IF EXISTS index_name;
```

### Padrões de Idioma
- **Código SQL**: Comentários e nomes em inglês
- **Documentação**: Português (README.md, CLAUDE.md, TODO.md, CHANGELOG.md)
- **Commits**: Português ou inglês (conforme padrão do projeto)

## Padrões de Desenvolvimento

### 🚨 REGRA CRÍTICA: PROIBIÇÃO DOCKER SYSTEM PRUNE
**NUNCA EXECUTAR:** `docker system prune -f` ou variações
- ⚠️ **EXTREMAMENTE PERIGOSO** - Remove TODOS os recursos Docker não utilizados
- 💥 **DESTRUTIVO** - Apaga imagens, containers, networks, volumes, cache
- 📦 **IMPACTO SEVERO** - Perde trabalho de outros projetos, re-downloads massivos
- 🕐 **LENTIDÃO** - Builds futuros muito mais lentos (cache perdido)

**Alternativas seguras:**
```bash
# Limpar apenas containers parados
docker container prune

# Limpar apenas imagens não utilizadas  
docker image prune

# Limpar apenas networks órfãos
docker network prune

# Ver espaço usado antes de limpar
docker system df
```

### 🚨 REGRA CRÍTICA DE BUILD
**COMANDO OBRIGATÓRIO:** `mvn clean compile`
- **SEMPRE executar** antes de qualquer tarefa ou mudança
- **Executar em loop** até compilação 100% sem erros
- **NUNCA iniciar aplicação** sem permissão explícita do usuário
- **TODA tarefa só é concluída** quando projeto compila sem erros

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
# TODO - BC Finances

## Branch Atual (nome-da-branch)
- [ ] Tarefas específicas da demanda da branch atual
- [ ] Atividades em desenvolvimento ativo da branch

## Bugs Conhecidos
- [ ] Problemas identificados pendentes relacionados à branch atual

## Concluído
- [x] Tarefas finalizadas da branch atual (atualizadas conforme progresso)
```

**IMPORTANTE:** 
- Focar APENAS na branch/demanda atual, não em funcionalidades futuras
- Seção "Concluído" é preenchida progressivamente conforme tarefas são finalizadas
- NÃO incluir seções de "Próximas Funcionalidades" - manter foco na demanda ativa

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
- `bc-finances-backend/TODO.md` e `lancamentos-ui/TODO.md` (se necessário)
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