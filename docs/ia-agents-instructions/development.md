# Diretrizes de Desenvolvimento

## Regras Fundamentais

- **`mvn clean compile` é obrigatório** antes de qualquer alteração. Execute novamente até obter sucesso absoluto.
- **Nunca** inicie aplicações ou serviços sem permissão explícita do usuário.
- Ao concluir uma tarefa parcial, pause e atualize `TODO.md` e `CHANGELOG.md` antes de seguir.

## Fluxo Backend (Spring Boot 3.5.4 / Java 21)

```bash
mvn clean compile              # obrigatório para validar a base do projeto
mvn spring-boot:run            # somente com autorização do usuário
mvn test                       # executar suites quando aplicável
mvn package                    # empacotar quando solicitado
mvn spring-boot:run -Dspring.profiles.active=dev
```

## Fluxo Frontend (React 19 / Vite / Node.js 20+)

```bash
npm install                    # instalar dependências em bc-finances-frontend/
npm run dev                    # desenvolvimento local
npm run build                  # build de produção
npm run preview                # pré-visualização do bundle gerado
npm run lint                   # verifica estilo e padrões via ESLint
```

### Requisitos de Ambiente Frontend

- Node.js 20 LTS (ou superior) e npm 10+ (`node --version`, `npm --version`).
- Projeto baseado em React 19 + Vite 7 + Tailwind CSS 4.
- Utiliza Axios, React Hook Form, Zod, React Router DOM e React Hot Toast.

## Padrões Principais de Código

- **Backend**
  - Configuração de segurança via `AuthorizationServerConfig`, `CustomTokenEnhancer` e `CorsFilter`.
  - Entidades JPA em `/model` com uso disciplinado de Lombok.
  - Repositórios customizados em `/repository/query` usando Criteria API.
  - Services em `/service` com exceções específicas em `/service/exception`.
  - Integrações dedicadas para email (`/mail`) e armazenamento S3.

- **Frontend**
  - Organização modular por domínio (`/pages`, `/components`, `/services`, `/contexts`, `/hooks`).
  - Serviços centralizados em `src/services` com Axios e interceptadores de autenticação.
  - Contexto global de Auth e rotas protegidas via React Router.
  - Componentes shared estilizados com Tailwind CSS; manter coerência de design tokens definidos em `src/index.css`.

## Princípios Essenciais

- SOLID e Clean Code aplicados rigorosamente.
- Cada classe ou módulo deve ter responsabilidade única.
- Seguir Aberto/Fechado, Substituição de Liskov, Segregação de Interface e Inversão de Dependência.

## Testes e Qualidade

- **Backend**: JUnit + Spring Boot Test, com foco em integração de repositórios e serviços.
- **Frontend**: Utilizar ESLint (`npm run lint`) e considerar Vitest/Testing Library em novos testes.
- Tenha certeza de que suites relevantes foram executadas após mudanças significativas.
- Credenciais padrão para verificação rápida: `admin@algamoney.com / admin` (utilize somente em ambientes de teste).

## Comentários e Formatação

- Comentários mínimos, apenas para explicar decisões ou regras de negócio complexas.
- Código deve ser autoexplicativo; evite comentários redundantes.
- Proibido uso de emoticons em código e documentações técnicas.
- Padronize formatação de acordo com convenções do projeto.
