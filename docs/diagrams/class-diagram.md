# Diagrama de Classes - BC Finances

## Visão Geral

Este diagrama representa a estrutura das principais entidades do sistema BC Finances e seus relacionamentos.

```mermaid
classDiagram
    %% Entidades Principais
    class User {
        +Long id
        +String name
        +String email
        +String password
        +List~Permission~ permissions
        +Boolean active
    }

    class Permission {
        +Long id
        +String description
    }

    class Person {
        +Long id
        +String name
        +String email
        +Boolean active
        +Address address
        +List~Contact~ contacts
    }

    class Contact {
        +Long id
        +String name
        +String email
        +String phone
        +Person person
    }

    class Address {
        +Long id
        +String street
        +String number
        +String complement
        +String neighborhood
        +String zipCode
        +City city
    }

    class City {
        +Long id
        +String name
        +State state
    }

    class State {
        +Long id
        +String name
    }

    class Category {
        +Long id
        +String name
    }

    class Transaction {
        +Long id
        +String description
        +LocalDate dueDate
        +LocalDate payday
        +BigDecimal value
        +String observation
        +TypeTransaction type
        +String attachment
        +String urlAttachment
        +Category category
        +Person person
    }

    class TypeTransaction {
        <<enumeration>>
        +RECEITA
        +DESPESA
    }

    User "*" --> "*" Permission : user_permissions
    Person "1" --> "*" Contact : has
    Person "1" --> "1" Address : has
    Address "*" --> "1" City : belongs_to
    City "*" --> "1" State : belongs_to
    Transaction "*" --> "1" Category : belongs_to
    Transaction "*" --> "1" Person : belongs_to
    Transaction "*" --> "1" TypeTransaction : has_type
```

## Relacionamentos

### User ↔ Permission (N:N)
- Um usuário pode ter múltiplas permissões
- Uma permissão pode ser atribuída a múltiplos usuários
- Tabela de junção: `user_permissions`

### Person ↔ Contact (1:N)
- Uma pessoa pode ter múltiplos contatos
- Cada contato pertence a uma única pessoa

### Person ↔ Address (1:1)
- Uma pessoa possui um endereço
- Um endereço pertence a uma única pessoa

### Address ↔ City (N:1)
- Múltiplos endereços podem estar na mesma cidade
- Cada endereço pertence a uma única cidade

### City ↔ State (N:1)
- Múltiplas cidades podem estar no mesmo estado
- Cada cidade pertence a um único estado

### Transaction ↔ Category (N:1)
- Múltiplas transações podem ter a mesma categoria
- Cada transação pertence a uma única categoria

### Transaction ↔ Person (N:1)
- Múltiplas transações podem estar associadas à mesma pessoa
- Cada transação está associada a uma única pessoa

## Principais Características

### Entidade User
- **Autenticação**: Email e senha criptografada
- **Autorização**: Lista de permissões para controle de acesso
- **Status**: Campo `active` para ativar/desativar usuários

### Entidade Transaction
- **Tipos**: RECEITA ou DESPESA (enum TypeTransaction)
- **Datas**: Data de vencimento e data de pagamento
- **Anexos**: Suporte a upload de arquivos (attachment/urlAttachment)
- **Categorização**: Associação obrigatória com categoria

### Entidade Person
- **Contatos**: Múltiplos meios de contato por pessoa
- **Endereço**: Endereço completo com cidade e estado
- **Status**: Campo `active` para controle de pessoas ativas

### Hierarquia Geográfica
- **Estado → Cidade → Endereço**: Estrutura hierárquica para localização
- **Normalização**: Evita duplicação de dados geográficos