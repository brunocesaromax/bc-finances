# Lançamentos
[![NPM](https://img.shields.io/npm/l/express)](https://github.com/brunocesaromax/lancamentos/blob/master/LICENSE)

# Sobre o projeto

Lançamentos é uma aplicação full stack web construída durante o acompanhamento do curso __Full Stack Angular and Spring__ da Algaworks.

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
- MySql (database: lancamentos | username: root  | password: root)
- Maven 

### Front end
- HTML / SASS / JS / Typescript
- PrimeNg
- Angular 9

## Como executar o projeto

### Back end

Pré-requisitos: Java 8

  1 - Clonar o repositório: 
  
  ```bash 
  git clone https://github.com/brunocesaromax/lancamentos.git
  ```
  
  
  2 - Entrar na pasta do projeto back end: 
  
  ```bash 
  cd lancamentos-api
  ```
  
  3 - Executar o projeto:
  
  
  ```bash 
  ./mvnw spring-boot:run
  ```

### Front end web

Pré-requisitos: npm / node / angular-cli 

  1 - Clonar o repositório: 
  
  ```bash 
  git clone https://github.com/brunocesaromax/lancamentos.git
  ```
  
  
  2 - Entrar na pasta do projeto front end: 
  
  ```bash 
  cd lancamentos-ui
  ```
  
  3 - Instalar dependências
  
  ```bash 
  npm install
  ```
  
  4 - Executar o projeto:
  
  
  ```bash
  ng serve
  ```
# Autor

Bruno César Vicente

 <a href="https://www.linkedin.com/in/bruno-cesar-vicente" target="_blank"><img src="https://img.shields.io/badge/-LinkedIn-%230077B5?style=for-the-badge&logo=linkedin&logoColor=white" target="_blank"></a> 
