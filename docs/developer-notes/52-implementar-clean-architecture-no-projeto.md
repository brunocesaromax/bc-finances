# Notas do Desenvolvedor - Clean Architecture Migration

**Branch:** 52-implementar-clean-architecture-no-projeto

**Descrição:** Implementação completa da Clean Architecture no projeto BC Finances, reorganizando toda a estrutura de código em camadas bem definidas (domain, application, infrastructure, presentation) com separação clara de responsabilidades e inversão de dependências.

**O que foi feito:**

* **Reestruturação completa da arquitetura de módulos**: Reorganização de todos os módulos (authentication, persons, transactions, categories, shared) seguindo os padrões da Clean Architecture com camadas domain, application, infrastructure e presentation
* **Migração do sistema de autenticação**: Movimentação das classes de segurança OAuth2/JWT de `br.com.bcfinances.configuration` e `br.com.bcfinances.security` para o módulo `auth` com estrutura Clean Architecture
* **Implementação de contratos de domínio**: Criação de interfaces de repositório no domínio (`UserRepository`, `PersonRepository`, `CategoryRepository`, `TransactionRepository`) com implementações na camada de infraestrutura
* **Refatoração de entidades JPA**: Separação entre entidades de domínio puras (sem anotações JPA) e entidades de persistência (`*Entity`) na camada de infraestrutura
* **Criação de Use Cases**: Implementação de casos de uso na camada de aplicação (`LoginUseCase`, `CreateCategoryUseCase`, `ListCategoriesUseCase`, `UpdateCategoryUseCase`, `DeleteCategoryUseCase`, etc.)
* **Implementação de Mappers**: Criação de mapeadores entre DTOs, entidades de domínio e entidades de persistência (`AuthMapper`, `CategoryMapper`, `PersonMapper`, `TransactionMapper`)
* **Reorganização de controllers**: Movimentação dos controllers para a camada de apresentação com refatoração para usar Use Cases
* **Configuração de beans de infraestrutura**: Criação de classes de configuração para injeção de dependência de serviços de domínio (`JwtServiceImpl`, `AuthenticationServiceImpl`)
* **Atualização de imports e referências**: Ajuste de todos os imports e referências de classes que foram movidas para nova estrutura
* **Configuração de propriedades**: Movimentação da classe `ApiProperty` para o módulo shared/infrastructure/config
* **Documentação da arquitetura**: Criação de documentação completa em `docs/architecture/clean-architecture.md` com diagramas Mermaid explicando a nova estrutura

**Testes recomendados:**

* **Testes de autenticação completos**: Validar login, geração de JWT, validação de tokens e autorização de endpoints com as novas classes do módulo auth
* **Testes de CRUD de todas as entidades**: Verificar se operações de criação, listagem, atualização e exclusão de Categories, Persons e Transactions continuam funcionando corretamente
* **Testes de persistência**: Validar se os mapeamentos entre entidades de domínio e entidades JPA estão corretos e se não há problemas de lazy loading
* **Testes de integração dos endpoints**: Verificar se todos os controllers estão respondendo corretamente com a nova estrutura
* **Testes de injeção de dependência**: Validar se o Spring está conseguindo injetar corretamente todas as dependências com a nova configuração
* **Testes de build e compilação**: Executar `mvn clean compile` para verificar se não há problemas de compilação
* **Testes de inicialização da aplicação**: Verificar se a aplicação sobe sem erros de configuração ou beans não encontrados
* **Testes de funcionalidades transversais**: Validar se features como envio de email, upload de arquivos S3 e geração de relatórios continuam funcionando
* **Testes de permissões e autorização**: Verificar se o sistema de roles e permissões continua funcionando com as novas classes UserSession e AppUserDetailsService

**Observações:** 

* **Quebra de compatibilidade temporária**: Durante a migração, podem ocorrer problemas de referências não atualizadas ou configurações incompletas que necessitam ajustes pontuais
* **Dependência de testes extensivos**: A reestruturação massiva do código requer validação completa de todas as funcionalidades para garantir que nada foi quebrado
* **Configuração de beans pode precisar ajustes**: Algumas dependências podem precisar de configuração manual no Spring devido à nova estrutura de pacotes
* **Performance pode ser impactada**: A nova arquitetura com mais camadas pode introduzir overhead que precisa ser monitorado
* **Documentação técnica ainda em evolução**: A documentação da nova arquitetura pode precisar de atualizações conforme descobertas durante os testes