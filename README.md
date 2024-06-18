# ATM

Sistema de caixa eletrônico com backend java com spring boot e frontend react

## Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina as seguintes ferramentas:
- [Git](https://git-scm.com)
- [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Maven 3.6.3](https://maven.apache.org/download.cgi)
- [PostgreSQL](https://www.postgresql.org/download/)

## Como rodar a aplicação

Criar um banco de dados no postgresql com o nome de ATM

Este projeto usa PostgreSQL como banco de dados. 
As configurações de conexão com o banco de dados estão no arquivo 
src/main/java/br/com/spindola/resources/application.properties

### Clone este repositório
```bash
git clone https://github.com/thiagospind/backend-atm.git

Abrir a pasta do projeto no terminal
cd "nome-do-projeto"

Instalar as dependencias
mvn install

Execute a aplicação
mvn spring-boot:run

A aplicação estará rodando na porta 8080 - acesse http://localhost:8080

Para rodar os testes, use o comando:
mvn test

---

Endpoints:

Clientes
GET /client/{id}: Este endpoint é usado para obter um cliente específico pelo seu ID. O ID do cliente é fornecido como um parâmetro de caminho.

POST /client: Este endpoint é usado para criar um novo cliente. O objeto do cliente é fornecido no corpo da solicitação. Se o cliente for criado com sucesso, ele retorna um status 201 (Criado).
Modelo de payload:
{  
	"name": "Cliente Teste",
	"cpf": "12345678909"
}

PUT /client: Este endpoint é usado para atualizar um cliente existente. O objeto do cliente é fornecido no corpo da solicitação. Se a atualização for bem-sucedida, ele retorna um status 204 (Sem conteúdo).

DELETE /client/{id}: Este endpoint é usado para excluir um cliente específico pelo seu ID. O ID do cliente é fornecido como um parâmetro de caminho. Se a exclusão for bem-sucedida, ele retorna um status 204 (Sem conteúdo).

---

Conta Bancária
GET /bankaccount/{id}: Este endpoint é usado para obter uma conta bancária específica pelo seu ID. O ID da conta bancária é fornecido como um parâmetro de caminho.

POST /bankaccount: Este endpoint é usado para criar uma nova conta bancária. O objeto da conta bancária é fornecido no corpo da solicitação. Se a conta bancária for criada com sucesso, ele retorna um status 201 (Criado).
Modelo de payload:
{
	"account": "9339",
	"agency": "109069",
	"balance": 10000,
	"clientId": 1
}
PUT /bankaccount: Este endpoint é usado para atualizar uma conta bancária existente. O objeto da conta bancária é fornecido no corpo da solicitação. Se a atualização for bem-sucedida, ele retorna um status 204 (Sem conteúdo).

DELETE /bankaccount/{id}: Este endpoint é usado para excluir uma conta bancária específica pelo seu ID. O ID da conta bancária é fornecido como um parâmetro de caminho. Se a exclusão for bem-sucedida, ele retorna um status 204 (Sem conteúdo).

---

Saques
GET /withdrawal/{id}: Este endpoint é usado para obter um saque específico pelo seu ID. O ID do saque é fornecido como um parâmetro de caminho.

GET /withdrawal/all/{id}: Este endpoint é usado para obter todos os saques associados a uma conta bancária específica. O ID da conta bancária é fornecido como um parâmetro de caminho.

POST /withdrawal: Este endpoint é usado para criar um novo saque. O objeto de saque é fornecido no corpo da solicitação. Se o saque for criado com sucesso, ele retorna um status 201. Se o ID da conta bancária não for fornecido, ele retorna um status 400 (Solicitação inválida).
Modelo de payload:
{
	"bankAccountId": 1,
	"value": 1250
}





