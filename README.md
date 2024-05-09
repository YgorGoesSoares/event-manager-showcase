# Gerenciador de Eventos

Este é um projeto de um gerenciador de eventos desenvolvido em Java utilizando Spring Boot, Spring Security, e outras tecnologias relacionadas. Ele permite gerenciar eventos com propriedades como ID, nome, data e URL, além de associar cada evento a uma cidade com um ID e um nome. A aplicação oferece autenticação e autorização com Spring Security.

## Tecnologias Utilizadas

- **Java**: Linguagem de programação utilizada para o desenvolvimento do projeto.
- **Spring Boot**: Framework para desenvolvimento de aplicações baseadas em Spring.
- **Spring Security**: Framework para controle de autenticação e autorização.
- **Spring Data JPA**: Framework para facilitar a comunicação com o banco de dados.
- **Spring OAuth2 Resource Server**: Para gerenciamento de tokens JWT e proteção de endpoints.
- **Java JWT**: Biblioteca para trabalhar com JSON Web Tokens (JWT).
- **H2 Database**: Banco de dados em memória para armazenamento de dados durante a execução.
- **Lombok**: Biblioteca para simplificar o desenvolvimento.
- **Maven**: Gerenciador de dependências para a construção do projeto.

## Regras de Autorização com Spring Security

As seguintes regras de autorização são aplicadas na aplicação:

1. Os endpoints de login e do H2 são públicos.
2. Os endpoints `GET` para `/cities` e `/events` são públicos.
3. O endpoint `POST` de `/events` requer login de **ADMIN** ou **CLIENT**.
4. Todos os demais endpoints requerem login de **ADMIN**.

A collection do Insomnia com as requisições prontas está na pasta /resources/Insomnia_2024-05-08.json

## Diagrama de Classes

Abaixo está o diagrama de classes da aplicação:

![Diagrama de Classes](https://i.imgur.com/hUHlj7a.png)

## Como Configurar e Executar o Projeto

Siga as instruções abaixo para configurar e executar o projeto localmente:

1. Clone o repositório do projeto:

    ```shell
    git clone https://github.com/YgorGoesSoares/event-manager-showcase
    ```

2. Navegue até o diretório do projeto:

    ```shell
    cd event-manager-showcase
    ```

3. Compile e execute o projeto usando Maven:

    ```shell
    mvn spring-boot:run
    ```

4. A aplicação estará em execução em `http://localhost:8080`.
A documentação ficará disponível em `http://localhost:8080/swagger-ui/index.html`.

## Usuários de Teste

A aplicação possui os seguintes usuários para testar as requisições:

- **Cliente (CLIENT):**
    - Email: `alice@example.com`
    - Senha: `alice123`

- **Administrador (ADMIN):**
    - Email: `bob@example.com`
    - Senha: `bob123`

## Contribuição

Sinta-se à vontade para contribuir com o projeto enviando pull requests ou reportando issues.

Feito por Ygor Goes | [LinkedIn](https://linkedin.com/in/ygor-goes)
