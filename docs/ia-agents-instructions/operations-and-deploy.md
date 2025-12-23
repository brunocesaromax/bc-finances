# Operações, Ambiente e Deploy

## Configuração de Ambiente

- Utilize perfis Spring apropriados: `dev` (desenvolvimento com PostgreSQL local), `prod`, `oauth-security` (padrão) e `basic-security` (fallback HTTP Basic).
- Arquivos `.env` são obrigatórios e devem conter credenciais e configurações sensíveis.
- Certifique-se de que Redis, PostgreSQL e demais serviços externos estejam acessíveis antes de iniciar a aplicação.

## Docker Compose

```bash
docker-compose up -d      # subir todos serviços (PostgreSQL, Redis, OpenObserve, LocalStack, backend e frontend)
docker-compose ps         # verificar status
docker-compose down       # encerrar serviços
```

- Acesse o pgAdmin em `http://localhost:8081` com `admin@lancamentos.com / admin`.
- Mantenha o arquivo `docker-compose.yml` sincronizado com as versões exigidas pelo projeto (ex.: Redis 8.2.1).

## Deploy (Railway)

- Produção roda no Railway com serviços separados: frontend, backend, PostgreSQL, Redis, OpenObserve e bucket de arquivos.
- Deploy automático via push na branch `master`.
- Config-as-code disponível em `bc-finances-backend/railway.json` e `bc-finances-frontend/railway.json`.
- Frontend utiliza build Vite e é servido por Nginx via Dockerfile do projeto; manter `VITE_API_URL` apontando para a URL pública do backend.
- Backend depende das variáveis `DB_*`, `REDIS_*`, `AWS_S3_*`, `MAIL_*` e `OTEL_*` configuradas no Railway.

## Boas Práticas Operacionais

- Valide conectividade com serviços externos (AWS S3, SMTP) em ambientes de staging antes de liberar para produção.
- Mantenha logs e métricas alinhados às necessidades de auditoria.
- Registre no `CHANGELOG.md` qualquer alteração operacional relevante (novos serviços, ajustes de infraestrutura, versões de dependências).
