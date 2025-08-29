# Notas do Desenvolvedor - Dockerização da Aplicação BC Finances

**Descrição:** Implementação completa de containerização Docker para aplicação BC Finances, incluindo backend Spring Boot, frontend Angular e infraestrutura de banco de dados PostgreSQL com pgAdmin.

**O que foi feito:**

* **Dockerização do Backend Spring Boot:**
  - Criado Dockerfile multi-stage com OpenJDK 21 (Alpine) para build e runtime otimizados
  - Implementado usuário não-root (bcfinances) para segurança em container
  - Configurado health check usando Spring Boot Actuator endpoint `/actuator/health`
  - Adicionada dependência `spring-boot-starter-actuator` ao pom.xml
  - Atualizada versão do spring-dotenv de 2.3.0 para 4.0.0 para compatibilidade
  - Criado `.dockerignore` para otimização do contexto de build
  - Configurado JVM tuning específico para containers com `UseContainerSupport` e `MaxRAMPercentage=75.0`

* **Dockerização do Frontend Angular:**
  - Criado Dockerfile multi-stage com Node.js 10.19 (Alpine) para build e Nginx 1.21 para runtime
  - Implementada configuração Nginx customizada (`nginx-full.conf`) com proxy reverso para API
  - Configurado proxy `/api/*` para backend interno `bc-finances-backend:8080`
  - Adicionados headers de segurança (X-Frame-Options, X-Content-Type-Options, X-XSS-Protection)
  - Implementada compressão Gzip para assets estáticos
  - Configurado cache para recursos estáticos (1 ano) e SPA routing
  - Criado `.dockerignore` para contexto de build otimizado
  - Health check usando wget no endpoint raiz

* **Orquestração Docker Compose:**
  - Expandido docker-compose.yml para incluir backend e frontend além da infraestrutura existente
  - Configurada rede interna `bc-finances-network` para comunicação entre containers
  - Implementados volumes persistentes para logs do backend (`backend_logs`)
  - Configuradas dependências corretas entre serviços (frontend → backend → postgres)
  - Definidas políticas de restart `unless-stopped` para backend e frontend

* **Refatoração de Configuração:**
  - Removida classe utilitária estática `getBean()` de `BcFinancesApplication` 
  - Refatorado `TransactionAttachmentListener` para injeção de dependência via Spring (`@Component` + `@RequiredArgsConstructor`)
  - Simplificada configuração `application-dev.yml` com variáveis de ambiente diretas
  - Removido arquivo `application-prod.yml` (configuração específica de produção Heroku não aplicável ao Docker)
  - Movidas configurações JPA e Jackson para `application.yml` base
  - Configurado endpoint `/actuator/health` como público em `JwtSecurityConfig`
  - Adicionada configuração Spring Boot Actuator com exposição limitada de endpoints

* **Atualização de Documentação:**
  - Expandido README.md com instruções completas de Docker
  - Adicionados comandos Docker úteis para desenvolvimento (logs, rebuild, cleanup)
  - Documentada opção de execução apenas do banco de dados
  - Incluídos endpoints de acesso para todos os serviços containerizados
  - Refinamento de instruções do CLAUDE.md para maior precisão na documentação de branches

**Testes recomendados:**

* **Cenários críticos de containerização:**
  - Verificar inicialização correta de todos os containers via `docker-compose up -d`
  - Testar health checks dos containers backend e frontend
  - Validar conectividade de rede entre frontend, backend e banco de dados
  - Confirmar persistência de dados PostgreSQL após restart dos containers
  - Testar proxy reverso Nginx funcionando corretamente para chamadas da API

* **Cenários de integração:**
  - Autenticação OAuth2 funcionando através do proxy containerizado
  - Upload e download de arquivos S3 através do backend containerizado
  - Envio de emails via backend containerizado
  - Geração de relatórios PDF funcionando em ambiente container
  - Migrações Flyway executando corretamente na inicialização

* **Cenários de performance e segurança:**
  - Verificar compressão Gzip funcionando para assets do frontend
  - Validar headers de segurança sendo aplicados pelo Nginx
  - Testar cache de recursos estáticos (verificar headers Cache-Control)
  - Monitorar uso de memória dos containers com configuração JVM ajustada
  - Verificar logs centralizados funcionando para todos os serviços

**Observações:** 

- A aplicação agora roda completamente containerizada com comunicação interna otimizada
- Configuração de produção removida (application-prod.yml) - deve ser recriada se necessário deploy em Heroku
- Frontend configurado para desenvolvimento (usa environment.ts ao invés de environment.prod.ts)
- Dependências de ambiente externo (AWS S3, SMTP) ainda precisam ser configuradas via variáveis de ambiente
- Logs do backend são persistidos em volume Docker, mas logs do frontend/Nginx ficam apenas no container
- Configuração Nginx permite extensão futura para múltiplos backends via load balancing
- Health checks configurados permitem integração futura com orquestradores como Kubernetes