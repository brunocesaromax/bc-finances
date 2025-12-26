# Arquitetura do Projeto

## Visão Geral

BC Finances é um sistema full-stack para gerenciamento financeiro, combinando:

- **Backend**: Spring Boot 4.0.1 (Java 25) com autenticação OAuth2/JWT, integração AWS S3 e notificações via e-mail.
- **Frontend**: React 19 (Vite + Tailwind) com gestão de autenticação via contexto, Axios e React Router.

Os diagramas atualizados residem em `docs/diagrams` (classes e entidade-relacionamento) e devem ser consultados a cada nova demanda.

## Componentes Backend

- Camada de domínio em `model` com entidades JPA.
- Repositórios customizados em `repository/query` utilizando Criteria API.
- Serviços em `service`, com tratamento de exceções dedicado em `service/exception`.
- Infraestrutura adicional em `mail` (templates Thymeleaf) e integrações AWS.

## Componentes Frontend

- Estrutura modular por domínio em `src/pages`, com layout compartilhado em `src/components`.
- `src/contexts/AuthContext` provê controle de sessão e permissões.
- Hooks utilitários (`src/hooks`) concentram lógicas reutilizáveis (ex.: debounce, autenticação).
- Serviços HTTP em `src/services` usam Axios com interceptadores de token e cancelamento.
- Estilização via Tailwind CSS 4 e design tokens declarados em `src/index.css`.

## Modelagem de Dados

Entidades principais:

- `Launch`: transações financeiras com valor, tipo (débito/crédito), datas, vínculos a pessoa e categoria.
- `Person`: responsáveis pelas movimentações, com dados de contato e endereço.
- `Category`: classificação de lançamentos.
- `User`: usuários da plataforma com perfis e permissões.
- `State` / `City`: apoio a dados geográficos.

Consulte o diagrama entidade-relacionamento para detalhes de cardinalidade e chaves.

## Guidelines Arquiteturais

- Utilize princípios de Clean Architecture ao introduzir novas funcionalidades.
- Preserve limites de módulos, evitando dependências cíclicas entre camadas.
- Qualquer alteração arquitetural deve ser previamente documentada e aprovada via TODO/CHANGELOG.
