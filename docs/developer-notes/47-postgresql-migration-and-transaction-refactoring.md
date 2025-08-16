# Notas do Desenvolvedor - Branch: 47-alterar-para-banco-de-dados-postgresql

**Data:** 2025-08-15  
**Descrição:** Migração para PostgreSQL 16 e refatoração completa de Launch para Transaction

## Estado Atual do Projeto

### Migração de Banco de Dados: MySQL → PostgreSQL 16

O projeto passou por uma migração completa de MySQL para PostgreSQL 16, incluindo:

#### Configuração de Infraestrutura
- **Docker Compose** configurado com PostgreSQL 16 + pgAdmin
- **Porta PostgreSQL:** 5435 (para evitar conflitos)
- **pgAdmin:** http://localhost:8081 (admin@lancamentos.com / admin)
- **Database:** bc-finances (postgres / postgres)

#### Configuração da Aplicação
- **Dependência Maven:** mysql-connector-java → postgresql
- **spring-dotenv:** Configuração baseada em variáveis de ambiente (.env)
- **Flyway:** Configurado para ignorar migrações ausentes e desabilitar validação na migração

### Refatoração Arquitetural: Launch → Transaction

#### Motivação
Alteração do nome de entidade "Launch" para "Transaction" por ser mais adequado semanticamente para transações financeiras no contexto internacional.

#### Impacto da Refatoração
```
Launch → Transaction (entidade principal)
LaunchController → TransactionController
Transactionservice → TransactionService
LaunchRepository → TransactionRepository
LaunchRepositoryQuery → TransactionRepositoryQuery
LaunchRepositoryImpl → TransactionRepositoryImpl
LaunchFilter → TransactionFilter
Transactionsummary → TransactionSummary
Transactionstatistic* → TransactionStatistic* (3 DTOs)
LaunchAttachmentListener → TransactionAttachmentListener
LaunchExceptionHandler → TransactionExceptionHandler
PersonExistentInLaunchException → PersonExistentInTransactionException
/Transactions → /transactions (endpoint)
```

## Arquitetura Atual

### Stack Tecnológico
```
Backend:
- Spring Boot 2.3.7 + Java 8
- PostgreSQL 16 com Docker
- OAuth2 + JWT (duplo cliente: web + mobile)
- JasperReports para PDF
- AWS S3 para anexos
- Email com Thymeleaf templates
- Flyway para migrações

Frontend:
- Angular 9 + PrimeNG
- OAuth2 com @auth0/angular-jwt
- Chart.js para dashboards
- Express server para Heroku deploy
```

### Entidades e Relacionamentos

#### Entidade Principal: Transaction
```java
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull private String description;
    @NotNull @Column(name = "due_date") private LocalDate dueDate;
    @Column(name = "payday") private LocalDate payday;
    @NotNull private BigDecimal value;
    private String observation;
    
    @NotNull @Enumerated(EnumType.STRING) private TypeLaunch type; // RECEITA/DESPESA
    
    @NotNull @ManyToOne @JoinColumn(name = "category_id") private Category category;
    @NotNull @ManyToOne @JoinColumn(name = "person_id") private Person person;
    
    private String attachment; // S3 key
    @Transient private String urlAttachment; // URL completa gerada dinamicamente
}
```

#### Entidades de Apoio
```java
// Todas as tabelas usam nomes no plural
@Entity @Table(name = "categories") class Category
@Entity @Table(name = "persons") class Person  
@Entity @Table(name = "users") class User
@Entity @Table(name = "permissions") class Permission
@Entity @Table(name = "contacts") class Contact
@Entity @Table(name = "states") class State
@Entity @Table(name = "cities") class City
```

### Padrões de Migrations (Flyway)

#### Estrutura de Diretórios
```
src/main/resources/db/migration/
├── 2025/08/
│   ├── V202508151800__create_categories_table.sql
│   ├── V202508151805__create_persons_table.sql
│   ├── V202508151810__create_transactions_table.sql
│   ├── V202508151815__create_users_and_permissions_tables.sql
│   ├── V202508151820__create_contacts_table.sql
│   ├── V202508151825__create_states_and_cities_tables.sql
│   └── V202508151830__add_attachment_column_to_transactions.sql
```

