# Diretrizes de Desenvolvimento

## Regras Fundamentais

- **`mvn clean compile` é obrigatório** antes de qualquer alteração. Execute novamente até obter sucesso absoluto.
- **Nunca** inicie aplicações ou serviços sem permissão explícita do usuário.
- Ao concluir uma tarefa parcial, pause e atualize `TODO.md` e `CHANGELOG.md` antes de seguir.

## Fluxo Backend (Spring Boot 2.3.7 / Java 8)

```bash
mvn clean compile            # obrigatório para validar a base do projeto
mvn spring-boot:run          # somente com autorização do usuário
mvn test                     # executar suites quando aplicável
mvn package                  # empacotar quando solicitado
mvn spring-boot:run -Dspring.profiles.active=dev
```

## Fluxo Frontend (Angular 9 / Node.js 10.x)

```bash
npm install                  # instalar dependências em bc-finances-frontend/
npx ng serve                 # desenvolvimento local (não use npm start)
ng build --prod              # builds de produção quando requisitado
ng test | ng lint | ng e2e   # qualidade e automação conforme necessidade
```

### Requisitos de Ambiente Frontend

- Node.js 10.x e npm 6.x são obrigatórios (`node --version`, `npm --version`).
- Chart.js, PrimeNG e @auth0/angular-jwt são dependências críticas – verifique compatibilidade antes de upgrades.

## Padrões Principais de Código

- **Backend**
  - Configuração de segurança via `AuthorizationServerConfig`, `CustomTokenEnhancer` e `CorsFilter`.
  - Entidades JPA em `/model` com uso disciplinado de Lombok.
  - Repositórios customizados em `/repository/query` usando Criteria API.
  - Services em `/service` com exceções específicas em `/service/exception`.
  - Integrações dedicadas para email (`/mail`) e armazenamento S3.

- **Frontend**
  - Modularização por domínio (launches, persons, categories, reports, dashboard).
  - Serviços centralizados para requisições HTTP e guards em `/security`.
  - Uso consistente de componentes compartilhados e padrões Angular.

## Princípios Essenciais

- SOLID e Clean Code aplicados rigorosamente.
- Cada classe ou módulo deve ter responsabilidade única.
- Seguir Aberto/Fechado, Substituição de Liskov, Segregação de Interface e Inversão de Dependência.

## Testes e Qualidade

- **Backend**: JUnit + Spring Boot Test, com foco em integração de repositórios e serviços.
- **Frontend**: Jasmine/Karma para unit tests, Protractor para e2e, TSLint obrigatório.
- Tenha certeza de que suites relevantes foram executadas após mudanças significativas.
- Credenciais padrão para verificação rápida: `admin@algamoney.com / admin` (utilize somente em ambientes de teste).

## Comentários e Formatação

- Comentários mínimos, apenas para explicar decisões ou regras de negócio complexas.
- Código deve ser autoexplicativo; evite comentários redundantes.
- Proibido uso de emoticons em código e documentações técnicas.
- Padronize formatação de acordo com convenções do projeto.
