# Segurança e Operações Críticas

## Configurações de Segurança

- OAuth2 com dois clientes obrigatórios: `web` e `mobile`, definidos em `AuthorizationServerConfig`.
- Tokens JWT são enriquecidos por `CustomTokenEnhancer`; qualquer ajuste deve preservar assinaturas e claims existentes.
- Configuração CORS centralizada em `CorsFilter`; mantenha alinhamento com o frontend.
- Tratamento de sessões depende de Redis (quando habilitado) e deve obedecer às políticas de invalidação definidas na demanda atual.

## Variáveis Sensíveis

- Defina obrigatoriamente: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `AWS_S3_ACCESS_KEY_ID`, `AWS_S3_SECRET_ACCESS_KEY`, `AWS_S3_BUCKET`, `FRONT_END_CLIENT`, `FRONT_END_PASSWORD`, `MOBILE_CLIENT`, `MOBILE_PASSWORD`.
- Nunca versione segredos; utilize arquivos `.env` e perfis Spring (`dev`, `prod`, `oauth-security`, `basic-security`).
- Ao criar tutoriais ou exemplos, utilize credenciais fictícias claramente indicadas.

## Operações Restringidas

- **Proibido executar `docker system prune -f`** ou variantes. O comando elimina recursos críticos de outros projetos.
- Utilize apenas:
  ```bash
  docker container prune
  docker image prune
  docker network prune
  docker system df
  ```
  conforme necessidade e sempre com aprovação do usuário.

- Não abra portas adicionais, não altere regras de firewall ou configurações sensíveis sem validação explícita.

## Boas Práticas Operacionais

- Antes de alterações críticas, confirme impacto em integrações (AWS S3, e-mails, OAuth).
- Garanta que qualquer script ou automação trate falhas com segurança (rollback, limpeza, logs).
- Em caso de comportamento inesperado, suspenda a execução e informe o usuário imediatamente – nunca force operações destrutivas.
