# Notas do Desenvolvedor - Observabilidade com OpenObserve

**Branch:** 75-implementar-observabilidade-na-aplicação-utilizando-openobserve

**Descrição:** Instrumentação completa do backend e ajuste operacional para coletar 100% de logs, métricas e traces via OpenTelemetry, enviando-os ao OpenObserve dockerizado. Inclui documentação, novos handlers e melhorias no Dockerfile para builds mais rápidos.

---

## Principais Alterações

### Stack de Observabilidade
- Inclusão das dependências `opentelemetry-spring-boot-starter`, `micrometer-tracing-bridge-otel` e `opentelemetry-logback-appender` (BOM 2.20.0) no `pom.xml`.
- Configuração central em `application.yml`: sampling 1.0, exportadores OTLP (http/protobuf + gzip), propagators `tracecontext`/`baggage`, ativação das instrumentações de Spring MVC, JDBC, Redis e Micrometer.
- Manipulação homogênea de erros contendo `traceId` e `spanId` em `BcFinancesExceptionHandler` e `PersonExceptionHandler`.

### Infraestrutura Docker
- Novo serviço `openobserve` no `docker-compose.yml` (portas 5080/5081, volume `openobserve_data`, credenciais padrão `root@example.com / Complexpass#123`).
- Backend agora depende explicitamente do serviço OpenObserve e exporta OTLP com variáveis `OPENOBSERVE_*` / `OTEL_*` (header Authorization configurável).
- `Dockerfile` do backend utiliza BuildKit e cache do `/root/.m2` para acelerar `mvn dependency:go-offline`, descrevendo no README o comando recomendado `DOCKER_BUILDKIT=1 docker build -t bc-finances-backend ./bc-finances-backend`.

### Documentação & Ferramentas
- README atualizado com instruções de observabilidade (serviço OpenObserve, geração de tokens, comando BuildKit, endpoints úteis).
- Criação do diretório `docs/observability/` contendo resumo da stack, diagramas Mermaid, variáveis necessárias, exemplos de payloads de erro com IDs de trace/span e boas práticas.
- Atualização do TODO/CHANGELOG para refletir a nova infraestrutura e os handlers de erro.

---

## Decisões Importantes
1. **Sampling 100% (sempre-on)**: atende ao requisito do usuário para máxima visibilidade. Em produção pode ser ajustado via `OTEL_SAMPLING_PROBABILITY`.
2. **Credenciais padrão apenas local**: `.env` e `docker-compose` trazem valores default funcionais, porém documentação reforça troca por token de API (Basic `<org_id>:<token>`) em outros ambientes.
3. **Logs correlacionados**: uso do `OpenTelemetryAppender` garante visibilidade unificada no OpenObserve.
4. **Maven cache via BuildKit**: opção simples para builds rápidos sem introduzir scripts externos.

---

## Pontos de Atenção
- Sempre executar `mvn -o clean compile` antes de subir a stack para garantir que as dependências já estejam no cache offline (sandbox sem internet).
- O header `OTEL_EXPORTER_OTLP_HEADER_AUTHORIZATION` não deve conter aspas; use `Basic $(echo -n '<org>:<token>' | base64)`.
- OpenObserve precisa estar saudável antes do backend iniciar; caso contrário, logs mostrarão 401/connection errors até o serviço responder.
- O volume `openobserve_data` cresce com o tempo. Planejar retenção/ limpeza conforme políticas do projeto.
- Os trace/span IDs retornados ao cliente são sensíveis ao modo do OpenTelemetry; se `OTEL_SDK_DISABLED=true`, os campos virão `null`.

---

## Testes Recomendados
1. `mvn -o clean compile` para validar instrumentações e dependências.
2. `docker-compose up -d openobserve backend` seguido de chamadas à API; verificar na UI do OpenObserve se o stream `bcfinances` recebe traces/logs/métricas.
3. Induzir erros (ex.: `GET /persons/{id inexistente}`) e confirmar que a resposta contém `traceId`/`spanId` e que o mesmo ID aparece na busca do OpenObserve.
4. Executar `docker build` com e sem `DOCKER_BUILDKIT=1` para comprovar ganho de performance e documentar para a equipe.
5. Validar `OTEL_EXPORTER_OTLP_HEADER_AUTHORIZATION` personalizado em ambiente diferente do local, garantindo que tokens customizados funcionem.

---

## Pendências / Próximos Passos
- Definir retenção e dashboards padrão no OpenObserve (tempo de retenção, alertas, etc.).
- Considerar sampling adaptativo para produção caso o volume de dados cause custos elevados.
- Criar métricas customizadas para integrações críticas (ex.: S3, e-mail) usando Micrometer.
- Avaliar captura de traces no frontend (OpenTelemetry JS) para distribuir o rastreamento ponta a ponta.
