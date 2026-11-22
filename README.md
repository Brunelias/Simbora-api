# 🎟️ Simbora API

API REST para **venda de tickets online**, desenvolvida com Java e Spring Boot.
Permite gerenciar eventos, lotes, ingressos e compras, com autenticação via JWT
e três perfis de usuário: Admin, Organizador e Cliente.

## ✨ Funcionalidades

**Implementado**

- Autenticação com JWT e alteração de senha
- Perfis de usuário: **Admin**, **Organizador** e **Cliente**
- Gerenciamento de **eventos** (CRUD)
- Gerenciamento de **lotes** e **ingressos**
- Registro de **compras** com itens
- Documentação interativa com Swagger

**Em desenvolvimento**

- Integração com **gateway de pagamento** para processar as compras
- **Check-in por QR Code**: geração de um QR Code para cada ingresso e
  validação na entrada do evento

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas, separando responsabilidades:

```
com.simbora.simbora_api
├── api
│   ├── controller   # Endpoints REST
│   └── dto          # Objetos de entrada e saída da API
├── config           # CORS, Spring Security e Swagger
├── exception        # Exceções de regra de negócio
├── model
│   ├── entity       # Entidades JPA
│   └── repository   # Acesso a dados (Spring Data JPA)
├── security         # Geração e validação de tokens JWT
└── service          # Regras de negócio
```

**Fluxo de uma requisição:**
`Controller → Service → Repository → Banco de dados`

**Decisões do projeto:**

- **DTOs** para não expor as entidades diretamente na API
- **ModelMapper** para converter DTOs em entidades
- **Herança** de `Pessoa` para `Admin`, `Organizador` e `Cliente`
- **Filtro JWT** (`JwtAuthFilter`) validando o token em cada requisição
- **Exceções customizadas** (`RegraNegocioException`, `SenhaInvalidaException`),
  retornando `400 Bad Request` com a mensagem do erro

## 🛠️ Tecnologias

- Java 17
- Spring Boot 2.5.0
- Spring Web
- Spring Data JPA
- Spring Security
- JWT (jjwt 0.9.1)
- PostgreSQL
- ModelMapper
- Lombok
- Swagger (Springfox 2.9.2)
- Maven

## ⚙️ Como rodar

### Pré-requisitos

- Java 17
- PostgreSQL

### Passo a passo

1. Clone o repositório:
   ```bash
   git clone https://github.com/Brunelias/Simbora-api.git
   cd Simbora-api
   ```

2. Crie o banco de dados vazio. As tabelas são criadas automaticamente
   pela aplicação na primeira execução:
   ```sql
   CREATE DATABASE [nome_do_banco];
   ```

3. Copie o arquivo de exemplo e preencha com seus dados:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```
   Edite o `application.properties` com o acesso ao banco e os dados do
   administrador inicial:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/simbora_db
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha

   app.admin.nome=Administrador
   app.admin.email=admin@exemplo.com
   app.admin.celular=00000000000
   app.admin.senha=troque_esta_senha
   ```
   
4. Execute a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
   (no Windows, pelo CMD ou PowerShell: `mvnw.cmd spring-boot:run`)

A API sobe em `http://localhost:8080`.

## 📚 Documentação (Swagger)

Com a aplicação rodando, acesse:
`http://localhost:8080/swagger-ui.html`

## 📌 Endpoints

Todas as rotas usam o prefixo `/api/v1`.

### Autenticação — `/api/v1/auth`

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/v1/auth` | Autentica o usuário e retorna o token JWT |
| GET | `/api/v1/auth/me` | Retorna os dados do usuário autenticado |
| PUT | `/api/v1/auth/alterar-senha` | Altera a senha do usuário autenticado |

**Exemplo de login:**

```json
POST /api/v1/auth
{
  "email": "usuario@email.com",
  "senha": "sua_senha"
}
```

**Resposta:**

```json
{
  "id": 1,
  "nome": "Nome do Usuário",
  "email": "usuario@email.com",
  "tipo": "CLIENTE",
  "token": "eyJhbGciOi..."
}
```

O campo `tipo` pode ser `ADMIN`, `ORGANIZADOR` ou `CLIENTE`. Nas rotas
protegidas, envie o token no cabeçalho:
`Authorization: Bearer <token>`

### Eventos — `/api/v1/eventos`

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/v1/eventos` | Lista todos os eventos |
| GET | `/api/v1/eventos/{id}` | Detalhes de um evento |
| POST | `/api/v1/eventos` | Cria um evento |
| PUT | `/api/v1/eventos/{id}` | Atualiza um evento |
| DELETE | `/api/v1/eventos/{id}` | Exclui um evento |

### Outros módulos

Lotes, ingressos, compras, clientes, organizadores e administradores seguem
o mesmo padrão REST. Veja todas as rotas no Swagger. Os módulos de
pagamento e check-in ainda estão em desenvolvimento.

## 🚧 Próximos passos

- [ ] Integração com gateway de pagamento
- [ ] Geração de QR Code para os ingressos e validação no check-in
- [ ] Testes automatizados

## 👤 Autor

**Bruno** · [Bruno Carvalho](https://www.linkedin.com/in/bruno-carvalho-b24ab3445/)
