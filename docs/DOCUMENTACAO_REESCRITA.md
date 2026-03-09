# jilo-com-jurubeba - Documentacao Reescrita

## Resumo

O `jilo-com-jurubeba` e uma API REST em Java/Spring Boot para gestao de usuarios, tipos de usuario, restaurantes e itens de cardapio, com autenticacao JWT e separacao arquitetural por camadas (`domain`, `application`, `infrastructure`).

A implementacao aplica principios de Clean Architecture:

- o dominio concentra regras de negocio
- casos de uso ficam na camada de aplicacao
- controllers, seguranca e persistencia ficam na infraestrutura
- integracoes com banco sao feitas por adapters que implementam gateways do dominio

## 1. Objetivo do projeto

Entregar uma API para:

- cadastrar e autenticar usuarios
- gerenciar perfis/tipos de usuario
- cadastrar restaurantes e seus dados operacionais
- cadastrar itens de cardapio por restaurante
- expor endpoints publicos para consulta e endpoints protegidos para escrita

## 2. Arquitetura baseada nos pacotes

Pacote raiz:

- `com.grupo3.postech.jilocomjurubeba`

### 2.1 Camada `domain`

Responsavel por regras de negocio puras, sem dependencia de framework.

Principais pacotes:

- `domain.entity`
  - `tipousuario.TipoUsuario`
  - `usuario.Usuario`
  - `restaurante.Restaurante`
  - `cardapio.Cardapio`
- `domain.valueobject`
  - `Cpf`
  - `Email`
- `domain.gateway`
  - `TipoUsuarioGateway`
  - `UsuarioGateway`
  - `RestauranteGateway`
  - `CardapioGateway`
- `domain.exception`
  - `DominioException`
  - `ValidacaoException`
  - `RegraDeNegocioException`
  - `EntidadeNaoEncontradaException`
- `domain.enumroles`
  - `TypeUsuario`
  - `TypeCozinha`

### 2.2 Camada `application`

Contem orquestracao dos fluxos de negocio.

Principais pacotes:

- `application.dto` (inputs/outputs)
- `application.usecase`
  - CRUD de `usuario`
  - CRUD de `tipousuario`
  - CRUD de `restaurante`
  - CRUD de `cardapio`
  - `saude.VerificarSaudeUseCase`
- `application.mapper` (transformacao entre dominio e DTOs)

### 2.3 Camada `infrastructure`

Responsavel por detalhes de framework, web, seguranca e banco.

Principais pacotes:

- `infrastructure.web.controller`
  - `AuthController`
  - `UsuarioController`
  - `TipoUsuarioController`
  - `RestauranteController`
  - `CardapioController`
  - `SaudeController`
- `infrastructure.security`
  - `SecurityConfig`
  - `JwtService`
  - `JwtAuthFilter`
  - `UsuarioDetailsService`
  - `AuthRequest` / `AuthResponse`
- `infrastructure.persistence`
  - entidades JPA
  - repositories Spring Data
  - adapters JPA para gateways do dominio
- `infrastructure.config`
  - configuracao de beans dos use cases
- `infrastructure.web.exception`
  - `GlobalExceptionHandler`

## 3. Fluxo tecnico de uma requisicao

1. O endpoint HTTP e recebido por um controller em `infrastructure.web.controller`.
2. O controller converte body/path params para DTOs de `application.dto`.
3. O use case em `application.usecase` executa a regra.
4. O use case depende de um gateway do dominio (`domain.gateway`).
5. O adapter JPA em `infrastructure.persistence.*` implementa o gateway.
6. O repository Spring Data persiste/consulta dados.
7. O retorno volta ao controller como DTO de saida.

## 4. Endpoints da API

Base local:

- `http://localhost:8080`

### 4.1 Autenticacao

- `POST /auth/login` (publico)

Request:

```json
{
  "email": "admin@jilo.com",
  "senha": "123456"
}
```

Response:

```json
{
  "token": "jwt-token"
}
```

### 4.2 Saude

- `GET /v1/health` (publico)
- `GET /actuator/health` (publico)

### 4.3 Usuarios (`/usuarios`)

- `POST /usuarios` (publico)
- `GET /usuarios` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `GET /usuarios/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `PUT /usuarios/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `DELETE /usuarios/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)

### 4.4 Tipos de usuario (`/tipos-usuario`)

- `POST /tipos-usuario` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `GET /tipos-usuario` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `GET /tipos-usuario/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `PUT /tipos-usuario/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `DELETE /tipos-usuario/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)

### 4.5 Restaurantes (`/restaurantes`)