#### Nomenclatura
- **Formato:** `V{YYYYMMDDHHMM}__{description}.sql`
- **SQL:** Keywords em UPPERCASE, tabelas no plural, colunas em snake_case
- **Reversão:** Comentários obrigatórios em inglês ao final de cada migration

#### Exemplo de Migration
```sql
-- Create transactions table for financial transactions
-- Date: 2025-08-15 18:10
-- Author: Bruno César

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(50) NOT NULL,
    due_date DATE NOT NULL,
    payday DATE,
    value DECIMAL(10,2) NOT NULL,
    observation VARCHAR(100),
    type VARCHAR(20) NOT NULL,
    category_id BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    CONSTRAINT fk_transactions_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT fk_transactions_person FOREIGN KEY (person_id) REFERENCES persons(id)
);

-- ROLLBACK (SQL to undo this migration):
-- DROP TABLE IF EXISTS transactions;
-- DELETE FROM flyway_schema_history WHERE version = '202508151810';
```

## Configuração de Segurança

### OAuth2 Duplo Cliente
```java
// Web Client
.withClient(apiProperty.getSecurity().getFrontEndClient())
.secret("{noop}" + apiProperty.getSecurity().getFrontEndPassword())
.scopes("read", "write")
.authorizedGrantTypes("password", "refresh_token")
.accessTokenValiditySeconds(1800)
.refreshTokenValiditySeconds(3600 * 24)

// Mobile Client  
.withClient(apiProperty.getSecurity().getMobileClient())
.secret("{noop}" + apiProperty.getSecurity().getMobilePassword())
.scopes("read")
.authorizedGrantTypes("password", "refresh_token")
.accessTokenValiditySeconds(1800)
.refreshTokenValiditySeconds(3600 * 24)
```

### Token Enhancement
- **CustomTokenEnhancer:** Adiciona informações customizadas ao JWT
- **Signing Key:** "algaworks" (deve ser alterado em produção)
- **Refresh Token:** Não reutilização habilitada

## Funcionalidades Principais

### TransactionService - Operações de Negócio
```java
// CRUD básico
Page<Transaction> findAll(TransactionFilter filter, Pageable pageable)
Optional<Transaction> findById(Long id)
Transaction save(Transaction transaction)
Transaction update(Long id, Transaction transaction)
void delete(Long id)

// Funcionalidades específicas
Page<TransactionSummary> sumUp(TransactionFilter filter, Pageable pageable)
byte[] reportByPerson(LocalDate start, LocalDate end) // PDF com JasperReports
List<TransactionStatisticCategory> findByCategory(LocalDate date)
List<TransactionStatisticByDay> findByDay(LocalDate date)

// Integração AWS S3
- Upload automático de anexos no save()
- Update/delete de arquivos no update()
- Geração de URLs pré-assinadas

// Agendamento
@Scheduled(cron = "0 0 6 * * *") // 6h da manhã diariamente
alertOverdueTransactions() // Email para usuários com ROLE_SEARCH_TRANSACTION
```

### Repositórios Customizados
```java
// Interface para consultas customizadas
TransactionRepositoryQuery extends JpaRepository<Transaction, Long>

// Implementação com Criteria API  
TransactionRepositoryImpl implements TransactionRepositoryQuery
- filterOut(TransactionFilter filter, Pageable pageable)
- sumUp(TransactionFilter filter, Pageable pageable)
- findByPerson(LocalDate start, LocalDate end)
- findByCategory(LocalDate date)
- findByDay(LocalDate date)
```

### Sistema de Anexos
```java
@EntityListeners(TransactionAttachmentListener.class)
class Transaction {
    private String attachment; // S3 key
    @Transient private String urlAttachment; // URL gerada
}

// Listener para gerar URLs automaticamente
@PostLoad @PostPersist @PostUpdate
public void configureAttachmentUrl(Transaction transaction) {
    if (StringUtils.hasText(transaction.getAttachment())) {
        transaction.setUrlAttachment(s3.generateUrl(transaction.getAttachment()));
    }
}
```

## Variáveis de Ambiente (.env)

### Configuração de Banco
```properties
DB_URL=jdbc:postgresql://localhost:5435/bc-finances
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

### Configuração de Email
```properties
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

