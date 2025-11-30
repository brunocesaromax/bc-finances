# Documentação Obrigatória

## Regras Críticas

- `TODO.md` e `CHANGELOG.md` devem existir e ser atualizados a cada demanda ou branch.
- Verifique a presença desses arquivos após a primeira interação com o usuário; crie-os imediatamente caso estejam ausentes.
- Atualize ambos sempre que concluir uma tarefa significativa. Pare o desenvolvimento até que a documentação esteja alinhada.
- Registre apenas o que pertence à demanda/branch atual. Não antecipe funcionalidades futuras.

## Estrutura do TODO.md

```markdown
# TODO - BC Finances

## Branch Atual (nome-da-branch)
- [ ] Itens relacionados à demanda vigente

## Bugs Conhecidos
- [ ] Problemas identificados pendentes dentro da mesma demanda

## Concluído
- [x] Tarefas finalizadas conforme avançar
```

- Seções adicionais somente em casos excepcionais, mantendo foco no escopo atual.
- Organize por contexto (Backend/Frontend/DevOps) se necessário, mas sem abrir novas demandas.

## Estrutura do CHANGELOG.md

```markdown
# Changelog

## [Versão] - Data (Branch: nome-da-branch)
### Backend
- Mudanças de API e serviços

### Frontend
- Alterações de UI e UX

### Adicionado/Modificado/Corrigido/Removido
- Liste cada item de forma clara
```

- Mantenha versão, data e branch que originou as mudanças.
- Sincronize as entradas com tags do Git quando aplicável.
- Considere arquivar periodicamente seções antigas, preservando histórico.

## Sincronização entre Agentes de IA

- `CLAUDE.md`, `GEMINI.md`, `AGENTS.md`, `.amazonq/rules` e demais artefatos devem conter referências consistentes entre si.
- Ao alterar regras neste diretório, reflita imediatamente as mesmas instruções nos arquivos de cada agente.
- Violações de consistência são tratadas como erro crítico de documentação.

## Padrões de Idioma

- Código SQL (incluindo comentários) deve permanecer em inglês.
- Documentações do projeto (README, TODO, CHANGELOG e arquivos deste diretório) devem ser escritas em português.
- Mensagens de commit podem ser feitas em inglês, seguindo o padrão em vigor na branch. Utilizar a boa prática de commits semânticos.
