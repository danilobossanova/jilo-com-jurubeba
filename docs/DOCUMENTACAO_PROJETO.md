# jilo-com-jurubeba - Documentacao do Projeto

## Resumo

O `jilo-com-jurubeba` e uma API REST em Java/Spring Boot para gestao de restaurantes, desenvolvida como Tech Challenge da Pos-Tech FIAP (Fase 2). O sistema permite cadastro de tipos de usuario, usuarios, restaurantes e itens de cardapio, com autenticacao JWT e Clean Architecture em 4 camadas.

## 1. Arquitetura - Clean Architecture (4 Camadas)

```
INTERFACES (REST)  --->  APPLICATION (Use Cases)  --->  DOMAIN (Core)  <---  INFRASTRUCTURE (DB, Config, Security)
```

| Camada         | Pacote base             | Responsabilidade                    | Depende de             |
|----------------|-------------------------|-------------------------------------|------------------------|
| Domain         | `domain`                | Entidades, VOs, Gateways, Excecoes  | Nada (nucleo puro)     |
| Application    | `application`           | UseCases, DTOs Input/Output         | Domain                 |
| Interfaces     | `interfaces.rest`       | Controllers, Request/Response DTOs, Mappers REST | Application, Domain |
| Infrastructure | `infrastructure`        | JPA, Repositories, Security, Config | Application, Domain    |

### Regras de Dependencia (validadas por ArchUnit)

- Domain **NAO** importa Spring, JPA, Hibernate ou qualquer framework
- Application **NAO** importa Spring Web ou JPA
- Interfaces **NAO** importa Infrastructure
- Infrastructure **NAO** importa Interfaces

## 2. Entidades de Dominio

### TipoUsuario
Define o papel do usuario: MASTER, DONO_RESTAURANTE, CLIENTE.
- Campos: `nome`, `descricao`, `ativo`
- Soft delete via `desativar()`

### Usuario
Representa um usuario do sistema com autenticacao.
- Campos: `nome`, `cpf` (VO), `email` (VO), `telefone`, `tipoUsuario`, `senha`, `ativo`
- Associacao com TipoUsuario (obrigatorio)
- Validacao de CPF e Email nos Value Objects

### Restaurante
Estabelecimento gerenciado por um usuario DONO_RESTAURANTE.
- Campos: `nome`, `endereco`, `tipoCozinha` (enum), `horaAbertura`, `horaFechamento`, `dono` (Usuario), `ativo`
- Regra de negocio: apenas o dono pode atualizar o restaurante

### Cardapio (Item do Menu)
Item vendido em um restaurante.
- Campos: `nome`, `descricao`, `preco`, `apenasNoLocal`, `caminhoFoto`, `restaurante`, `ativo`
- Regra de negocio: apenas o dono do restaurante pode atualizar o item

## 3. Endpoints da API

Base: `http://localhost:8080`

### Autenticacao
| Metodo | Endpoint        | Descricao           | Auth |
|--------|-----------------|---------------------|------|
| POST   | /auth/login     | Login (retorna JWT) | Nao  |

### Saude
| Metodo | Endpoint   | Descricao       | Auth |
|--------|------------|-----------------|------|
| GET    | /v1/health | Health check    | Nao  |

### Tipo de Usuario (`/v1/tipos-usuario`)
| Metodo | Endpoint                   | Descricao              |
|--------|----------------------------|------------------------|
| POST   | /v1/tipos-usuario          | Criar tipo             |
| GET    | /v1/tipos-usuario          | Listar todos           |
| GET    | /v1/tipos-usuario/{id}     | Buscar por ID          |
| PUT    | /v1/tipos-usuario/{id}     | Atualizar              |
| DELETE | /v1/tipos-usuario/{id}     | Desativar (soft delete)|

### Usuario (`/v1/usuarios`)
| Metodo | Endpoint                   | Descricao              |
|--------|----------------------------|------------------------|
| POST   | /v1/usuarios               | Criar usuario          |
| GET    | /v1/usuarios               | Listar todos           |
| GET    | /v1/usuarios/{id}          | Buscar por ID          |
| PUT    | /v1/usuarios/{id}          | Atualizar              |
| DELETE | /v1/usuarios/{id}          | Desativar (soft delete)|

### Restaurante (`/v1/restaurantes`)
| Metodo | Endpoint                          | Descricao                      |
|--------|-----------------------------------|--------------------------------|
| POST   | /v1/restaurantes                  | Criar restaurante              |
| GET    | /v1/restaurantes                  | Listar todos                   |
| GET    | /v1/restaurantes/{id}             | Buscar por ID                  |
| PUT    | /v1/restaurantes/{id}?donoId={id} | Atualizar (somente o dono)     |
| DELETE | /v1/restaurantes/{id}             | Desativar (soft delete)        |

