# Instruções Gerais para Agentes de IA

Este diretório centraliza todas as orientações obrigatórias para qualquer agente de IA (Claude, Gemini, Codex, Amazon Q, etc.) que interaja com este repositório.

## Leitura Obrigatória a Cada Nova Sessão

Antes de iniciar qualquer atividade:

1. Leia integralmente `docs/ia-agents-instructions/README.md`, `tasks/{branch-name}/TODO.md` e `tasks/{branch-name}/CHANGELOG.md` no diretório `tasks/{branch-name}/`.
2. Revise os diagramas em `docs/diagrams` (classes e entidade-relacionamento).
3. Leia conforme necessário, repito **CONFORME NECESSÁRIO**, os arquivos deste diretório `docs/ia-agents-instructions`. Nenhuma interação é permitida sem o carregamento dos arquivos **necessários** dessas instruções em contexto.
   Quando digo necessário, me refiro a carregar conforme o contexto que se irá trabalhar/alterar: desenvolvimento, arquitetura, observabilidade, etc. 

Ignorar qualquer um desses passos constitui violação grave das regras do projeto.

## Estrutura por Contexto

- `development.md`: comandos obrigatórios, padrões de código, testes e práticas de desenvolvimento.
- `documentation.md`: procedimentos para TODO, CHANGELOG e sincronização de documentações.
- `security-and-operations.md`: políticas críticas de segurança, variáveis sensíveis, restrições de operações e rotinas de infraestrutura.
- `architecture.md`: visão geral do sistema, módulos principais e referências a diagramas.
- `migrations.md`: padrões para banco de dados, nomenclaturas e regras rígidas para migrations Flyway.
- `operations-and-deploy.md`: orientações de ambiente, Docker, deploys e requisitos de execução.
- `docs/release/release-process.md`: fluxo oficial para versionamento, criação de tags e publicação das releases.

Todos os arquivos devem ser lidos integralmente – não basta consultar tópicos isolados.

## Cumprimento e Atualizações

- Trate estas regras como parte do código-fonte: nenhuma exceção é permitida.
- Ao abrir uma nova demanda, verifique se `tasks/{branch-name}/TODO.md` e `tasks/{branch-name}/CHANGELOG.md` existem e estão alinhados com a tarefa atual.
- **IMPORTANTE**: Atualize os documentos do item anterior a cada sub-etapa da tarefa atual concluída antes de continuar o desenvolvimento. Em relação ao conceito de 'sub-etapa', siga a famosa regra de desenvolvimento de software de dividir para conquistar.
- Atualize as instruções deste diretório sempre que regras forem ajustadas ou novas políticas forem definidas pelo mantenedor.

Em caso de dúvida, pause o trabalho e solicite esclarecimentos antes de prosseguir. Segurança, consistência e aderência às regras são obrigações absolutas.
