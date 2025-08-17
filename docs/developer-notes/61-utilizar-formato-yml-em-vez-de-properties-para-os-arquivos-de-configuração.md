**Descrição:** Refatoração dos arquivos de configuração da aplicação, migrando do formato .properties para .yml e implementando sistema de internacionalização (i18n) com MessageSource configurável.

**O que foi feito:**
* Migração completa dos arquivos de configuração de .properties para .yml (application.properties → application.yml, application-dev.properties → application-dev.yml, application-prod.properties → application-prod.yml)
* Criação da classe InternationalizationConfig com configuração do MessageSource para suporte a i18n usando ReloadableResourceBundleMessageSource
* Adição da propriedade i18n.cacheSeconds na classe ApiProperty com valores diferenciados por ambiente (0 para dev, -1 para prod)
* Reorganização dos arquivos de mensagens para o diretório i18n/, movendo ValidationMessages.properties e criando messages.properties consolidado
* Limpeza de arquivos de documentação pessoal (Notas.txt) movidos para docs/initial-personal-notes/ como documentação histórica
* Correção no README.md alterando comando de execução de ./mvnw para mvn spring-boot:run
* Simplificação do arquivo messages.properties removendo mensagens não utilizadas e mantendo apenas as essenciais

**Testes recomendados:**
* Validar se a aplicação inicia corretamente nos perfis dev e prod após migração para yml
* Testar se as mensagens de validação e i18n estão sendo carregadas corretamente pelo MessageSource
* Verificar se o cache de mensagens está funcionando conforme configurado (sem cache em dev, com cache infinito em prod)
* Confirmar que as configurações de banco de dados, segurança, S3 e email permanecem funcionais após a migração
* Testar cenários de erro para validar se as mensagens localizadas são exibidas corretamente
* Validar que não há regressões nas funcionalidades existentes relacionadas a configurações

**Observações:** A migração para yml melhora a legibilidade e manutenibilidade das configurações. A implementação do sistema de i18n fornece base sólida para futuras expansões de idiomas. O cache configurável permite otimização em produção mantendo flexibilidade em desenvolvimento. A reorganização da documentação preserva o histórico de aprendizado em local mais apropriado.