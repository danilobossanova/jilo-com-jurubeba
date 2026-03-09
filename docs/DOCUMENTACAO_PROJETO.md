# Documentacao Tecnica - jilo-com-jurubeba

## 1. Visao geral

`jilo-com-jurubeba` e uma API REST para gestao de:

- tipos de usuario
- usuarios
- restaurantes
- itens de cardapio
- autenticacao JWT

Stack principal:

- Java 21
- Spring Boot 3.5.10
- Spring Web
- Spring Data JPA
- Spring Security + JWT
- MySQL/PostgreSQL (JPA)
- MongoDB (profile dedicado)
- Maven
- Docker / Docker Compose

Pacote raiz:

- `com.grupo3.postech.jilocomjurubeba`

## 2. Arquitetura (baseada nas classes e pacotes)

O projeto adota Clean Architecture com separacao por camadas.

### 2.1 Camadas

- `domain`
- `application`
- `infrastructure`

### 2.2 Pacotes e responsabilidades

#### `domain`

- `entity`
  - `usuario.Usuario`
  - `tipousuario.TipoUsuario`
  - `restaurante.Restaurante`
  - `cardapio.Cardapio`
- `valueobject`
  - `Cpf`
  - `Email`
- `gateway` (ports de saida)
  - `UsuarioGateway`
  - `TipoUsuarioGateway`
  - `RestauranteGateway`
  - `CardapioGateway`
- `exception`
  - `DominioException`
  - `RegraDeNegocioException`
  - `ValidacaoException`
  - `EntidadeNaoEncontradaException`
- `enumroles`
  - `TypeUsuario`
  - `TypeCozinha`

#### `application`

- `dto`
  - Inputs/Outputs dos casos de uso
- `usecase`
  - CRUD de Usuario, TipoUsuario, Restaurante e Cardapio
  - `saude.VerificarSaudeUseCase`

#### `infrastructure`

- `web.controller`
  - `AuthController`
  - `UsuarioController`
  - `TipoUsuarioController`
  - `RestauranteController`
  - `CardapioController`
  - `SaudeController`
- `web.dto`
  - `CriarCardapioRequest`
  - `AtualizarCardapioRequest`
- `web.exception`
  - `GlobalExceptionHandler`
- `security`
  - `SecurityConfig`
  - `JwtService`
  - `JwtAuthFilter`
  - `UsuarioDetailsService`
- `config`
  - configuracao de beans de UseCase
- `persistence`
  - entidades JPA
  - repositories Spring Data
  - adapters JPA para gateways do dominio

### 2.3 Fluxo de requisicao

1. Controller recebe HTTP.
2. Controller monta DTO de entrada.
3. UseCase executa regra de aplicacao.
4. UseCase chama Gateway (interface do dominio).
5. Adapter JPA implementa Gateway.
6. Repository persiste/busca no banco.
7. Resposta volta via DTO de saida.

## 3. Modelo funcional atual

Entidades de negocio implementadas:

- `TipoUsuario` (nome, descricao, ativo)
- `Usuario` (nome, cpf, email, telefone, tipoUsuario, senha, ativo)
- `Restaurante` (nome, endereco, typeCozinha, horario, dono, ativo)
- `Cardapio` (nome, descricao, preco, apenasNoLocal, caminhoFoto, restaurante, ativo)

Regras relevantes no dominio:

- CPF obrigatorio com 11 digitos numericos.
- Email deve conter `@`.
- Nome e campos obrigatorios validados nas entidades.
- Preco de cardapio deve ser maior que zero.
- Restaurante exige dono valido.
- Horario de abertura e fechamento nao pode ser igual.

## 4. Endpoints da API

Base local: `http://localhost:8080`

## 4.1 Autenticacao

### POST `/auth/login`

- Autenticacao por email/senha.
- Retorna token JWT.
- Publico (nao exige token).

Request:

```json
{
  "email": "admin@jilo.com",
  "senha": "123456"
}
```

Response `200`:

```json
{
  "token": "jwt-token-aqui"
}
```

## 4.2 Saude

### GET `/v1/health`

- Health check simples da API.
- Publico.

Response:

```json
{
  "status": "UP",
  "versao": "1.0.0",
  "timestamp": "2026-03-08T20:00:00Z"
}
```

## 4.3 Usuarios

Base: `/usuarios`

