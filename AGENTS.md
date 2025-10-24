# AGENTS.md

Este documento define o protocolo obrigatório para agentes de IA ao atuar neste repositório.

## Checklist Inicial Obrigatório

A cada nova sessão ou interação, carregue integralmente em contexto:

1. `README.md`
2. `TODO.md`
3. `CHANGELOG.md`
4. Diagramas em `docs/diagrams` (classes e entidade-relacionamento)
5. **Todos os arquivos do diretório `docs/ia-agents-instructions`**

Não prossiga com nenhuma ação sem confirmar a leitura completa desses materiais.

## Diretório Mestre de Instruções (`docs/ia-agents-instructions`)

As regras detalhadas estão organizadas por contexto. Leia cada arquivo na íntegra:

- `README.md`: visão geral, ordem de leitura e políticas globais.
- `development.md`: comandos obrigatórios, padrões de código, testes e práticas de engenharia.
- `documentation.md`: manutenção de TODO/CHANGELOG, padrões de idioma e sincronização entre agentes.
- `security-and-operations.md`: políticas críticas de segurança, manuseio de credenciais e operações restritas.
- `architecture.md`: visão de alto nível, módulos e entidades principais do sistema.
- `migrations.md`: regras rígidas para migrations Flyway (estrutura, nomenclatura, rollback).
- `operations-and-deploy.md`: requisitos de ambiente, Docker Compose e notas de deploy.

Qualquer alteração nesses arquivos deve ser imediatamente refletida aqui e nos demais documentos de agentes.

## Cumprimento das Regras

- Trate estas instruções como parte do código-fonte: não existem exceções.
- Ao abrir nova demanda, garanta que `TODO.md` e `CHANGELOG.md` existam e estejam alinhados com o trabalho em execução.
- Atualize a documentação a cada tarefa concluída antes de continuar o desenvolvimento.
- Em caso de dúvida, pause a atividade e solicite esclarecimentos ao mantenedor.
