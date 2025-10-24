# Arquitetura do Projeto

## Visão Geral

BC Finances é um sistema full-stack para gerenciamento financeiro, combinando:

- **Backend**: Spring Boot 2.3.7 (Java 8) com autenticação OAuth2/JWT, JasperReports para PDFs, integração AWS S3 e notificações via e-mail.
- **Frontend**: Angular 9 com PrimeNG, Chart.js e gestão de tokens com `@auth0/angular-jwt`.

Os diagramas atualizados residem em `docs/diagrams` (classes e entidade-relacionamento) e devem ser consultados a cada nova demanda.

## Componentes Backend

- Camada de domínio em `model` com entidades JPA.
- Repositórios customizados em `repository/query` utilizando Criteria API.
- Serviços em `service`, com tratamento de exceções dedicado em `service/exception`.
- Infraestrutura adicional em `mail` (templates Thymeleaf) e integrações AWS.

## Componentes Frontend

- Módulos organizados por contexto funcional (lançamentos, pessoas, categorias, relatórios, dashboard).
- Módulo compartilhado para componentes reutilizáveis.
- Guards e serviços de segurança em `security/`.
- Serviços especializados para comunicação com a API, respeitando padrões Angular.

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
