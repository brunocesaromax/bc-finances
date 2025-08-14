# Notas do Desenvolvedor - Branch: 44-adicionar-instruções-de-ia-no-projeto

**Descrição:** Implementação de instruções padronizadas para assistentes de IA (Claude, Gemini) e reorganização da estrutura de documentação do projeto, incluindo criação de agente especializado para geração de notas de desenvolvedor.

**O que foi feito:**
* Criação do arquivo `CLAUDE.md` na raiz do projeto contendo diretrizes completas para o Claude Code, incluindo:
  - Instruções obrigatórias de leitura inicial de contexto
  - Visão geral completa da arquitetura do projeto (Spring Boot + Angular 9)
  - Comandos comuns de desenvolvimento para backend e frontend
  - Configuração detalhada de variáveis de ambiente
  - Padrões de código e princípios SOLID
  - Estruturas obrigatórias para TODO.md e CHANGELOG.md
  - Regras de documentação e formatação (incluindo proibição de emoticons)
* Criação do arquivo `GEMINI.md` como ponteiro para as diretrizes do CLAUDE.md
* Implementação do agente especializado `.claude/agents/developer-notes.md` para automatização da geração de notas de desenvolvedor
* Reorganização da estrutura de documentação movendo imagens importantes para o diretório `./docs/`:
  - Diagramas de classe e entidade movidos para `docs/diagrams/`
  - Capturas de tela das páginas principais movidas para `docs/main-pages/`
* Atualização do `README.md` para referenciar as novas localizações das imagens no diretório docs
* Estabelecimento do diretório `./docs/` como centralizador oficial de documentações técnicas e decisões de arquitetura

**Testes recomendados:**
* Verificar se todas as imagens no README.md estão sendo carregadas corretamente após a mudança de localização
* Validar se o agente developer-notes está funcionando corretamente executando-o em uma branch de teste
* Testar se as instruções do CLAUDE.md estão completas executando diferentes tipos de tarefas de desenvolvimento
* Verificar se a estrutura de diretórios em `./docs/` está adequada para futuras documentações (ADRs, especificações técnicas, etc.)
* Confirmar que os padrões de documentação estabelecidos no CLAUDE.md são consistentes com a metodologia de desenvolvimento do projeto

**Observações:** 
* Esta implementação estabelece a base para padronização de interações com assistentes de IA no projeto
* A estrutura criada permite sincronização futura com outros assistentes de IA (Amazon Q, etc.)
* O diretório `./docs/` agora serve como fonte única de verdade para documentações técnicas
* As diretrizes estabelecidas no CLAUDE.md devem ser mantidas sincronizadas com futuras configurações de IA
* O agente developer-notes automatiza a criação de documentação de mudanças, mas requer validação manual para garantir qualidade
* Recomenda-se criar os arquivos TODO.md e CHANGELOG.md na raiz do projeto seguindo as estruturas definidas no CLAUDE.md