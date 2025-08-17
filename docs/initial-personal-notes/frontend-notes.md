# Frontend Development Notes

Notas pessoais de desenvolvimento Angular criadas durante a construção do projeto BC Finances, documentando conceitos e implementações aprendidos durante o processo.

## Data Binding

### Tipos de Data Binding no Angular

1. **Interpolação**
   ```html
   {{name}}
   ```

2. **Property Binding**
   ```html
   [prop]="name"
   ```

3. **Event Binding**
   ```html
   (click)="save()"
   ```

4. **Two Way Data Binding**
   ```html
   [(ngModel)]="name"
   ```

## Variáveis de Referência

```html
<input type="text" class="form-control" #inputName>
```

## Diretivas

### Tipos de Diretivas

1. **Componentes**
   ```html
   <person-list></person-list>
   ```

2. **Estruturais** (Manipula/Modifica o DOM)
   ```html
   <h2 *ngIf="logado">Olá {{userName}}</h2>
   ```

3. **Atributos** (Modifica comportamento/aparência de elementos)

## Comunicação Entre Componentes

### Entrada de Dados
- **@Input()**: Para receber dados do componente pai

### Saída de Dados  
- **@Output()**: Para emitir eventos para o componente pai
```typescript
@Output() funcionarioAdd = new EventEmitter();
```

## Estilização

### CSS no Componente
```typescript
@Component({
  styles: ['/* CSS aqui */']
})
```

### Estilo CSS Dinâmico

#### Com ngStyle
```html
<!-- Opção 1: Objeto literal -->
[ngStyle]="{ 'background-color': 'red' }"

<!-- Opção 2: Método do componente -->
[ngStyle]="getCardStyle()"
```

#### Com ngClass
```html
<!-- Opção 1: Array de classes -->
[ngClass]="['badge', 'badge-default']"

<!-- Opção 2: Método do componente -->
[ngClass]="getClassListCss()"
```

## Bibliotecas de Componentes

### Opções Disponíveis
- **Angular Material**
- **Angular Materialize** 
- **ng-bootstrap**
- **ngx-bootstrap**
- **PrimeNG** (utilizada neste projeto)

### Instalação de Dependências
```bash
# PrimeNG
npm install primeng --save

# FontAwesome  
npm install fontawesome --save

# Máscara de números
npm install ng2-currency-mask --save
```

## Diretivas Customizadas

### Criação
```bash
ng g d 'directive-name'
```

## Formulários

### Tipos de Formulários

1. **Template-driven forms**
2. **Reactive forms (model-driven)**

### Exemplo Template-driven
```html
<form #userForm="ngForm" (ngSubmit)="salvar(userForm)">
  <!-- campos do formulário -->
</form>
```

## Geração de Componentes

### Componente Simples (sem arquivos separados)
```bash
ng g c message --spec=false --inline-template --inline-style
```

### Componente em Submódulo
```bash
ng g c buttons/button-big --spec=false
```

## Módulos Angular

### Conceitos Principais
- **AppModule**: Módulo raiz de toda aplicação Angular
- **Propósito**: Organizar componentes, diretivas e pipes relacionados
- **Módulos Utilitários**: Contêm ferramentas reutilizáveis em várias partes do projeto

### Quando Criar um Módulo?
- **Feature Modules**: Para funcionalidades relacionadas
- **Core Module**: Para elementos usados na raiz da aplicação

### Organização
- Componentes internos de um módulo podem não ser exportados
- Componentes em submódulos seguem estrutura de diretórios

## Serviços

### Definição
Funcionalidade comum fornecida para vários componentes ou módulos.

### Benefícios
- Componentes mais limpos
- Evita duplicação de código  
- Melhor organização do projeto
- Local para regras de negócio

### Boas Práticas
- **ngOnInit**: Executado após o construtor, ideal para inicializações
- **Construtor**: Apenas para atribuições simples
- **@Injectable**: Por convenção, usar em todas as classes de serviço
- **Atenção**: Evitar problema de múltiplas instâncias do mesmo serviço

## Ferramentas de Desenvolvimento

### JSON Server (API Fake para Testes)
Ferramenta para criar APIs mockadas durante desenvolvimento.

### Build de Produção
```bash
ng build --prod --configuration=production
```

## Atualização do Angular

### Recursos
- **Site oficial**: [update.angular.io](https://update.angular.io/)
- **Comando de verificação**: `ng build --prod` (ajuda a descobrir possíveis erros)
- **Compatibilidade**: Atualizar PrimeNG para versão compatível com Angular

### Processo de Atualização
1. Consultar guia oficial de atualização
2. Executar build de produção para identificar erros
3. Atualizar dependências (ex: PrimeNG) conforme compatibilidade
4. Testar funcionalidades após atualização