### AWS S3
```properties
AWS_S3_ACCESS_KEY_ID=your-access-key
AWS_S3_SECRET_ACCESS_KEY=your-secret-key
AWS_S3_BUCKET=your-bucket-name
```

### OAuth2 Clients
```properties
FRONT_END_CLIENT=angular
FRONT_END_PASSWORD=@ngul@r0
MOBILE_CLIENT=mobile
MOBILE_PASSWORD=m0b1l30
```

## Comandos de Desenvolvimento

### Build e Execução
```bash
# Build obrigatório (sempre verificar compilação)
cd bc-finances-backend && mvn clean compile

# Iniciar aplicação (somente com permissão do usuário)
mvn spring-boot:run

# Testes
mvn test

# Docker (banco de dados)
docker-compose up -d
docker-compose ps
docker-compose down
```

### Frontend
```bash
cd lancamentos-ui
npm install
ng serve
ng build --prod
ng test
ng lint
```

## Testes e Validação

### Status Atual - Build Success
- ✅ **Compilação:** mvn clean compile executado com sucesso
- ⚠️ **Warnings:** Uso de API deprecated em TransactionService (não crítico)
- ✅ **Migrations:** Todas as 7 migrations criadas e estruturadas
- ✅ **Entidades:** Todas atualizadas para nomes de tabelas plurais

### Testes Recomendados
```bash
# Validar funcionamento completo
1. Testar conexão PostgreSQL (docker-compose up -d)
2. Executar aplicação e verificar migrations
3. Testar endpoints /transactions via API
4. Validar upload de anexos (S3)
5. Testar geração de relatórios PDF
6. Verificar email de lançamentos vencidos
7. Testar autenticação OAuth2 (web + mobile)
```

## Decisões Arquiteturais Importantes

### 1. Nomenclatura de Entidades
**Decisão:** Usar nomes no plural para tabelas PostgreSQL
**Motivo:** Padrão mais comum em PostgreSQL e facilita mapeamento JPA

### 2. Migrations com Timestamp
**Decisão:** Formato YYYYMMDDHHMM__ ao invés de V01__, V02__
**Motivo:** Evita conflitos em desenvolvimento paralelo e facilita organização temporal

### 3. Refatoração Launch → Transaction  
**Decisão:** Renomear toda estrutura de Launch para Transaction
**Motivo:** Semântica mais adequada para contexto de transações financeiras

### 4. PostgreSQL na porta 5435
**Decisão:** Usar porta não-padrão para PostgreSQL
**Motivo:** Evitar conflitos com instalações locais de PostgreSQL

### 5. Duplo Cliente OAuth2
**Decisão:** Manter dois clientes (web + mobile) com scopes diferentes
**Motivo:** Separação de responsabilidades e segurança granular

## Problemas Conhecidos e Melhorias

### Warnings Identificados
1. **API Deprecated:** TransactionService usa APIs deprecated do Java 8
2. **Hibernate Validator:** Relocação de dependência (warning não crítico)

### Frontend Legacy
- **Angular 9:** Versão desatualizada (considerar upgrade futuro)
- **Node.js 10.19.0:** Versão EOL (upgrade recomendado)

### Melhorias Futuras
1. **Migrar para Spring Boot 3.x + Java 17**
2. **Atualizar Angular para versão LTS atual**
3. **Implementar cache Redis para queries frequentes**
4. **Adicionar métricas com Micrometer + Prometheus**
5. **Implementar observabilidade completa (logs estruturados)**

## Documentação Relacionada

- **TODO.md:** Estado atual das tarefas da branch
- **CHANGELOG.md:** Histórico detalhado das mudanças
- **CLAUDE.md:** Instruções para assistentes IA
- **README.md:** Visão geral e comandos básicos
- **./docs/diagrams/:** Diagramas de classe e entidade
- **./docs/main-pages/:** Screenshots da aplicação

---

**Observação:** Esta documentação reflete o estado do projeto na branch `47-alterar-para-banco-de-dados-postgresql` em 2025-08-15. A refatoração de Launch para Transaction está completa e o projeto compila com sucesso, pronto para testes de funcionalidade completa.