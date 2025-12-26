**Descrição:** Inclusão de Checkstyle, teste unitário inicial, validação de migrations e pipeline de PR no GitHub Actions, com ajustes de padrões e documentação.

**O que foi feito:**
* Adicionados `.editorconfig` no backend e frontend para padronizar formatação por módulo.
* Implementado Checkstyle no backend com configurações faseadas (`checkstyle-config`).
* Criado teste unitário de controller usando MockMvc standalone, compatível com Spring Boot 4+.
* Adicionado script de validação de migrations no backend (padrão `VYYYYMMDDHHMM__descricao.sql`).
* Configurado workflow de PR no GitHub Actions com setup de Java 25 e build do frontend, incluindo filtros por `paths`.
* Ajustados imports explícitos para atender regras de Checkstyle.
* Adicionado badge de PR Checks no README.

**Testes recomendados:**
* Backend (Tests): `mvn -B test`
* Backend (Checkstyle): `mvn -B checkstyle:check -Dmaven.compile.skip=true`
* Backend (Migrations): `./script/validate-migrations.sh`
* Frontend (Build): `npm run build`

**Observações:**
* O workflow usa `actions/setup-java` com `corretto` para Java 25 e cache Maven.
* O script de migrations esta compatível com `sh`, evitando falhas por interpretador.