- `GET /restaurantes` (publico)
- `GET /restaurantes/{id}` (publico)
- `POST /restaurantes` (autenticado)
- `PUT /restaurantes/{id}` (autenticado)
- `DELETE /restaurantes/{id}` (autenticado)

### 4.6 Cardapio

Rotas suportadas:

- `/cardapios`
- `/restaurantes/{restauranteId}/cardapio`

Endpoints:

- `GET /cardapios` (publico)
- `GET /cardapios/{id}` (publico)
- `POST /cardapios` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `PUT /cardapios/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `DELETE /cardapios/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `GET /restaurantes/{restauranteId}/cardapio` (publico)
- `GET /restaurantes/{restauranteId}/cardapio/{id}` (publico)
- `POST /restaurantes/{restauranteId}/cardapio` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `PUT /restaurantes/{restauranteId}/cardapio/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `DELETE /restaurantes/{restauranteId}/cardapio/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)

## 5. Contratos principais (DTOs)

### 5.1 Usuario

- `CriarUsuarioInput(nome, cpf, email, telefone, tipoUsuarioId, senha)`
- `AtualizarUsuarioInput(id, nome, cpf, email, telefone, tipoUsuarioId)`
- `UsuarioOutput(id, nome, cpf, email, telefone, tipoUsuarioId, tipoUsuarioNome, ativo)`

### 5.2 TipoUsuario

- `CriarTipoUsuarioInput(nome, descricao)`
- `AtualizarTipoUsuarioInput(id, nome, descricao)`
- `TipoUsuarioOutput(id, nome, descricao, ativo)`

### 5.3 Restaurante

- `CriarRestauranteInput(nome, endereco, typeCozinha, horaAbertura, horaFechamento, donoId)`
- `AtualizarRestauranteInput(id, nome, endereco, typeCozinha, horaAbertura, horaFechamento, donoId)`
- `RestauranteOutput(id, nome, endereco, typeCozinha, horaAbertura, horaFechamento, donoId, ativo)`

### 5.4 Cardapio

- `CriarCardapioInput(nome, descricao, preco, apenasNoLocal, caminhoFoto, restauranteId)`
- `AtualizarCardapioInput(id, nome, descricao, preco, apenasNoLocal, caminhoFoto, restauranteId)`
- `CardapioOutput(id, nome, descricao, preco, apenasNoLocal, caminhoFoto, restauranteId, ativo)`

## 6. Seguranca

A seguranca e definida em `SecurityConfig`:

- estrategia stateless com JWT
- filtro `JwtAuthFilter` antes de `UsernamePasswordAuthenticationFilter`
- login por `AuthenticationManager` e `UsuarioDetailsService`
- tratamento de erro de autenticacao/autorizacao com `ProblemDetail`

Header esperado para endpoints protegidos:

- `Authorization: Bearer <token>`

## 7. Tratamento de erros

`GlobalExceptionHandler` converte excecoes para `application/problem+json`:

- `EntidadeNaoEncontradaException` -> `404`
- `ValidacaoException` -> `400`
- `RegraDeNegocioException` -> `422`
- erro inesperado -> `500`

## 8. Persistencia e profiles

Entidades JPA mapeadas:

- `tipo_usuario`
- `usuario`
- `restaurante`
- `cardapio`

Arquivos de configuracao:

- `src/main/resources/application.properties`
- `src/main/resources/application-postgres.properties`
- `src/main/resources/application-mysql.properties`
- `src/main/resources/application-mongodb.properties`

Variaveis de ambiente importantes:

- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
- `MONGO_HOST`, `MONGO_PORT`, `MONGO_DB`
- `JWT_SECRET`

## 9. Configuracao e execucao

### 9.1 Requisitos

- Java 21
- Maven 3.9+ (ou Maven Wrapper)
- Docker e Docker Compose (opcional)

### 9.2 Execucao local

Windows (PowerShell):

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

### 9.3 Execucao por profile

PostgreSQL:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=postgres"
```

MySQL:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=mysql"
```

MongoDB:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=mongodb"
```

### 9.4 Docker

Infra apenas (dev):

```powershell
docker-compose -f docker-compose.dev.yml up -d
```

Stack completa:

```powershell
docker-compose up -d --build
```

## 10. Documentacao OpenAPI

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`
- OpenAPI YAML: `http://localhost:8080/api-docs.yaml`

## 11. Autores

- Danilo Fernando
- Gilmar da Costa Moraes Junior
- Juliana Maria Dal Olio Braz
- Luis Henrique Silveira Borges
- Thiago de Jesus Cordeiro

