# Operações, Ambiente e Deploy

## Configuração de Ambiente

- Utilize perfis Spring apropriados: `dev` (desenvolvimento com PostgreSQL local), `prod`, `oauth-security` (padrão) e `basic-security` (fallback HTTP Basic).
- Arquivos `.env` são obrigatórios e devem conter credenciais e configurações sensíveis.
- Certifique-se de que Redis, PostgreSQL e demais serviços externos estejam acessíveis antes de iniciar a aplicação.

## Docker Compose

```bash
docker-compose up -d      # subir PostgreSQL + pgAdmin
docker-compose ps         # verificar status
docker-compose down       # encerrar serviços
```

- Acesse o pgAdmin em `http://localhost:8081` com `admin@lancamentos.com / admin`.
- Mantenha o arquivo `docker-compose.yml` sincronizado com as versões exigidas pelo projeto (ex.: Redis 8.2.1).

## Deploy (Heroku)

- O frontend utiliza `server.js` (Express) para servir o bundle de produção.
- `postinstall` executa o build de produção automaticamente.
- Engines configuradas: Node.js 10.19.0 / npm 6.14.11.
- Backend possui `Procfile` pronto para deploy; valide variáveis de ambiente antes de publicar.

## Boas Práticas Operacionais

- Valide conectividade com serviços externos (AWS S3, SMTP) em ambientes de staging antes de liberar para produção.
- Mantenha logs e métricas alinhados às necessidades de auditoria.
- Registre no `CHANGELOG.md` qualquer alteração operacional relevante (novos serviços, ajustes de infraestrutura, versões de dependências).