### Cardapio (`/v1/cardapios`)
| Metodo | Endpoint                        | Descricao                              |
|--------|---------------------------------|----------------------------------------|
| POST   | /v1/cardapios                   | Criar item de cardapio                 |
| GET    | /v1/cardapios                   | Listar todos                           |
| GET    | /v1/cardapios/{id}              | Buscar por ID                          |
| PUT    | /v1/cardapios/{id}?donoId={id}  | Atualizar (somente dono do restaurante)|
| DELETE | /v1/cardapios/{id}              | Desativar (soft delete)                |

## 4. DTOs (Contratos da API)

### Request DTOs (camada Interfaces)
Usam `@Schema` (OpenAPI), `@NotBlank`, `@Size`, `@Valid` (Bean Validation).

### Application DTOs
Java Records puros sem anotacoes de framework.

| Entidade     | Input                              | Output                            |
|--------------|------------------------------------|------------------------------------|
| TipoUsuario  | CriarTipoUsuarioInput(nome, descricao) | TipoUsuarioOutput(id, nome, descricao, ativo) |
| Usuario      | CriarUsuarioInput(nome, cpf, email, telefone, tipoUsuarioId, senha) | UsuarioOutput(id, nome, cpf, email, telefone, tipoUsuario, ativo) |
| Restaurante  | CriarRestauranteInput(nome, endereco, tipoCozinha, horaAbertura, horaFechamento, donoId) | RestauranteOutput(id, nome, endereco, tipoCozinha, horaAbertura, horaFechamento, donoId, ativo) |
| Cardapio     | CriarCardapioInput(nome, descricao, preco, apenasNoLocal, caminhoFoto, restauranteId) | CardapioOutput(id, nome, descricao, preco, apenasNoLocal, caminhoFoto, restauranteId, ativo) |

## 5. Seguranca

- Autenticacao via **JWT** (JSON Web Token)
- CSRF desabilitado (API stateless)
- Endpoints publicos: `/auth/login`, `/v1/health`, `/actuator/**`, `/swagger-ui/**`
- Demais endpoints requerem `Authorization: Bearer <token>`
- Implementacao segue Clean Architecture: `AutenticacaoGateway` (domain) -> `JwtAutenticacaoGateway` (infrastructure)

## 6. Tratamento de Erros (RFC 7807)

| Excecao                        | HTTP Status | Descricao                    |
|--------------------------------|-------------|------------------------------|
| `ValidacaoException`           | 400         | Dados de entrada invalidos   |
| `EntidadeNaoEncontradaException`| 404        | Recurso nao encontrado       |
| `RegraDeNegocioException`      | 422         | Violacao de regra de negocio |
| `MethodArgumentNotValidException`| 400       | Falha de Bean Validation     |
| `Exception` (generica)         | 500         | Erro interno                 |

## 7. Persistencia

- JPA/Hibernate com H2 (default) ou MySQL (profile `dev`)
- Entidades JPA separadas das entidades de dominio (Clean Arch)
- PersistenceMappers com MapStruct para conversao Domain <-> JPA
- Tabelas: `tipos_usuario`, `usuarios`, `restaurantes`, `cardapios`
- TipoUsuarioDataSeeder semeia dados iniciais (MASTER, DONO_RESTAURANTE, CLIENTE)

## 8. Testes

- **193 testes** no total
- Cobertura minima: **80%** (JaCoCo)
- Testes unitarios: Domain (JUnit 5 + AssertJ) e Application (Mockito)
- Testes de integracao: Controllers com MockMvc + H2
- Testes de arquitetura: ArchUnit (35 regras validando Clean Architecture)

## 9. Execucao

### Local (H2)
```bash
./mvnw spring-boot:run
```

### Docker (MySQL)
```bash
docker-compose up --build -d
```

### Build completo
```bash
./mvnw clean verify
```

## 10. Documentacao da API

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs
- Postman Collection: `docs/postman/Jilo-com-Jurubeba-API.postman_collection.json`

## 11. Autores

### Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian

- **Thiago de Jesus Cordeiro** - Desenvolvimento e Arquitetura
- **Juliana Maria Dal Olio Braz** - Desenvolvimento e Arquitetura
- **Luis Henrique Silveira Borges** - Desenvolvimento e Arquitetura
- **Gilmar da Costa Moraes Junior** - Desenvolvimento e Arquitetura
- **Danilo Fernando** - Desenvolvimento e Arquitetura
