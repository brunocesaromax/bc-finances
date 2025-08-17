# BC Finances
[![NPM](https://img.shields.io/npm/l/express)](https://github.com/brunocesaromax/lancamentos/blob/master/LICENSE)

# Sobre o projeto

BC Finances é uma aplicação full stack web construída durante o acompanhamento do curso __Full Stack Angular and Spring__ da Algaworks.

A aplicação consiste no gerenciamento financeiro do usuário, através de lançamentos de débitos e créditos.

## Acesso 
Login: admin@algamoney.com   
Senha: admin

## Layout Web

![img1.png](docs/main-pages/img1.png)
![img2.png](docs/main-pages/img2.png)
![img3.png](docs/main-pages/img3.png)
![img4.png](docs/main-pages/img4.png)
![img5.png](docs/main-pages/img5.png)
![img6.png](docs/main-pages/img6.png)

## Modelo Conceitual

![Modelo Conceitual](docs/diagrams/class-diagram.png)

## Modelo de banco de dados

![Modelo bd](docs/diagrams/entity-diagram.png)

## Tecnologias utilizadas

### Back end
- Java 8
- Spring Boot 2.3.7
- JPA/ Hibernate
- PostgreSQL 16 (database: bc-finances | username: postgres | password: postgres)
- Maven 

### Front end
- HTML / SASS / JS / Typescript
- PrimeNg
- Angular 9

## Como executar o projeto

### Executando com Docker Compose (Recomendado)

Pré-requisitos: Docker e Docker Compose

  1 - Clonar o repositório: 
  
  ```bash 
  git clone https://github.com/brunocesaromax/lancamentos.git
  cd lancamentos
  ```
  
  2 - Subir os serviços (PostgreSQL + pgAdmin):
  
  ```bash 
  docker-compose up -d
  ```
  
  3 - Criar arquivo .env com as configurações (baseado no .env.example):
  
  ```bash 
  cp .env.example .env
  ```
  
  4 - Entrar na pasta do projeto back end e executar:
  
  ```bash 
  cd bc-finances-backend
  mvn spring-boot:run
  ```

### Back end (Desenvolvimento Local)

Pré-requisitos: Java 8, PostgreSQL 16

  1 - Clonar o repositório: 
  
  ```bash 
  git clone https://github.com/brunocesaromax/lancamentos.git
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

### Front end web

**Pré-requisitos:** Node.js 10.x, npm 6.x, Angular CLI

**IMPORTANTE:** O frontend requer Node.js versão 10 e npm compatível. Versões mais recentes podem causar incompatibilidade.

  1 - Clonar o repositório: 
  
  ```bash 
  git clone https://github.com/brunocesaromax/lancamentos.git
  ```
  
  
  2 - Entrar na pasta do projeto front end: 
  
  ```bash 
  cd bc-finances-frontend
  ```
  
  3 - Instalar dependências
  
  ```bash 
  npm install
  ```
  
  4 - Executar o projeto (DESENVOLVIMENTO):
  
  ```bash
  npx ng serve
  ```
  
  **Nota:** Use `npx ng serve` para desenvolvimento (conecta ao backend local). O comando `npm start` executa a versão de produção.
# Autor

Bruno César Vicente

 <a href="https://www.linkedin.com/in/bruno-cesar-vicente" target="_blank"><img src="https://img.shields.io/badge/-LinkedIn-%230077B5?style=for-the-badge&logo=linkedin&logoColor=white" target="_blank"></a> 
