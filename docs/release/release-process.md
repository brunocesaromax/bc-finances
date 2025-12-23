# Guia de Release e Versionamento

Este documento descreve o fluxo oficial para publicar novas versões do BC Finances, garantindo consistência entre branches, tags e documentação.

## 1. Preparação na Branch `develop`
- Certifique-se de que todas as implementações da versão estejam integradas em `develop`.
- Execute `mvn clean compile` no backend e valide testes adicionais conforme a demanda.
- Para o frontend, rode `npm run lint` e demais verificações necessárias.
- Atualize as versões das aplicações antes do commit de versionamento. Garanta que backend e frontend estejam alinhados com a tag planejada (ex.: `0.3.0`):
  - Backend: ajuste o `<version>` em `bc-finances-backend/pom.xml`.
  - Frontend: atualize o campo `version` em `bc-finances-frontend/package.json` e `package-lock.json`.
- Gere o commit de versionamento (`chore(project-version): Set new project version 0.3.0`, por exemplo) já em `develop`.

```bash
git checkout develop
git pull --rebase origin develop

## Validar backend
mvn clean compile

## Validar frontend
npm run lint
```

## 2. Alinhamento com a Branch `master`
- Troque para `master` e traga a versão remota mais recente.
- Faça merge fast-forward a partir de `develop`; releases **sempre** partem de `master`.
- Em caso de conflito, resolva, confirme o merge e revalide os testes obrigatórios.
- **Produção no Railway:** qualquer push na `master` dispara o deploy automático.

```bash
git checkout master
git pull origin master
git merge --ff-only develop
```

## 3. Criação da Tag de Release
- Com `master` apontando para o commit de versionamento, crie a tag anotada. **Nunca** tague diretamente a partir de `develop` ou de branches de feature.
- A mensagem deve indicar claramente o objetivo da release.

```bash
git tag -a v0.2.0 -m "Refactor backend to Clean Architecture"
```

## 4. Publicação
- Publique a branch e a tag para o repositório remoto.
- Atualize documentações adicionais (README, notas de deploy, etc.) se houver alterações relevantes.

```bash
git push origin master
git push origin v0.3.0
```

## 5. Merge-back obrigatório para `develop`
- Após publicar a tag, traga `master` de volta para `develop` para evitar divergências (ex.: commits de versionamento ou hotfixes feitos direto em `master`).

```bash
git checkout develop
git pull --rebase origin develop
git merge --ff-only master
git push origin develop
```

- Caso `--ff-only` falhe, abra PR de sincronização `master -> develop` para revisão. **Não** deixe commits exclusivos em `master`.

## Políticas Complementares
- Mantenha o histórico linear em `master`; evite `--no-ff` e commits extras.
- Releases só devem ser executadas com a árvore de trabalho limpa e após todos os testes definidos em `docs/ia-agents-instructions/development.md`.
- Sempre valide que `pom.xml` e `package*.json` com a versão da release estão presentes em `develop` depois do merge-back; divergências indicam release incompleto.
