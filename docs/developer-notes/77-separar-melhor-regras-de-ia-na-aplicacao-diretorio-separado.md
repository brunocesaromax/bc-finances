# Notas do Desenvolvedor - Reorganização das Regras de IA

**Branch:** 77-separar-melhor-regras-de-ia-na-aplicacao-diretorio-separado

**Descrição:** Estruturação das diretrizes de agentes de IA em um diretório dedicado organizado por contexto, padronização dos ponteiros principais (CLAUDE, GEMINI e AGENTS) e reforço das instruções obrigatórias para qualquer agente que interaja com o repositório.

---

## Principais Alterações

- Criação do diretório `docs/ia-agents-instructions/` com arquivos individuais por contexto (`README`, `development`, `documentation`, `security-and-operations`, `architecture`, `migrations`, `operations-and-deploy`), consolidando regras antes dispersas.
- Renomeação dos arquivos de instrução para nomes em inglês, mantendo coerência com o padrão de nomenclatura do projeto.
- Atualização de `CLAUDE.md`, `GEMINI.md` e `AGENTS.md` para utilizar o mesmo protocolo, exigindo leitura completa do novo diretório a cada sessão.
- Registro das novas obrigações em `TODO.md` e `CHANGELOG.md`, garantindo rastreabilidade da mudança de processo.

---

## Decisões Importantes

1. **Fonte única de verdade:** toda instrução obrigatória para agentes passa a residir em `docs/ia-agents-instructions`, eliminando divergências entre arquivos individuais.
2. **Padronização de idiomas:** nomes de arquivos em inglês (ASCII) para compatibilidade com padrões do repositório e integrações externas.
3. **Sincronização automática:** os ponteiros principais agora compartilham o mesmo conteúdo, reduzindo risco de desatualização entre agentes diferentes.

---

## Pontos de Atenção

- Qualquer atualização nas regras deve ocorrer primeiro no diretório `docs/ia-agents-instructions` e, em seguida, ser refletida nos arquivos de ponteiro.
- Verificar se outros artefatos de automação (ex.: `.amazonq/rules`) também estão alinhados com o novo diretório.
- Manter a disciplina de leitura inicial obrigatória (README/TODO/CHANGELOG/diagramas + instruções de IA) antes de qualquer interação do agente.

---

## Testes / Validações

- `mvn clean compile` (backend) para assegurar que não houve regressões acidentais ao manipular arquivos do projeto.
- Verificação manual das referências para garantir que não restaram menções aos nomes antigos de arquivos (`desenvolvimento.md`, etc.).

