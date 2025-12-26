# AGENTS.md

Este documento define o protocolo obrigatório para agentes de IA ao atuar neste repositório.

## Checklist Inicial Obrigatório

A cada nova sessão ou interação, carregue integralmente em contexto:

1. `README.md`
2. `tasks/{branch-name}/TODO.md`, se `{branch-name}` for diferente de: develop, master, main 
3. `tasks/{branch-name}/CHANGELOG.md`, se `{branch-name}` for diferente de: develop, master, main
4. Diagramas em `docs/diagrams` (classes e entidade-relacionamento)
5. **O arquivo README.md do diretório `docs/ia-agents-instructions/README.md`**
6. Carregar conforme necessário, repito **CONFORME NECESSÁRIO**, os arquivos de instruções de IA em `docs/ia-agents-instructions` e também os arquivos de documentação gerais em `docs`

Não prossiga com nenhuma ação sem confirmar a leitura e entendimento completo dos materiais de 1 a 6 acima.

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
- Ao abrir nova demanda, garanta que `tasks/{branch-name}/TODO.md` e `tasks/{branch-name}/CHANGELOG.md` existam e estejam alinhados com o trabalho em execução. Regra deve ser aplicada somente se branch atual for diferente de: develop, master, main.
- **IMPORTANTE**: Atualize os documentos do item anterior a cada sub-etapa da tarefa atual concluída antes de continuar o desenvolvimento. Em relação ao conceito de 'sub-etapa', siga a famosa regra de desenvolvimento de software de dividir para conquistar.
- Em caso de dúvida, pause a atividade e solicite esclarecimentos ao mantenedor.
