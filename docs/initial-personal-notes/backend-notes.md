# Backend Development Notes

Notas pessoais de desenvolvimento criadas durante a construção do projeto BC Finances, documentando conceitos e implementações aprendidos durante o processo.

## Status Codes HTTP

### Códigos de Sucesso (2xx)
- **2xx** → Sucesso

### Códigos de Erro do Cliente (4xx)
- **4xx** → Erro do cliente

### Códigos de Erro do Servidor (5xx)
- **5xx** → Erro no serviço/servidor

## OAuth2

### Principais Atores do Fluxo OAuth2

1. **Usuário dono do recurso (Resource Owner)**
   - O usuário final que autoriza o acesso aos seus recursos

2. **Client qualquer (Client Application)**
   - Aplicação que solicita acesso aos recursos

3. **Authorization Server (Servidor de autorização)**
   - Responsável por autenticar o usuário e emitir tokens
   - Recebe requisições de token e retorna tokens válidos

4. **Resource Server (Servidor que contém os recursos)**
   - Servidor que hospeda os recursos protegidos
   - Valida tokens de acesso antes de fornecer recursos

### Arquitetura

Os dois últimos componentes (Authorization Server e Resource Server) podem ser:
- **Separados**: Usado por grandes empresas para maior escalabilidade
- **Juntos**: Abordagem utilizada neste curso/projeto

## JWT (JSON Web Tokens)

### Referência
- **Site oficial**: [jwt.io](https://jwt.io)

### Características
- O token contém informações como:
  - Algoritmo de codificação
  - Dados (Payload)
  - Assinatura digital
- **Tamanho**: Quanto mais informações no payload, maior será o token
- **Composição**: header + payload + assinatura

### Estrutura
```
header.payload.signature
```

## Refresh Token

### Conceitos Principais
- **Finalidade**: Atualizar o accessToken sem necessidade de usuario e senha novamente
- **Segurança**: AccessToken deve ser temporário por questões de segurança
- **Armazenamento**: O refreshToken é guardado em um cookie seguro HTTPS
- **Isolamento**: JavaScript não tem acesso direto ao cookie do refreshToken

### Boas Práticas
- Colocar o refresh_token em um cookie para que a aplicação não tenha acesso direto
- O cookie com refresh token é enviado automaticamente pelo browser
- A aplicação não precisa (e não deve) gerenciar diretamente o refresh token

### Observações Técnicas
- É possível visualizar pelo Wireshark que o cookie com refresh token é enviado pelo Postman
- A aplicação cliente não tem conhecimento direto deste envio

## CORS (Cross-Origin Resource Sharing)

### Problema
- Proteção implementada no JavaScript que só permite requisições para a mesma origem
- Navegadores bloqueiam requisições cross-origin por padrão

### Solução
- **CORS**: Configuração que permite requisições entre diferentes origens

### Documentação de Referência
- [Spring CORS Guide](https://spring.io/guides/gs/rest-service-cors/)

### Limitações Conhecidas
- Na época da criação destas notas, ainda não havia uma forma padrão de integração do CORS com Spring Security OAuth2