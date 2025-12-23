# Notas do Desenvolvedor - Hospedagem em Produção no Railway

**Branch:** 56-hospedar-aplicação

**Descrição:** Consolidação do ambiente de produção no Railway, com serviços separados para backend, frontend, banco, cache, observabilidade e bucket, além da definição do fluxo de deploy via push na branch `master`.

---

## Principais Alterações

- Estrutura de produção no Railway com serviços independentes: `bc-finances-backend`, `bc-finances-frontend`, PostgreSQL, Redis, OpenObserve e bucket de arquivos.
- Frontend publicado em `https://bc-finances.up.railway.app`, consumindo a API por URL pública do backend.
- Integração Railway ↔ GitHub ativa: push na branch `master` dispara o deploy automático em produção.
- Observabilidade no Railway com envio parcial de traces ao OpenObserve; logs e métricas estão desabilitados por custo e podem ser reativados via `bc-finances-backend/src/main/resources/application-prod.yml`.

---

## Decisões Importantes

1. **Deploy contínuo via `master`:** qualquer push na branch `master` gera nova publicação em produção.
2. **Separação de serviços:** backend, frontend, banco, cache e observabilidade em serviços isolados para permitir escala e manutenção independentes.
3. **Frontend desacoplado de proxy `/api`:** uso de `VITE_API_URL` apontando para a URL pública do backend.

---

## Pontos de Atenção

- Manter `bc-finances.origin-permitted` atualizado com a URL de produção do frontend.
- Garantir que as variáveis de ambiente no Railway estejam alinhadas aos perfis `prod` (S3, Redis, SMTP e OpenObserve).
- Verificar periodicamente a consistência dos arquivos `railway.json` com a configuração real dos serviços.

---

## Testes / Validações

- Acessar `https://bc-finances.up.railway.app` e validar login com `admin@algamoney.com / admin`.
- Verificar saúde do backend via `GET /actuator/health`.
- Validar upload e acesso de anexos via URL presignada no bucket.
- Confirmar ingestão parcial de traces no OpenObserve do Railway (logs e métricas permanecem desabilitados até ajuste no `application-prod.yml`).
