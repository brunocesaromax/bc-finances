# Notas do Desenvolvedor – Branch 78-atualizar-frontend-para-utilizar-react-em-vez-de-angular

**Data:** 2025-10-26  
**Responsável:** Bruno César  

## Resumo da Demanda
- Substituição definitiva do frontend Angular pelo novo projeto React (Vite + Tailwind).
- Ajustes na infraestrutura (Docker Compose, Dockerfile, Nginx) para servir o bundle React.
- Atualização das instruções de agentes, README e criação de guia dedicado ao frontend (`docs/frontend/README.md`).
- Limpeza de endpoints backend obsoletos e configuração de defaults de ambiente (mail/S3) para rodar via Docker.

## Principais Alterações
1. **Frontend React**
   - Diretório definitivo: `bc-finances-frontend/`.
   - Estrutura modular organizada em `components/`, `pages/`, `services/`, `contexts/`, `hooks/`.
   - Axios configurado com interceptador para token e fallback `/api` em produção.
   - AuthContext gerencia login, logout silencioso (401) e permissões.
   - Tailwind CSS com design tokens no `index.css` e componentes UI reutilizáveis.

2. **Docker / Deploy**
   - Dockerfile multi-stage (Node 20 + Nginx 1.27) em `bc-finances-frontend/Dockerfile`.
   - Configuração SPA com fallback e proxy `/api` em `bc-finances-frontend/docker/nginx.conf`.
   - `docker-compose.yml`: expõe porta 5173, adiciona `VITE_API_URL=/api`.
   - Backend recebe defaults para variáveis de mail/S3 e CORS atualizado para 5173.

3. **Backend**
   - Remoção de endpoints/DTOs de estatísticas, relatórios e listagens não utilizados.
   - Ajustes em `TransactionRepositoryImpl` mantendo somente operações necessárias ao resumo consumido pelo React.

4. **Documentação**
   - README atualizado com porta 5173, instruções de build do React e link para o guia de frontend.
   - Instruções de agentes (`docs/ia-agents-instructions`) alinhadas com o stack moderno.
   - Novo `docs/frontend/README.md` detalhando padrões e convenções do projeto React.

## Comandos Importantes
```bash
# Desenvolvimento
cd bc-finances-frontend
npm install
npm run dev

# Build de produção
npm run build
npm run preview

# Docker local
docker compose down --remove-orphans
docker compose up -d --build
```

## Considerações / Lições
- Manter o valor `VITE_API_URL` como `/api` em ambientes containerizados simplifica a comunicação via proxy Nginx.
- AuthContext precisa monitorar expiração do token (já faz via payload `exp`); futuros ajustes podem considerar refresh tokens.
- O bundle de produção (`~593 kB gzip 177 kB`) ainda pode ser otimizado com code splitting (aviso do Vite). Avaliar manualChunks posteriormente.

## Pendências Futuras
- Introduzir suíte de testes (Vitest + Testing Library) para componentes críticos.
- Documentar design tokens e guidelines visuais com mais detalhes.
- Avaliar logs de build (chunk grande) e quebrar rotas caso necessário.
- Configurar pipeline CI/CD para rodar `npm run lint` e `npm run build`.
