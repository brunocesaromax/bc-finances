# Frontend React – Guia de Arquitetura e Padrões

Este documento resume os principais conceitos adotados no novo frontend do **BC Finances** após a migração para **React 19 + Vite + Tailwind CSS**. O objetivo é manter o time alinhado sobre estrutura de pastas, padrões de codificação e decisões arquiteturais.

---

## 1. Stack e Ferramentas

- **Framework:** React 19 com Vite 7.
- **Linguagem:** TypeScript 5.9 (strict).
- **Estilização:** Tailwind CSS 4 + tokens definidos em `src/index.css`.
- **Formulários:** `react-hook-form` + `zod` (validação declarativa).
- **HTTP:** Axios com interceptadores centralizados (`src/services/apiClient.ts`).
- **Estado global:** Context API (principalmente `AuthContext`).
- **Roteamento:** React Router DOM 7 (`src/routes`).
- **Build tooling:** Vite (dev & bundling) + Docker multi-stage servindo por Nginx.
- **Feedbacks:** `react-hot-toast` para notificações.

---

## 2. Estrutura de Diretórios (`src/`)

```
components/     # UI compartilhada (botões, inputs, cards, etc.)
config/         # Configurações globais e helpers (ex.: env.ts)
contexts/       # Contextos React (AuthContext, etc.)
hooks/          # Hooks reutilizáveis (useAuth, useDebouncedValue, ...)
pages/          # Páginas organizadas por domínio
routes/         # Definição de rotas protegidas e públicas
services/       # Camada de acesso à API (Axios) e serviços auxiliares
storage/        # Abstrações de armazenamento (ex.: authStorage usando localStorage)
types/          # Tipos/contratos compartilhados por domínio
utils/          # Funções utilitárias (formatters, masks, etc.)
assets/         # Logos, ícones e recursos estáticos
```

### Convenções

- Cada domínio (transactions, persons, auth) possui uma página principal dentro de `pages/` e consome serviços/tipos específicos.
- Componentes compartilhados ficam em `components/ui`. Prefira compostos compostáveis com propriedades tipadas.
- Hooks seguem prefixo `use` e devem ser puros; evitar efeitos colaterais implícitos.
- Serviços expõem funções assíncronas simples retornando tipos definidos em `types/`.

---

## 3. Fluxo de Autenticação

- `AuthContext` centraliza login, logout, token e permissões.
- Tokens são persistidos via `authStorage` (localStorage).
- Interceptador Axios remove token quando recebe 401 não relacionado a login/logout e dispara `logout()` silencioso.
- Rotas protegidas utilizam `PrivateRoute` (ver `src/routes/AppRoutes.tsx`) com checagem de permissões via `hasAnyPermission`.

---

## 4. Axios & Acesso à API

- `apiClient` adiciona automaticamente cabeçalho `Authorization`.
- Base URL vem de `env.apiUrl`:
  - Dev (`npm run dev`): `http://localhost:8080`.
  - Produção (build): `/api` (proxy Nginx).
- Requests aceitam `AbortSignal` para cancelamento (ex.: buscas com debounce).
- Serviços organizam endpoints por domínio (`transactionService`, `personService`, etc.).

---

## 5. Formulários e Validação

- `react-hook-form` com `zodResolver`.
- Schemas ficam próximos ao formulário (ex.: `TransactionFormPage.tsx`).
- Use `defaultValues` centralizados e função `hydrate` para edição.
- Manter mensagens de erro em português, consistentes com UX.

---

## 6. Estilização e Design System

- Tailwind CSS com tokens customizados em `:root` (`src/index.css`).
- Componentes reutilizáveis (`Button`, `Input`, `FormLabel`, etc.) encapsulam classes Tailwind para garantir consistência.
- Evite classes inline duplicadas; quando um padrão se repetir, extraia para componente/hook.
- Responsividade mobile-first usando utilitários (`sm:`, `md:`…).

---

## 7. Boas Práticas de Código

- **Tipagem:** Sempre tipar respostas de API (tipos em `types/`). Evitar `any`.
- **Imports:** Usar paths absolutos via alias `@/` (configurado no `tsconfig.json`).
- **Erro e Loading states:** tratar explicitamente (spinner, EmptyState, toast).
- **Debounce e abort:** para buscas em tempo real, usar `useDebouncedValue` + `AbortController`.
- **Logs:** evitar `console.log` em produção; usar toasts para feedback de usuário.
- **Separação de responsabilidades:** páginas cuidam da orquestração, componentes de layout/visual.

---

## 8. Configuração e Ambientes

- Variáveis de ambiente:
  - `VITE_API_URL` (opcional no build). No Docker definimos `/api`.
- Dockerfile multi-stage (Node build + Nginx runtime) em `Dockerfile`.
- Configuração Nginx em `docker/nginx.conf` (fallback SPA + proxy `/api`).
- `docker-compose.yml` expõe frontend em `5173` e backend em `8080`.

---

## 9. Testes e Qualidade

- ESLint configurado (executar `npm run lint`).
- Ainda não há suíte de testes automatizados migrada do Angular; recomendação é adotar Vitest + Testing Library futuramente.
- Husky/Prettier não estão configurados; manter atenção à formatação manual.

---