- `POST /usuarios` (publico)
- `GET /usuarios` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `GET /usuarios/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `PUT /usuarios/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `DELETE /usuarios/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)

Payload criar/atualizar:

```json
{
  "nome": "Maria Silva",
  "cpf": "12345678901",
  "email": "maria@email.com",
  "telefone": "11999999999",
  "tipoUsuarioId": 1,
  "senha": "123456"
}
```

Obs.: em `PUT`, o `id` vem da URL.

## 4.4 Tipos de usuario

Base: `/tipos-usuario`

- `POST /tipos-usuario` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `GET /tipos-usuario` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `GET /tipos-usuario/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `PUT /tipos-usuario/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)
- `DELETE /tipos-usuario/{id}` (ROLE_DONO_RESTAURANTE ou ROLE_MASTER)

Payload:

```json
{
  "nome": "DONO_RESTAURANTE",
  "descricao": "Proprietario de restaurante"
}
```

## 4.5 Restaurantes

Base: `/restaurantes`

- `GET /restaurantes` (publico)
- `GET /restaurantes/{id}` (publico)
- `POST /restaurantes` (autenticado)
- `PUT /restaurantes/{id}` (autenticado)
- `DELETE /restaurantes/{id}` (autenticado)

Payload criar/atualizar:

```json
{
  "nome": "Bistro Central",
  "endereco": "Rua A, 100",
  "typeCozinha": "BRASILEIRA",
  "horaAbertura": "08:00:00",
  "horaFechamento": "18:00:00",
  "donoId": 1
}
```

## 4.6 Cardapio

Rotas base aceitas pelo controller:

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

Payload criar/atualizar:

```json
{
  "nome": "Prato Executivo",
  "descricao": "Arroz, feijao e carne",
  "preco": 39.90,
  "apenasNoLocal": true,
  "caminhoFoto": "/imagens/prato.jpg",
  "restauranteId": 1
}
```

Obs.: quando a rota inclui `{restauranteId}`, ele prevalece sobre o `restauranteId` do body.

## 4.7 Endpoints tecnicos

- `GET /actuator/health` (publico)
- `GET /swagger-ui.html` (publico)
- `GET /api-docs` (publico)
- `GET /api-docs.yaml` (publico)

## 5. Autorizacao e seguranca

Configurada em `SecurityConfig` com JWT stateless.

- Token enviado no header: `Authorization: Bearer <token>`
- Login: `POST /auth/login`
- Endpoints publicos:
  - `/auth/**`
  - `GET /cardapios/**`
  - `GET /restaurantes/**`
  - `GET /restaurantes/*/cardapio/**`
  - `POST /usuarios`
  - `/actuator/health`
  - Swagger/OpenAPI
- Escrita em cardapio exige `ROLE_DONO_RESTAURANTE` ou `ROLE_MASTER`.
- `/usuarios/**` e `/tipos-usuario/**` exigem `ROLE_DONO_RESTAURANTE` ou `ROLE_MASTER`.

Erros seguem `application/problem+json` (ProblemDetail).

## 6. Persistencia e banco

Entidades JPA implementadas:

- `tipo_usuario`
- `usuario`
- `restaurante`
- `cardapio`

Profiles:

- default: datasource MySQL local definido em `application.properties`
- `postgres`: `application-postgres.properties`
- `mysql`: `application-mysql.properties`
- `mongodb`: `application-mongodb.properties` (desativa JPA)

Variaveis de ambiente mais usadas:

- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
- `MONGO_HOST`, `MONGO_PORT`, `MONGO_DB`
- `JWT_SECRET`

## 7. Configuracao e execucao

## 7.1 Pre-requisitos

- Java 21
- Maven 3.9+ (ou Maven Wrapper)
- Docker e Docker Compose (opcional)

## 7.2 Executar local com Maven Wrapper

Windows (PowerShell):

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

API: `http://localhost:8080`

## 7.3 Executar com profile especifico

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

## 7.4 Subir somente infraestrutura (desenvolvimento)

```powershell
docker-compose -f docker-compose.dev.yml up -d
```

Com MySQL adicional:

```powershell
docker-compose -f docker-compose.dev.yml --profile mysql up -d
```

Com MongoDB adicional:

```powershell
docker-compose -f docker-compose.dev.yml --profile mongodb up -d
```

## 7.5 Subir stack completa com Docker

```powershell
docker-compose up -d --build
```

## 8. Testes e qualidade

Executar testes:

```powershell
.\mvnw.cmd test
```

Arquitetura (ArchUnit):

```powershell
.\mvnw.cmd test -Dtest=CleanArchitectureTest
```

Cobertura (JaCoCo):

```powershell
.\mvnw.cmd test jacoco:report
```

Relatorio: `target/site/jacoco/index.html`

## 9. Documentacao interativa da API

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`
- OpenAPI YAML: `http://localhost:8080/api-docs.yaml`

