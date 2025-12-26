**Descrição:** Demanda principal de remoção de dependências não utilizadas no backend e frontend, com atualização de Java e Spring Boot aproveitada para limpeza e compatibilidade.

**O que foi feito:**
* Removidas dependências não utilizadas no backend após validação de build.
* Revisado frontend para dependências não utilizadas (sem remoções necessárias).
* Atualizada a configuração do Maven para garantir annotation processing do Lombok e compilação com Java 25.
* Atualizado o Spring Boot para 4.0.1 e alinhadas as documentações principais com as novas versões.
* Configurado `spring-dotenv` para ler o `.env` no diretório raiz por padrão.
* Ajustada a geração de URLs assinadas do S3 para usar `public-endpoint` quando definido.
* Refatorada a serialização do Redis para remover APIs depreciadas e manter compatibilidade com payloads antigos (ObjectMapper + DefaultTypeResolverBuilder).
* Corrigido warning de lint no formulário de transações do frontend.

**Testes recomendados:**
* Executar `mvn clean compile` usando o JDK 25 configurado no ambiente.
* Executar `npm run lint` no frontend para garantir lint limpo.
* Validar leitura/escrita de cache Redis (categorias e sessões) após atualização.

**Observações:** Avisos de Jansi/Unsafe aparecem durante o build do Maven e não afetam o funcionamento da aplicação.
