**Descrição:** Correção dos avisos do ESLint no frontend garantindo logging adequado dos erros sem alterar os fluxos existentes.

**O que foi feito:**
* Ajustada a paginação para utilizar `const end`, eliminando o alerta `prefer-const`.
* Inseridos logs de erro com `console.error` nos fluxos de formulários e listagens de pessoas e lançamentos para preservar rastreabilidade antes dos toasts.
* Adequados blocos `catch` em utilitários e contexto de autenticação para remover variáveis não utilizadas mantendo o tratamento original.
* Nenhuma configuração extra ou fluxo fora do escopo foi alterado.

**Testes recomendados:**
* Executar `npm run lint` para garantir que os avisos permanecem eliminados.
* Validar criação/edição/listagem de pessoas e lançamentos, observando logs em caso de falhas simuladas nos serviços.

**Observações:** Sem pendências identificadas nesta correção.
