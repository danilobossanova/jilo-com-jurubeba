# FACULDADE DE INFORMATICA E ADMINISTRACAO PAULISTA -- FIAP

## POS TECH -- ARQUITETURA E DESENVOLVIMENTO JAVA

## TECH CHALLENGE -- FASE 02

---

### jilo-com-jurubeba

### API REST para Gestao de Restaurantes com Clean Architecture

---

**Autores:**

- Danilo Fernando de Paula e Silva
- Gilmar da Costa Moraes Junior
- Juliana Maria Dal Olio Braz
- Luis Henrique Silveira Borges
- Thiago de Jesus Cordeiro

---

**Sao Paulo -- 2026**

---

## Sumario

- [I. Resumo Executivo](#i-resumo-executivo)
- [II. Objetivo do Projeto](#ii-objetivo-do-projeto)
- [III. Arquitetura da Solucao](#iii-arquitetura-da-solucao)
  - [3.1 Visao Geral -- Clean Architecture](#31-visao-geral--clean-architecture)
  - [3.2 Camada Domain](#32-camada-domain)
  - [3.3 Camada Application](#33-camada-application)
  - [3.4 Camada Interfaces](#34-camada-interfaces)
  - [3.5 Camada Infrastructure](#35-camada-infrastructure)
  - [3.6 Fluxo Tecnico de uma Requisicao](#36-fluxo-tecnico-de-uma-requisicao)
- [IV. Endpoints da API](#iv-endpoints-da-api)
  - [4.1 Autenticacao](#41-autenticacao)
  - [4.2 Saude](#42-saude)
  - [4.3 Usuarios](#43-usuarios)
  - [4.4 Tipos de Usuario](#44-tipos-de-usuario)
  - [4.5 Restaurantes](#45-restaurantes)
  - [4.6 Cardapio](#46-cardapio)
- [V. Contratos da API (DTOs)](#v-contratos-da-api-dtos)
- [VI. Seguranca](#vi-seguranca)
  - [6.1 Autenticacao JWT Stateless](#61-autenticacao-jwt-stateless)
  - [6.2 Tratamento de Erros (RFC 7807)](#62-tratamento-de-erros-rfc-7807)
- [VII. Persistencia e Configuracao](#vii-persistencia-e-configuracao)
  - [7.1 Entidades JPA](#71-entidades-jpa)
  - [7.2 Spring Profiles](#72-spring-profiles)
  - [7.3 Arquivos de Configuracao](#73-arquivos-de-configuracao)
  - [7.4 Variaveis de Ambiente](#74-variaveis-de-ambiente)
  - [7.5 Modelagem de Dados](#75-modelagem-de-dados)
- [VIII. Como Executar o Projeto](#viii-como-executar-o-projeto)
  - [8.1 Pre-requisitos](#81-pre-requisitos)
  - [8.2 Execucao Local](#82-execucao-local)
  - [8.3 Execucao por Profile](#83-execucao-por-profile)
  - [8.4 Docker Compose](#84-docker-compose)
  - [8.5 Usuario Administrador Padrao](#85-usuario-administrador-padrao)
- [IX. Testes e Qualidade](#ix-testes-e-qualidade)
  - [9.1 Estrategia de Testes](#91-estrategia-de-testes)
  - [9.2 Testes Unitarios](#92-testes-unitarios)
  - [9.3 Testes de Integracao](#93-testes-de-integracao)
  - [9.4 Testes de Arquitetura (ArchUnit)](#94-testes-de-arquitetura-archunit)
  - [9.5 Cobertura JaCoCo](#95-cobertura-jacoco)
  - [9.6 Spotless (Formatacao)](#96-spotless-formatacao)
  - [9.7 Como Executar os Testes](#97-como-executar-os-testes)
  - [9.8 Total de Testes](#98-total-de-testes)
  - [9.9 OpenAPI / Swagger](#99-openapi--swagger)
  - [9.10 Collection Postman](#910-collection-postman)
- [X. Geracao do Hash BCrypt](#x-geracao-do-hash-bcrypt)
- [XI. Proximos Passos](#xi-proximos-passos)
- [XII. Diagramas](#xii-diagramas)
- [XIII. Artefatos](#xiii-artefatos)
- [XIV. Referencias Bibliograficas](#xiv-referencias-bibliograficas)

---

## I. Resumo Executivo

O **jilo-com-jurubeba** e uma API REST desenvolvida em Java 21 com Spring Boot 3.5.10 para gestao de restaurantes, concebida como projeto academico do Tech Challenge da Pos-Graduacao em Arquitetura e Desenvolvimento Java da FIAP (Fase 02). O sistema permite que donos de restaurantes gerenciem seus estabelecimentos e cardapios, enquanto clientes consultam informacoes disponiveis publicamente.

A aplicacao foi projetada seguindo rigorosamente os principios da **Clean Architecture** (Robert C. Martin), organizada em **4 camadas** independentes: **Domain** (regras de negocio puras, sem dependencias de framework), **Application** (casos de uso e orquestracao de fluxos), **Interfaces** (adaptadores REST com controllers e DTOs de entrada/saida) e **Infrastructure** (implementacoes de persistencia JPA, seguranca Spring Security e configuracoes).

O projeto conta com **194 testes automatizados** distribuidos entre testes unitarios de dominio, testes unitarios de casos de uso com Mockito, testes de integracao com MockMvc e H2, e 35 testes de arquitetura via ArchUnit. A cobertura de codigo, medida pelo JaCoCo, alcanca **86%** de cobertura de linhas, superando o minimo obrigatorio de 80%.

A autenticacao e realizada via **JWT** (JSON Web Token) em modelo stateless, e a documentacao interativa da API esta disponivel via Swagger UI (SpringDoc OpenAPI 2.5.0). Uma collection Postman com **26 requisicoes** em 7 pastas permite a validacao completa de todos os endpoints, incluindo cenarios de erro, com resultado de 53 assertions passando e 0 falhas.

---

## II. Objetivo do Projeto

O objetivo da Fase 02 do Tech Challenge e expandir o sistema de gestao de restaurantes, implementando as seguintes funcionalidades e requisitos tecnicos:

1. **CRUD de Tipo de Usuario:** Gerenciamento dos papeis do sistema (MASTER, DONO_RESTAURANTE, CLIENTE), com validacao de unicidade de nome e normalizacao para UPPERCASE.

2. **CRUD de Usuario:** Cadastro e gerenciamento de usuarios com autenticacao, associacao obrigatoria a um tipo de usuario, validacao de CPF e email unicos, e criptografia de senha via BCrypt.

3. **CRUD de Restaurante:** Cadastro e gerenciamento de estabelecimentos vinculados a um usuario com papel DONO_RESTAURANTE, incluindo informacoes como endereco, tipo de cozinha, horario de funcionamento e regra de propriedade (apenas o dono pode atualizar).

4. **CRUD de Cardapio (Item do Menu):** Cadastro e gerenciamento de itens de cardapio vinculados a um restaurante, com informacoes como nome, descricao, preco, disponibilidade local e foto. A regra de propriedade garante que apenas o dono do restaurante pode atualizar seus itens.

5. **Clean Architecture:** Implementacao rigorosa em 4 camadas com separacao de responsabilidades, validada automaticamente por 35 testes ArchUnit que impedem violacoes de dependencia.

6. **Testes Automatizados:** Cobertura minima de 80% com testes unitarios, de integracao e de arquitetura, totalizando 194 testes.

7. **Docker Compose:** Containerizacao completa com MySQL 8.0 e a API Spring Boot para execucao integrada em qualquer ambiente.

8. **Documentacao Completa:** API documentada via Swagger (OpenAPI 3.0), collection Postman com 26 requisicoes e documentacao tecnica abrangente.

---

## III. Arquitetura da Solucao

### 3.1 Visao Geral -- Clean Architecture

O projeto adota a **Clean Architecture** proposta por Robert C. Martin (Uncle Bob), organizada em 4 camadas concentricas com dependencias apontando sempre para o centro (dominio). A seguinte representacao ilustra o fluxo de dependencias entre camadas:

```
+-----------------------------------------------------------+
|                      INTERFACES                           |
|       (Controllers REST, Request/Response DTOs, Mappers)  |
+-----------------------------------------------------------+
                            |
                            v
+-----------------------------------------------------------+
|                      APPLICATION                          |
|             (Use Cases, Input/Output DTOs)                |
+-----------------------------------------------------------+
                            |
                            v
+-----------------------------------------------------------+
|                        DOMAIN                             |
|    (Entities, Value Objects, Gateway Interfaces, Exceptions)|
+-----------------------------------------------------------+
                            ^
                            |
+-----------------------------------------------------------+
|                    INFRASTRUCTURE                         |
|      (JPA, Repositories, Spring Security, Config)         |
+-----------------------------------------------------------+
```

**Tabela de Responsabilidades por Camada:**

| Camada | Responsabilidade |
|--------|-----------------|
| **Domain** | Regras de negocio puras, entidades, value objects, interfaces de gateway (ports) e excecoes de dominio. ZERO dependencias de framework. |
| **Application** | Casos de uso (Use Cases), DTOs de entrada (Input) e saida (Output). Sem Spring, sem JPA. Orquestra o fluxo de negocio delegando para gateways. |
| **Interfaces** | Controllers REST, DTOs de requisicao (Request) e resposta (Response) com anotacoes OpenAPI e Bean Validation, RestMappers (MapStruct) e GlobalExceptionHandler (RFC 7807). |
| **Infrastructure** | Implementacoes JPA (JpaEntity, Repository, PersistenceMapper, GatewayJpa), Spring Security (JWT), configuracoes de beans (UseCaseConfig), data seeders e integracao com frameworks externos. |

**Regras de Dependencia entre Camadas (validadas por ArchUnit com 35 testes):**

| Camada | Pode depender de | NAO pode depender de |
|--------|-------------------|----------------------|
| **Domain** | Nada (nucleo puro) | Application, Interfaces, Infrastructure |
| **Application** | Domain | Interfaces, Infrastructure |
| **Interfaces** | Application, Domain | Infrastructure |
| **Infrastructure** | Application, Domain | Interfaces |

Essas regras sao validadas automaticamente a cada execucao de `./mvnw test` pelo framework ArchUnit, impedindo que qualquer desenvolvedor introduza dependencias indevidas entre camadas.

---

### 3.2 Camada Domain

**Pacote:** `com.grupo3.postech.jilocomjurubeba.domain`

A camada de dominio contem as regras de negocio mais criticas do sistema. Todas as classes nesta camada sao POJOs puros, sem nenhuma anotacao de framework (Spring, JPA, Lombok, MapStruct ou Jackson).

#### 3.2.1 Entidades de Dominio (`domain.entity`)

| Entidade | Pacote | Descricao |
|----------|--------|-----------|
| **TipoUsuario** | `domain.entity.tipousuario` | Define o papel do usuario no sistema (MASTER, DONO_RESTAURANTE, CLIENTE). Campos: `id`, `nome`, `descricao`, `ativo`, `criadoEm`, `atualizadoEm`. |
| **Usuario** | `domain.entity.usuario` | Representa um usuario do sistema com autenticacao. Campos: `id`, `nome`, `cpf` (VO), `email` (VO), `telefone`, `tipoUsuario`, `senhaHash`, `ativo`, `criadoEm`, `atualizadoEm`. |
| **Restaurante** | `domain.entity.restaurante` | Estabelecimento gerenciado por um usuario DONO_RESTAURANTE. Campos: `id`, `nome`, `endereco`, `tipoCozinha` (enum), `horaAbertura`, `horaFechamento`, `dono` (Usuario), `ativo`, `criadoEm`, `atualizadoEm`. |
| **Cardapio** | `domain.entity.cardapio` | Item vendido em um restaurante. Campos: `id`, `nome`, `descricao`, `preco`, `apenasNoLocal`, `caminhoFoto`, `restaurante`, `ativo`, `criadoEm`, `atualizadoEm`. |

**Caracteristicas das entidades de dominio:**

- **Dois construtores:** um de criacao (sem `id`, com valores default como `ativo = true`) e um de reconstituicao (completo, usado pelo PersistenceMapper para hidratar a entidade a partir do banco de dados).
- **Validacao no construtor:** lanca `ValidacaoException` caso campos obrigatorios sejam nulos ou vazios.
- **Comportamento rico:** metodos como `atualizarDados()`, `desativar()`, `ativar()`, `pertenceAoDono()` e `eDonoDeRestaurante()` encapsulam regras de negocio.
- **Soft delete:** campo `ativo` (boolean) controlado pelo metodo `desativar()` -- entidades nunca sao removidas fisicamente do banco.
- **Equals/hashCode por ID:** duas entidades sao consideradas iguais se possuirem o mesmo identificador.
- **Normalizacao:** nomes sao convertidos para UPPERCASE e trimmed automaticamente.

#### 3.2.2 Value Objects (`domain.valueobject`)

| Value Object | Tipo | Descricao |
|-------------|------|-----------|
| **Cpf** | Classe | Encapsula a validacao de CPF (11 digitos numericos, rejeita sequencias de digitos identicos). Imutavel. |
| **Email** | Classe | Encapsula a validacao de email (formato valido, normalizacao para lowercase + trim). Imutavel. |
| **TipoCozinha** | Enum | Tipos de cozinha disponiveis: `BRASILEIRA`, `ITALIANA`, `JAPONESA`, `MEXICANA`, `FRANCESA`, `CHINESA`, `INDIANA`, `ARABE`, `AMERICANA`, `OUTRA`. |

#### 3.2.3 Gateways (`domain.gateway`)

As interfaces de gateway (ports) definem os contratos que as implementacoes de infraestrutura devem cumprir, seguindo o principio de inversao de dependencia:

| Gateway | Pacote | Descricao |
|---------|--------|-----------|
| **TipoUsuarioGateway** | `domain.gateway.tipousuario` | Operacoes CRUD para TipoUsuario (salvar, buscarPorId, listarTodos, existsByNome, deletar). |
| **UsuarioGateway** | `domain.gateway.usuario` | Operacoes CRUD para Usuario (salvar, buscarPorId, listarTodos, buscarPorEmail, existsByCpf, existsByEmail). |
| **AutenticacaoGateway** | `domain.gateway.usuario` | Autenticacao de usuario (autenticar). |
| **CriptografiaSenhaGateway** | `domain.gateway.usuario` | Criptografia de senha (criptografar). |
| **RestauranteGateway** | `domain.gateway.restaurante` | Operacoes CRUD para Restaurante. |
| **CardapioGateway** | `domain.gateway.cardapio` | Operacoes CRUD para Cardapio. |

#### 3.2.4 Excecoes (`domain.exception`)

A hierarquia de excecoes de dominio segue um padrao consistente:

```
DominioException (abstract, extends RuntimeException)
+-- ValidacaoException              -> HTTP 400
+-- EntidadeNaoEncontradaException  -> HTTP 404
+-- RegraDeNegocioException         -> HTTP 422
```

| Excecao | HTTP Status | Descricao |
|---------|-------------|-----------|
| **ValidacaoException** | 400 Bad Request | Dados de entrada invalidos. Suporta `Map<String, String>` com erros por campo. |
| **EntidadeNaoEncontradaException** | 404 Not Found | Recurso nao encontrado. Armazena nome da entidade e identificador buscado. |
| **RegraDeNegocioException** | 422 Unprocessable Entity | Violacao de regra de negocio (ex: CPF duplicado, usuario nao e dono do restaurante). Suporta causa encadeada. |

---

### 3.3 Camada Application

**Pacote:** `com.grupo3.postech.jilocomjurubeba.application`

A camada de aplicacao contem os casos de uso (Use Cases) que orquestram os fluxos de negocio, delegando para os gateways definidos no dominio. Esta camada NAO possui dependencias de Spring, JPA ou qualquer outro framework.

#### 3.3.1 Use Cases (`application.usecase`)

O projeto implementa **21 Use Cases** mais os casos de autenticacao e verificacao de saude, organizados por entidade:

| Entidade | Use Cases | Total |
|----------|-----------|-------|
| **Autenticacao** | AutenticarUsuarioUseCase | 1 |
| **Saude** | VerificarSaudeUseCase | 1 |
| **TipoUsuario** | CriarTipoUsuarioUseCase, BuscarTipoUsuarioPorIdUseCase, ListarTiposUsuarioUseCase, AtualizarTipoUsuarioUseCase, DeletarTipoUsuarioUseCase | 5 |
| **Usuario** | CriarUsuarioUseCase, BuscarUsuarioUseCase, ListarUsuarioUseCase, AtualizarUsuarioUseCase, DeletarUsuarioUseCase | 5 |
| **Restaurante** | CriarRestauranteUseCase, BuscarRestauranteUseCase, ListarRestauranteUseCase, AtualizarRestauranteUseCase, DeletarRestauranteUseCase | 5 |
| **Cardapio** | CriarCardapioUseCase, BuscarCardapioUseCase, ListarCardapioUseCase, AtualizarCardapioUseCase, DeletarCardapioUseCase | 5 |
| **Total** | | **22** |

**Interfaces base (@FunctionalInterface):**

```java
UseCase<I, O>           // Com entrada e saida:  O executar(I input)
UseCaseSemEntrada<O>    // Sem entrada:           O executar()
UseCaseSemSaida<I>      // Sem saida:             void executar(I input)
```

**Registro de beans:** Os Use Cases NAO utilizam `@Component`. Sao registrados manualmente como beans Spring na classe `UseCaseConfig.java` (camada Infrastructure), mantendo a camada Application completamente livre de anotacoes Spring.

#### 3.3.2 DTOs (`application.dto`)

Os DTOs da camada Application sao Java Records puros, sem anotacoes de framework:

| Entidade | Input (Criacao) | Input (Atualizacao) | Output |
|----------|----------------|---------------------|--------|
| **Autenticacao** | AutenticarUsuarioInput(email, senha) | -- | AutenticacaoOutput(token) |
| **Saude** | -- | -- | SaudeOutput(status, versao) |
| **TipoUsuario** | CriarTipoUsuarioInput(nome, descricao) | AtualizarTipoUsuarioInput(id, nome, descricao) | TipoUsuarioOutput(id, nome, descricao, ativo) |
| **Usuario** | CriarUsuarioInput(nome, cpf, email, telefone, tipoUsuarioId, senha) | AtualizarUsuarioInput(id, nome, email, telefone) | UsuarioOutput(id, nome, cpf, email, telefone, tipoUsuario, ativo) |
| **Restaurante** | CriarRestauranteInput(nome, endereco, tipoCozinha, horaAbertura, horaFechamento, donoId) | AtualizarRestauranteInput(id, donoId, nome, endereco, tipoCozinha, horaAbertura, horaFechamento) | RestauranteOutput(id, nome, endereco, tipoCozinha, horaAbertura, horaFechamento, donoId, ativo) |
| **Cardapio** | CriarCardapioInput(nome, descricao, preco, apenasNoLocal, caminhoFoto, restauranteId) | AtualizarCardapioInput(id, donoId, nome, descricao, preco, apenasNoLocal, caminhoFoto) | CardapioOutput(id, nome, descricao, preco, apenasNoLocal, caminhoFoto, restauranteId, ativo) |

A conversao de entidade de dominio para Output e realizada dentro do proprio UseCase por meio de um metodo privado `toOutput()`, sem o uso de MapStruct, para nao acoplar a camada Application a frameworks de mapeamento.

---

### 3.4 Camada Interfaces

**Pacote:** `com.grupo3.postech.jilocomjurubeba.interfaces`

A camada de interfaces contem os adaptadores REST que expoe os endpoints da API HTTP.

#### 3.4.1 Controllers (`interfaces.rest.{entidade}`)

Cada entidade possui seu controller dedicado:

| Controller | Pacote | Endpoints |
|-----------|--------|-----------|
| **AuthController** | `interfaces.rest.auth` | POST /auth/login |
| **SaudeController** | `interfaces.rest` | GET /v1/health |
| **TipoUsuarioController** | `interfaces.rest.tipousuario` | CRUD /v1/tipos-usuario |
| **UsuarioController** | `interfaces.rest.usuario` | CRUD /v1/usuarios |
| **RestauranteController** | `interfaces.rest.restaurante` | CRUD /v1/restaurantes |
| **CardapioController** | `interfaces.rest.cardapio` | CRUD /v1/cardapios |

Os controllers sao "thin" (finos) -- delegam TODA a logica para os Use Cases:

```
Request -> Mapper.toInput() -> UseCase.executar(input) -> Mapper.toResponse(output) -> Response
```

Caracteristicas:

- `@RestController` + `@RequestMapping("/v1/{entidade-plural}")`
- `@Tag` do OpenAPI para agrupamento no Swagger
- `@Operation` + `@ApiResponses` em cada endpoint
- `@Valid` nos `@RequestBody`
- Logger SLF4J com `log.debug()` para rastreabilidade
- POST retorna `201 Created` com header `Location`
- DELETE retorna `204 No Content`
- Injecao de dependencia via construtor

#### 3.4.2 DTOs REST (`interfaces.rest.dto.{entidade}`)

DTOs de Request e Response com anotacoes OpenAPI (`@Schema`) e Bean Validation (`@NotBlank`, `@Size`, `@Valid`, `@Email`, `@NotNull`, `@Positive`):

| Entidade | Request (Criar) | Request (Atualizar) | Response |
|----------|----------------|---------------------|----------|
| **Auth** | LoginRequest(email, senha) | -- | LoginResponse(token) |
| **Saude** | -- | -- | SaudeResponse(status, versao) |
| **TipoUsuario** | CriarTipoUsuarioRequest(nome, descricao) | AtualizarTipoUsuarioRequest(nome, descricao) | TipoUsuarioResponse(id, nome, descricao, ativo) |
| **Usuario** | CriarUsuarioRequest(nome, cpf, email, telefone, tipoUsuarioId, senha) | AtualizarUsuarioRequest(nome, email, telefone) | UsuarioResponse(id, nome, cpf, email, telefone, tipoUsuario, ativo) |
| **Restaurante** | CriarRestauranteRequest(nome, endereco, tipoCozinha, horaAbertura, horaFechamento, donoId) | AtualizarRestauranteRequest(nome, endereco, tipoCozinha, horaAbertura, horaFechamento) | RestauranteResponse(id, nome, endereco, tipoCozinha, horaAbertura, horaFechamento, donoId, ativo) |
| **Cardapio** | CriarCardapioRequest(nome, descricao, preco, apenasNoLocal, caminhoFoto, restauranteId) | AtualizarCardapioRequest(nome, descricao, preco, apenasNoLocal, caminhoFoto) | CardapioResponse(id, nome, descricao, preco, apenasNoLocal, caminhoFoto, restauranteId, ativo) |

#### 3.4.3 RestMappers (`interfaces.rest.mapper.{entidade}`)

Mappers MapStruct com `@Mapper(componentModel = "spring")` que convertem Request para Input e Output para Response:

| Mapper | Conversoes |
|--------|-----------|
| **TipoUsuarioRestMapper** | CriarTipoUsuarioRequest -> CriarTipoUsuarioInput, TipoUsuarioOutput -> TipoUsuarioResponse |
| **UsuarioRestMapper** | CriarUsuarioRequest -> CriarUsuarioInput, UsuarioOutput -> UsuarioResponse |
| **RestauranteRestMapper** | CriarRestauranteRequest -> CriarRestauranteInput, RestauranteOutput -> RestauranteResponse |
| **CardapioRestMapper** | CriarCardapioRequest -> CriarCardapioInput, CardapioOutput -> CardapioResponse |

#### 3.4.4 GlobalExceptionHandler (`interfaces.rest.handler`)

Tratamento centralizado de excecoes utilizando o padrao RFC 7807 (`ProblemDetail`):

| Excecao | HTTP Status | Tipo RFC 7807 |
|---------|-------------|---------------|
| ValidacaoException | 400 | Dados de entrada invalidos com mapa de erros por campo |
| MethodArgumentNotValidException | 400 | Falha de Bean Validation com detalhes dos campos |
| EntidadeNaoEncontradaException | 404 | Recurso nao encontrado com identificador |
| RegraDeNegocioException | 422 | Violacao de regra de negocio |
| Exception (generica) | 500 | Erro interno do servidor |

---

### 3.5 Camada Infrastructure

**Pacote:** `com.grupo3.postech.jilocomjurubeba.infrastructure`

A camada de infraestrutura contem as implementacoes concretas dos contratos definidos pelo dominio, alem das configuracoes de framework.

#### 3.5.1 Persistencia (`infrastructure.persistence.{entidade}`)

Cada entidade possui um pacote dedicado com os seguintes componentes:

| Componente | Padrao de Nome | Descricao |
|-----------|----------------|-----------|
| **JpaEntity** | `{Entidade}JpaEntity` | Entidade JPA com anotacoes `@Entity`, `@Table`, `@Id`, Lombok (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`) e timestamps (`@CreationTimestamp`, `@UpdateTimestamp`). |
| **Repository** | `{Entidade}Repository` | Interface Spring Data JPA com metodos customizados (ex: `existsByNome()`, `findByEmail()`). |
| **PersistenceMapper** | `{Entidade}PersistenceMapper` | Mapper MapStruct com `@Mapper(componentModel = "spring")` que converte entre entidade de dominio e JpaEntity. Quando a entidade possui mais de um construtor, o metodo `toDomain()` e implementado manualmente como `default` method. |
| **GatewayJpa** | `{Entidade}GatewayJpa` | Implementacao do gateway do dominio usando Spring Data JPA. Anotado com `@Component`. |

**Entidades JPA implementadas:**

| JpaEntity | Tabela | Campos |
|-----------|--------|--------|
| TipoUsuarioJpaEntity | `tipos_usuario` | id, nome, descricao, ativo, criado_em, atualizado_em |
| UsuarioJpaEntity | `usuarios` | id, nome, cpf, email, telefone, tipo_usuario_id (FK), senha_hash, ativo, criado_em, atualizado_em |
| RestauranteJpaEntity | `restaurantes` | id, nome, endereco, tipo_cozinha, hora_abertura, hora_fechamento, dono_id (FK), ativo, criado_em, atualizado_em |
| CardapioJpaEntity | `cardapios` | id, nome, descricao, preco, apenas_no_local, caminho_foto, restaurante_id (FK), ativo, criado_em, atualizado_em |

#### 3.5.2 Configuracoes (`infrastructure.config`)

| Classe | Descricao |
|--------|-----------|
| **UseCaseConfig** | Registra todos os 22 Use Cases como beans Spring com `@Bean`, injetando as dependencias (Gateways, configs). Mantem a camada Application livre de `@Component`. |
| **OpenApiConfig** | Configuracao do SpringDoc OpenAPI (titulo, descricao, versao, esquema de seguranca JWT Bearer). |
| **TipoUsuarioDataSeeder** | `@Component` que implementa `CommandLineRunner`. Semeia 3 registros iniciais (MASTER, DONO_RESTAURANTE, CLIENTE) de forma idempotente. |
| **UsuarioDataSeeder** | `@Component` que implementa `CommandLineRunner`. Cria o usuario administrador padrao (`admin@jilocomjurubeba.com` / `admin123`) no profile H2. Idempotente. |

#### 3.5.3 Seguranca (`infrastructure.security`)

| Classe | Descricao |
|--------|-----------|
| **SecurityConfig** | Configura a cadeia de filtros de seguranca: CSRF desabilitado, sessao STATELESS, endpoints publicos e protegidos, filtro JWT, entry points RFC 7807 para 401/403. Ativado para todos os profiles exceto `test`. |
| **JwtService** | Servico para geracao e validacao de tokens JWT. Utiliza HMAC-SHA256 com segredo configuravel via propriedade `security.jwt.secret`. |
| **JwtAuthFilter** | Filtro `OncePerRequestFilter` que intercepta requisicoes, extrai o token JWT do header `Authorization: Bearer`, valida e configura o `SecurityContext`. |

---

### 3.6 Fluxo Tecnico de uma Requisicao

O fluxo completo de uma requisicao HTTP segue a seguinte sequencia:

```
Cliente HTTP
    |
    v
[JwtAuthFilter] -- Valida token JWT (se endpoint autenticado)
    |
    v
[Controller] -- Recebe Request DTO (@Valid)
    |
    v
[RestMapper] -- Converte Request -> Input (MapStruct)
    |
    v
[UseCase] -- Executa logica de negocio (executar())
    |
    v
[Gateway Interface] -- Contrato definido no Domain
    |
    v
[GatewayJpa] -- Implementacao com Spring Data JPA
    |
    v
[PersistenceMapper] -- Converte Domain <-> JpaEntity (MapStruct)
    |
    v
[Repository] -- Persiste no banco de dados
    |
    v
(Retorno pelo mesmo caminho)
    |
    v
[UseCase] -- Converte Entity -> Output (toOutput())
    |
    v
[RestMapper] -- Converte Output -> Response (MapStruct)
    |
    v
[Controller] -- Retorna ResponseEntity<Response>
    |
    v
Cliente HTTP (JSON)
```

Em caso de erro, o `GlobalExceptionHandler` intercepta a excecao e retorna uma resposta padronizada no formato RFC 7807 (`ProblemDetail`).

---

## IV. Endpoints da API

**URL Base:** `http://localhost:8080`

### 4.1 Autenticacao

**Observacao:** O endpoint de autenticacao NAO possui o prefixo `/v1/` (intencional).

| Metodo | Endpoint | Descricao | Auth |
|--------|----------|-----------|------|
| POST | `/auth/login` | Realiza login e retorna token JWT | Nao (Publico) |

**Exemplo de requisicao:**

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@jilocomjurubeba.com", "senha": "admin123"}'
```

**Exemplo de resposta (200 OK):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

Utilize o token retornado no header de autorizacao das demais requisicoes:

```
Authorization: Bearer <token>
```

### 4.2 Saude

| Metodo | Endpoint | Descricao | Auth |
|--------|----------|-----------|------|
| GET | `/v1/health` | Health check da API | Nao (Publico) |
| GET | `/actuator/health` | Health check do Spring Actuator | Nao (Publico) |

### 4.3 Usuarios

| Metodo | Endpoint | Descricao | Auth |
|--------|----------|-----------|------|
| POST | `/v1/usuarios` | Criar novo usuario | **Nao (Publico)** |
| GET | `/v1/usuarios` | Listar todos os usuarios | Sim (JWT) |
| GET | `/v1/usuarios/{id}` | Buscar usuario por ID | Sim (JWT) |
| PUT | `/v1/usuarios/{id}` | Atualizar usuario existente | Sim (JWT) |
| DELETE | `/v1/usuarios/{id}` | Desativar usuario (soft delete) | Sim (JWT) |

**Observacao:** O endpoint `POST /v1/usuarios` e publico para permitir o auto-cadastro de novos usuarios no sistema.

### 4.4 Tipos de Usuario

| Metodo | Endpoint | Descricao | Auth |
|--------|----------|-----------|------|
| POST | `/v1/tipos-usuario` | Criar novo tipo de usuario | Sim (JWT) |
| GET | `/v1/tipos-usuario` | Listar todos os tipos | Sim (JWT) |
| GET | `/v1/tipos-usuario/{id}` | Buscar tipo por ID | Sim (JWT) |
| PUT | `/v1/tipos-usuario/{id}` | Atualizar tipo existente | Sim (JWT) |
| DELETE | `/v1/tipos-usuario/{id}` | Desativar tipo (soft delete) | Sim (JWT) |

### 4.5 Restaurantes

| Metodo | Endpoint | Descricao | Auth |
|--------|----------|-----------|------|
| POST | `/v1/restaurantes` | Criar novo restaurante | Sim (JWT) |
| GET | `/v1/restaurantes` | Listar todos os restaurantes | **Nao (Publico)** |
| GET | `/v1/restaurantes/{id}` | Buscar restaurante por ID | **Nao (Publico)** |
| PUT | `/v1/restaurantes/{id}?donoId={id}` | Atualizar restaurante (somente o dono) | Sim (JWT) |
| DELETE | `/v1/restaurantes/{id}` | Desativar restaurante (soft delete) | Sim (JWT) |

**Observacao:** Os endpoints GET de restaurantes sao publicos para permitir que clientes consultem os estabelecimentos disponiveis.

### 4.6 Cardapio

| Metodo | Endpoint | Descricao | Auth |
|--------|----------|-----------|------|
| POST | `/v1/cardapios` | Criar novo item de cardapio | Sim (JWT -- DONO_RESTAURANTE ou MASTER) |
| GET | `/v1/cardapios` | Listar todos os itens | **Nao (Publico)** |
| GET | `/v1/cardapios/{id}` | Buscar item por ID | **Nao (Publico)** |
| PUT | `/v1/cardapios/{id}?donoId={id}` | Atualizar item (somente dono do restaurante) | Sim (JWT -- DONO_RESTAURANTE ou MASTER) |
| DELETE | `/v1/cardapios/{id}` | Desativar item (soft delete) | Sim (JWT -- DONO_RESTAURANTE ou MASTER) |

**Observacao:** Os endpoints GET de cardapio sao publicos para que clientes consultem os itens disponiveis. As operacoes de escrita (POST, PUT, DELETE) sao restritas a usuarios com papel DONO_RESTAURANTE ou MASTER.

---

## V. Contratos da API (DTOs)

### 5.1 LoginRequest

```json
{
  "email": "admin@jilocomjurubeba.com",
  "senha": "admin123"
}
```

| Campo | Tipo | Obrigatorio | Validacao |
|-------|------|-------------|-----------|
| email | String | Sim | @NotBlank, @Email |
| senha | String | Sim | @NotBlank |

### 5.2 LoginResponse

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 5.3 CriarTipoUsuarioRequest

```json
{
  "nome": "DONO_RESTAURANTE",
  "descricao": "Proprietario de restaurante"
}
```

| Campo | Tipo | Obrigatorio | Validacao |
|-------|------|-------------|-----------|
| nome | String | Sim | @NotBlank, @Size(max=50) |
| descricao | String | Sim | @NotBlank, @Size(max=255) |

### 5.4 TipoUsuarioResponse

```json
{
  "id": 1,
  "nome": "DONO_RESTAURANTE",
  "descricao": "Proprietario de restaurante",
  "ativo": true
}
```

### 5.5 CriarUsuarioRequest

```json
{
  "nome": "Joao da Silva",
  "cpf": "12345678901",
  "email": "joao@email.com",
  "telefone": "11999998888",
  "tipoUsuarioId": 1,
  "senha": "senha123"
}
```

| Campo | Tipo | Obrigatorio | Validacao |
|-------|------|-------------|-----------|
| nome | String | Sim | @NotBlank, @Size(max=100) |
| cpf | String | Sim | @NotBlank, @Size(min=11, max=11) |
| email | String | Sim | @NotBlank, @Email |
| telefone | String | Nao | -- |
| tipoUsuarioId | Long | Sim | @NotNull |
| senha | String | Sim | @NotBlank, @Size(min=6) |

### 5.6 AtualizarUsuarioRequest

```json
{
  "nome": "Joao da Silva",
  "email": "joao@email.com",
  "telefone": "11999998888"
}
```

| Campo | Tipo | Obrigatorio | Validacao |
|-------|------|-------------|-----------|
| nome | String | Sim | @NotBlank, @Size(max=100) |
| email | String | Nao | @Email |
| telefone | String | Nao | -- |

**Observacao:** CPF e tipo de usuario NAO sao alteraveis apos a criacao.

### 5.7 UsuarioResponse

```json
{
  "id": 1,
  "nome": "JOAO DA SILVA",
  "cpf": "12345678901",
  "email": "joao@email.com",
  "telefone": "11999998888",
  "tipoUsuario": "DONO_RESTAURANTE",
  "ativo": true
}
```

### 5.8 CriarRestauranteRequest

```json
{
  "nome": "Restaurante Bom Sabor",
  "endereco": "Rua das Flores, 123 - Centro",
  "tipoCozinha": "BRASILEIRA",
  "horaAbertura": "08:00",
  "horaFechamento": "22:00",
  "donoId": 1
}
```

| Campo | Tipo | Obrigatorio | Validacao |
|-------|------|-------------|-----------|
| nome | String | Sim | @NotBlank, @Size(max=100) |
| endereco | String | Sim | @NotBlank, @Size(max=255) |
| tipoCozinha | String | Sim | @NotBlank (enum TipoCozinha) |
| horaAbertura | LocalTime | Sim | @NotNull (formato HH:mm) |
| horaFechamento | LocalTime | Sim | @NotNull (formato HH:mm) |
| donoId | Long | Sim | @NotNull |

**Valores validos para tipoCozinha:** `BRASILEIRA`, `ITALIANA`, `JAPONESA`, `MEXICANA`, `FRANCESA`, `CHINESA`, `INDIANA`, `ARABE`, `AMERICANA`, `OUTRA`.

### 5.9 AtualizarRestauranteRequest

```json
{
  "nome": "Restaurante Bom Sabor",
  "endereco": "Rua das Flores, 456 - Centro",
  "tipoCozinha": "ITALIANA",
  "horaAbertura": "09:00",
  "horaFechamento": "23:00"
}
```

| Campo | Tipo | Obrigatorio | Validacao |
|-------|------|-------------|-----------|
| nome | String | Sim | @NotBlank, @Size(max=100) |
| endereco | String | Sim | @NotBlank, @Size(max=255) |
| tipoCozinha | String | Nao | -- |
| horaAbertura | LocalTime | Nao | -- |
| horaFechamento | LocalTime | Nao | -- |

**Observacao:** O `donoId` e fornecido como query parameter na URL (`?donoId={id}`), nao no corpo da requisicao.

### 5.10 RestauranteResponse

```json
{
  "id": 1,
  "nome": "RESTAURANTE BOM SABOR",
  "endereco": "Rua das Flores, 123 - Centro",
  "tipoCozinha": "BRASILEIRA",
  "horaAbertura": "08:00",
  "horaFechamento": "22:00",
  "donoId": 1,
  "ativo": true
}
```

### 5.11 CriarCardapioRequest

```json
{
  "nome": "Feijoada Completa",
  "descricao": "Feijoada completa com arroz, couve e farofa",
  "preco": 45.90,
  "apenasNoLocal": false,
  "caminhoFoto": "/imagens/feijoada.jpg",
  "restauranteId": 1
}
```

| Campo | Tipo | Obrigatorio | Validacao |
|-------|------|-------------|-----------|
| nome | String | Sim | @NotBlank, @Size(max=100) |
| descricao | String | Nao | @Size(max=500) |
| preco | BigDecimal | Sim | @NotNull, @Positive |
| apenasNoLocal | boolean | Nao | -- |
| caminhoFoto | String | Nao | -- |
| restauranteId | Long | Sim | @NotNull |

### 5.12 AtualizarCardapioRequest

```json
{
  "nome": "Feijoada Especial",
  "descricao": "Feijoada especial com carnes nobres",
  "preco": 55.90,
  "apenasNoLocal": true,
  "caminhoFoto": "/imagens/feijoada-especial.jpg"
}
```

| Campo | Tipo | Obrigatorio | Validacao |
|-------|------|-------------|-----------|
| nome | String | Sim | @NotBlank, @Size(max=100) |
| descricao | String | Nao | @Size(max=500) |
| preco | BigDecimal | Nao | @Positive |
| apenasNoLocal | boolean | Nao | -- |
| caminhoFoto | String | Nao | -- |

**Observacao:** O `donoId` e fornecido como query parameter na URL (`?donoId={id}`), nao no corpo da requisicao.

### 5.13 CardapioResponse

```json
{
  "id": 1,
  "nome": "FEIJOADA COMPLETA",
  "descricao": "Feijoada completa com arroz, couve e farofa",
  "preco": 45.90,
  "apenasNoLocal": false,
  "caminhoFoto": "/imagens/feijoada.jpg",
  "restauranteId": 1,
  "ativo": true
}
```

---

## VI. Seguranca

### 6.1 Autenticacao JWT Stateless

A aplicacao utiliza autenticacao baseada em **JSON Web Token (JWT)** em modelo completamente stateless:

- **Algoritmo:** HMAC-SHA256
- **Segredo:** Configuravel via propriedade `security.jwt.secret` ou variavel de ambiente `JWT_SECRET`
- **Expiracao:** 120 minutos (profile default) ou 1440 minutos (profile dev)
- **CSRF:** Desabilitado (API REST stateless, sem cookies de sessao -- sem vetor de ataque CSRF)
- **Sessao:** STATELESS (sem cookies de sessao do Spring Security)
- **Codificacao de senha:** BCrypt via `BCryptPasswordEncoder`

**Endpoints publicos (sem autenticacao):**

| Endpoint | Descricao |
|----------|-----------|
| `/auth/**` | Login e autenticacao |
| `/v1/health` | Health check da API |
| `/actuator/health` | Health check Spring Actuator |
| `/swagger-ui/**`, `/api-docs/**` | Documentacao OpenAPI |
| `POST /v1/usuarios` | Auto-cadastro de usuarios |
| `GET /v1/restaurantes/**` | Consulta publica de restaurantes |
| `GET /v1/cardapios/**` | Consulta publica de cardapios |

**Endpoints com restricao de papel (Role):**

| Endpoint | Papeis Permitidos |
|----------|-------------------|
| POST/PUT/PATCH/DELETE `/v1/cardapios/**` | DONO_RESTAURANTE, MASTER |
| `/v1/usuarios/**` (exceto POST) | DONO_RESTAURANTE, MASTER |
| `/v1/tipos-usuario/**` | DONO_RESTAURANTE, MASTER |

**Fluxo de autenticacao:**

1. O cliente envia `POST /auth/login` com email e senha.
2. O `AuthController` delega para o `AutenticarUsuarioUseCase`.
3. O `AutenticacaoGateway` (implementado por `JwtAutenticacaoGateway`) autentica via `AuthenticationManager` do Spring Security.
4. Se bem-sucedido, o `JwtService` gera um token JWT contendo o email do usuario e seus papeis.
5. O token e retornado no corpo da resposta.
6. Nas requisicoes subsequentes, o `JwtAuthFilter` intercepta o header `Authorization: Bearer {token}`, valida o token e configura o `SecurityContext`.

**Respostas de erro de seguranca (RFC 7807):**

| Cenario | HTTP Status | Descricao |
|---------|-------------|-----------|
| Token ausente, invalido ou expirado | 401 Unauthorized | ProblemDetail com detail "Token ausente, invalido ou expirado" |
| Usuario sem permissao para o recurso | 403 Forbidden | ProblemDetail com detail "Voce nao tem permissao para acessar este recurso" |

### 6.2 Tratamento de Erros (RFC 7807)

Todas as respostas de erro seguem o padrao **RFC 7807 Problem Details for HTTP APIs**, utilizando o objeto `ProblemDetail` do Spring Framework 6:

```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Dados de entrada invalidos",
  "instance": "/v1/usuarios"
}
```

| Excecao | HTTP Status | Cenario |
|---------|-------------|---------|
| ValidacaoException | 400 | Campos obrigatorios nulos/vazios, CPF invalido, email invalido |
| MethodArgumentNotValidException | 400 | Falha de Bean Validation (@NotBlank, @Size, @Email, etc.) |
| EntidadeNaoEncontradaException | 404 | Busca por ID inexistente |
| RegraDeNegocioException | 422 | CPF/email duplicado, usuario nao e dono do restaurante, tipo de usuario inexistente |
| Exception (generica) | 500 | Erro interno nao previsto |

A propriedade `spring.mvc.problemdetails.enabled=true` habilita o suporte nativo do Spring para respostas RFC 7807.

---

## VII. Persistencia e Configuracao

### 7.1 Entidades JPA

As entidades JPA sao separadas das entidades de dominio (principio da Clean Architecture). A conversao e realizada por PersistenceMappers (MapStruct).

| Tabela | JpaEntity | Relacoes |
|--------|-----------|----------|
| `tipos_usuario` | TipoUsuarioJpaEntity | -- |
| `usuarios` | UsuarioJpaEntity | FK `tipo_usuario_id` -> `tipos_usuario(id)` |
| `restaurantes` | RestauranteJpaEntity | FK `dono_id` -> `usuarios(id)` |
| `cardapios` | CardapioJpaEntity | FK `restaurante_id` -> `restaurantes(id)` |

### 7.2 Spring Profiles

| Profile | Banco de Dados | Flyway | DDL Auto | Descricao |
|---------|---------------|--------|----------|-----------|
| **(default)** | H2 em memoria | Desabilitado | `update` | Desenvolvimento local rapido, sem Docker. DDL auto-gerado pelo Hibernate. |
| **dev** | MySQL 8.0 | Habilitado (`classpath:db/migration/mysql`) | `update` | Usado pelo Docker Compose. Migracoes Flyway + DDL update. JWT expira em 24h. |
| **mysql** | MySQL 8.0 | Habilitado (`classpath:db/migration/mysql`) | `validate` | MySQL local (sem Docker). Flyway gerencia o schema, Hibernate apenas valida. |
| **postgres** | PostgreSQL 16 | Habilitado (`classpath:db/migration/postgresql`) | `validate` | Alternativa com PostgreSQL. Flyway gerencia o schema, Hibernate apenas valida. |
| **test** | H2 em memoria (herdado do default) | Desabilitado | `update` | Utilizado nos testes automatizados. Nao possui arquivo properties proprio -- funciona via `@Profile("!test")` que desativa todos os beans de seguranca (SecurityConfig, JwtService, JwtAuthFilter, UsuarioDetailsService, SpringPasswordHashGateway, JwtAutenticacaoGateway, PasswordHashBackfillRunner). |

### 7.3 Arquivos de Configuracao

| Arquivo | Descricao |
|---------|-----------|
| `application.properties` | Configuracao base (JPA, Actuator, SpringDoc, Logging, Jackson, JWT). H2 como banco padrao. Flyway desabilitado. |
| `application-dev.properties` | Profile de desenvolvimento com MySQL (usado pelo Docker Compose). Flyway habilitado com migracoes MySQL. JWT com expiracao de 24h. |
| `application-mysql.properties` | Profile MySQL para uso local (sem Docker). Flyway habilitado, `ddl-auto=validate`. |
| `application-postgres.properties` | Profile PostgreSQL. Flyway habilitado com migracoes PostgreSQL, `ddl-auto=validate`. |

**Nota:** Nao existe arquivo `application-test.properties`. O profile `test` funciona exclusivamente via anotacao `@Profile("!test")` aplicada nos beans de seguranca, que sao desativados durante os testes. Os testes herdam a configuracao do profile default (H2 em memoria).

**Configuracoes principais (application.properties):**

| Propriedade | Valor | Descricao |
|-------------|-------|-----------|
| `server.port` | `8080` | Porta do servidor |
| `spring.jpa.hibernate.ddl-auto` | `${JPA_DDL_AUTO:update}` | Estrategia de criacao/atualizacao de tabelas (configuravel via env var) |
| `spring.jpa.open-in-view` | `false` | Desabilitado para evitar lazy loading fora da transacao |
| `spring.flyway.enabled` | `false` | Desabilitado no profile default (H2). Habilitado em dev, mysql e postgres. |
| `security.jwt.secret` | `${JWT_SECRET:...fallback}` | Segredo para assinar tokens JWT (configuravel via env var) |
| `security.jwt.expiration-minutes` | `120` | Expiracao do token JWT em minutos (2h no default, 24h no profile dev) |
| `spring.jackson.default-property-inclusion` | `non_null` | Omite campos nulos do JSON |
| `spring.mvc.problemdetails.enabled` | `true` | Habilita RFC 7807 ProblemDetail |

### 7.4 Variaveis de Ambiente

| Variavel | Valor Padrao | Descricao |
|----------|-------------|-----------|
| `DB_HOST` | `mysql` (Docker) / `localhost` (local) | Host do banco de dados. Padrao varia conforme o profile. |
| `DB_PORT` | `3306` (MySQL) / `5432` (PostgreSQL) | Porta do banco. Docker mapeia externamente `3307:3306`. |
| `DB_NAME` | `jilocomjurubeba` | Nome do banco de dados |
| `DB_USER` | `jilocomjurubeba` | Usuario do banco |
| `DB_PASSWORD` | `jilocomjurubeba123` | Senha do banco |
| `JWT_SECRET` | `change-me-in-dev` (Docker) / fallback interno (local) | Segredo para assinar tokens JWT |
| `MASTER_PASSWORD_HASH` | BCrypt de `admin123` | Hash da senha do administrador (usado pelo Flyway seed). Ja possui valor padrao no `docker-compose.yml`. |
| `SPRING_PROFILES_ACTIVE` | *(nenhum = H2)* | Profile ativo. Usar `dev` para MySQL via Docker. |
| `JPA_DDL_AUTO` | `update` | Estrategia DDL do Hibernate. Sobreescrito para `validate` nos profiles `mysql` e `postgres`. |
| `JPA_SHOW_SQL` | `true` | Exibir queries SQL no log |
| `JPA_FORMAT_SQL` | `true` | Formatar queries SQL no log |

**Observacao importante:** A variavel `MASTER_PASSWORD_HASH` ja possui um valor padrao no `docker-compose.yml` correspondente ao hash BCrypt da senha `admin123`. Nao e necessario defini-la manualmente para desenvolvimento. O email do administrador padrao e `admin@jilocomjurubeba.com`. A variavel `JWT_EXPIRATION` presente no `docker-compose.yml` nao e lida diretamente pelo `application.properties` -- a expiracao e controlada pela propriedade `security.jwt.expiration-minutes` que possui valores fixos por profile (120 min no default, 1440 min no dev).

### 7.5 Modelagem de Dados

#### TipoUsuario

Define o papel do usuario no sistema. Os tres tipos padrao sao semeados automaticamente pelo `TipoUsuarioDataSeeder`:

| Tipo | Descricao |
|------|-----------|
| MASTER | Administrador com acesso total ao sistema |
| DONO_RESTAURANTE | Proprietario de restaurante que gerencia seus estabelecimentos e cardapios |
| CLIENTE | Usuario que consulta restaurantes e cardapios |

#### Usuario

Representa um usuario autenticavel do sistema. O CPF e o email devem ser unicos. A senha e armazenada como hash BCrypt. O campo `tipoUsuario` e uma referencia obrigatoria a um registro de `TipoUsuario`.

#### Restaurante

Estabelecimento vinculado a um usuario do tipo DONO_RESTAURANTE. O campo `tipoCozinha` e um enum com 10 opcoes. A regra de propriedade garante que apenas o dono pode atualizar ou desativar o restaurante.

#### Cardapio

Item de menu vinculado a um restaurante. Possui preco (`BigDecimal`), indicador de disponibilidade local (`apenasNoLocal`), e caminho de foto opcional. A regra de propriedade garante que apenas o dono do restaurante pode gerenciar os itens do cardapio.

---

## VIII. Como Executar o Projeto

### 8.1 Pre-requisitos

| Requisito | Versao | Obrigatorio |
|-----------|--------|-------------|
| **Java** | 21 ou superior | Sim |
| **Maven** | 3.9+ (ou usar o Maven Wrapper incluso: `./mvnw`) | Sim |
| **Docker** e **Docker Compose** | 24+ | Apenas para execucao com MySQL |

### 8.2 Execucao Local (H2 em Memoria)

A forma mais rapida de executar a aplicacao, sem necessidade de Docker:

```bash
# Clonar o repositorio
git clone https://github.com/danilobossanova/jilo-com-jurubeba.git
cd jilo-com-jurubeba

# Executar a aplicacao
./mvnw spring-boot:run
```

A aplicacao estara disponivel em: **http://localhost:8080**

| Recurso | URL |
|---------|-----|
| API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/api-docs |
| H2 Console | http://localhost:8080/h2-console |

**Credenciais do H2 Console:** JDBC URL: `jdbc:h2:mem:testdb`, usuario: `sa`, senha: *(vazia)*.

### 8.3 Execucao por Profile

#### Profile dev (MySQL local)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Requer MySQL rodando em `localhost:3306` com banco `jilocomjurubeba`, usuario `jilocomjurubeba`, senha `jilocomjurubeba123`.

#### Profile postgres (PostgreSQL local)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

Requer PostgreSQL rodando em `localhost:5432` com banco `jilocomjurubeba`.

### 8.4 Docker Compose

O arquivo `docker-compose.yml` sobe a stack completa com dois servicos:

| Servico | Container | Imagem | Porta Externa | Porta Interna |
|---------|-----------|--------|---------------|---------------|
| **mysql** | `jurubeba_mysql` | mysql:8.0 | 3307 | 3306 |
| **api** | `jurubeba_api` | Build local (Dockerfile) | 8080 | 8080 |

**Variaveis de ambiente configuradas no docker-compose.yml:**

| Variavel | Valor |
|----------|-------|
| `MYSQL_DATABASE` / `DB_NAME` | `jilocomjurubeba` |
| `MYSQL_USER` / `DB_USER` | `jilocomjurubeba` |
| `MYSQL_PASSWORD` / `DB_PASSWORD` | `jilocomjurubeba123` |
| `SPRING_PROFILES_ACTIVE` | `dev` |
| `MASTER_PASSWORD_HASH` | *(hash BCrypt de admin123 -- valor padrao incluso)* |

**Comandos:**

```bash
# Clonar e subir a stack completa
git clone https://github.com/danilobossanova/jilo-com-jurubeba.git
cd jilo-com-jurubeba

# Build e execucao
docker-compose up --build -d

# Verificar se os servicos subiram
docker-compose ps

# Ver logs da API
docker-compose logs -f api

# Parar os servicos
docker-compose down

# Parar e limpar volumes (dados do banco)
docker-compose down -v
```

**Health checks configurados:**

- **MySQL:** `mysqladmin ping` a cada 10s, com 10 tentativas.
- **API:** `wget` para `/actuator/health` a cada 30s, com 5 tentativas e 60s de start period.

O servico `api` depende do servico `mysql` com condicao `service_healthy`, garantindo que o banco esteja pronto antes de iniciar a aplicacao.

**Dockerfile (multi-stage build):**

| Estagio | Imagem Base | Descricao |
|---------|-------------|-----------|
| Build | `maven:3.9.9-eclipse-temurin-21-alpine` | Compila o projeto com Maven |
| Runtime | `eclipse-temurin:21-jre-alpine` | Imagem minima de execucao |

Caracteristicas do Dockerfile:

- Usuario nao-root (`spring`)
- JVM container-aware: `-XX:MaxRAMPercentage=75`
- Porta exposta: 8080

### 8.5 Usuario Administrador Padrao

A aplicacao cria automaticamente um usuario administrador MASTER no startup:

| Campo | Valor |
|-------|-------|
| **Email** | `admin@jilocomjurubeba.com` |
| **Senha** | `admin123` |
| **Tipo** | MASTER (acesso total) |

**Mecanismo de criacao:**

- **Profile default (H2):** O `UsuarioDataSeeder` (implementa `CommandLineRunner`) cria o usuario de forma idempotente ao iniciar a aplicacao.
- **Profile dev (MySQL) / postgres (PostgreSQL):** O Flyway seed script insere o usuario utilizando a variavel `${master_password_hash}` (hash BCrypt da senha `admin123`).

---

## IX. Testes e Qualidade

### 9.1 Estrategia de Testes

O projeto adota uma estrategia de testes em multiplas camadas, cobrindo desde as regras de negocio isoladas ate o fluxo HTTP completo, com validacao automatica da arquitetura:

| Tipo | Camada | Ferramentas | Qtd Aprox. |
|------|--------|-------------|------------|
| Unitario | Domain | JUnit 5, AssertJ | ~40 |
| Unitario | Application (UseCases) | JUnit 5, Mockito, AssertJ | ~60 |
| Unitario | Application (DTOs) | JUnit 5, ObjectMapper, AssertJ | ~30 |
| Integracao | Interfaces (Controllers) | MockMvc, @SpringBootTest, @WithMockUser, H2 | ~27 |
| Arquitetura | Transversal | ArchUnit (35 regras) | 35 |
| **Total** | | | **~193** |

### 9.2 Testes Unitarios

#### 9.2.1 Testes de Domain (~40 testes)

**Ferramentas:** JUnit 5, AssertJ

Testam as entidades de dominio (`TipoUsuario`, `Usuario`, `Restaurante`, `Cardapio`), Value Objects (`Cpf`, `Email`) e suas regras de negocio. Organizados com `@Nested` e `@DisplayName` em portugues.

**Cenarios testados:**

- Criacao com dados validos (dois construtores: criacao e reconstituicao)
- Validacao de campos obrigatorios (lanca `ValidacaoException`)
- Normalizacao de nomes (UPPERCASE + trim)
- Soft delete via `desativar()` e `ativar()`
- Igualdade por ID (`equals/hashCode`)
- Rejeicao de CPFs invalidos (todos digitos iguais, menos de 11 digitos, caracteres nao numericos)
- Normalizacao de email (lowercase + trim)
- Metodos de comportamento rico (`atualizarDados`, `pertenceAoDono`, `eDonoDeRestaurante`)

#### 9.2.2 Testes de Application - UseCases (~60 testes)

**Ferramentas:** JUnit 5, Mockito (`@ExtendWith(MockitoExtension.class)`, `@Mock`), AssertJ

Testam os 21 Use Cases com gateways mockados. NAO utilizam Spring Context. Cada UseCase possui pelo menos 2 testes (cenario de sucesso e cenario de erro).

**Cenarios testados:**

- `CriarUsuarioUseCase`: verifica unicidade de CPF/email, busca TipoUsuario, criptografa senha
- `AtualizarRestauranteUseCase`: valida que apenas o dono pode atualizar (regra de propriedade)
- `DeletarCardapioUseCase`: confirma soft delete (chama `desativar()` + `salvar()`, nunca `deletar()`)
- `AutenticarUsuarioUseCase`: delega para o gateway de autenticacao
- `VerificarSaudeUseCase`: retorna status UP e versao da aplicacao

#### 9.2.3 Testes de DTOs (~30 testes)

**Ferramentas:** JUnit 5, Jackson ObjectMapper, AssertJ

Testam a serializacao e deserializacao JSON dos Records (Input, Output, Request, Response), garantindo que os contratos da API nao quebram silenciosamente.

### 9.3 Testes de Integracao

**Ferramentas:** Spring Boot Test, MockMvc, H2, `@WithMockUser`, `@ActiveProfiles("test")`

Testam o fluxo completo HTTP: Request -> Controller -> UseCase -> Gateway -> JPA -> H2 -> Response. Utilizam o contexto Spring real com banco H2 em memoria.

| Controller | Testes | Cenarios |
|-----------|--------|----------|
| TipoUsuarioController | 9 | CRUD completo + validacoes + 404 + 422 |
| UsuarioController | 6 | CRUD completo + 404 |
| RestauranteController | 6 | CRUD completo + validacao de dono + 404 |
| CardapioController | 6 | CRUD completo + validacao de dono + 404 |
| SaudeController | 2 | Health check com status UP |
| **Total** | **~27** | |

**Convencoes:**

- `@DisplayName` em portugues: "Deve retornar status 200 ao listar todos"
- `@WithMockUser` para autenticacao nos testes de controller
- `greaterThanOrEqualTo()` em testes de listagem (evita falhas por ordem de execucao)

### 9.4 Testes de Arquitetura (ArchUnit)

**Ferramenta:** ArchUnit 1.2.1

O projeto possui **35 testes de arquitetura** organizados em 5 grupos, que validam automaticamente as regras estruturais da Clean Architecture escaneando todas as classes compiladas:

#### Grupo 1: Regras de Dependencia entre Camadas (5 testes)

Validam a **Dependency Rule** da Clean Architecture:

| Regra | O que garante |
|-------|---------------|
| Arquitetura em camadas | As 4 camadas respeitam a direcao de dependencia |
| Application !-> Infrastructure | A camada de aplicacao nunca importa classes de infraestrutura |
| Application !-> Interfaces | A camada de aplicacao nunca importa classes de interfaces REST |
| Domain !-> nada | O nucleo do dominio nao depende de nenhuma outra camada |
| Infrastructure !-> Interfaces | A infraestrutura nunca importa controllers ou DTOs REST |

#### Grupo 2: Independencia de Frameworks (10 testes)

Validam que Domain e Application sao livres de framework:

| Camada | Frameworks proibidos |
|--------|---------------------|
| Domain | Spring, JPA/Hibernate, Lombok, Jackson, Bean Validation |
| Application | Spring, JPA/Hibernate, Lombok, Jackson, Bean Validation |

#### Grupo 3: Localizacao de Anotacoes (5 testes)

| Anotacao | Local permitido |
|----------|----------------|
| `@Entity` (JPA) | `infrastructure.persistence` |
| `@Configuration` | `infrastructure` |
| `@RestController` | `interfaces.rest` |
| `@RestControllerAdvice` | `interfaces.rest` |
| `@Repository` | `infrastructure.persistence` |

#### Grupo 4: Regras Estruturais do Domain (2 testes)

| Regra | O que garante |
|-------|---------------|
| Gateways sao interfaces | Todas as classes em `domain.gateway` devem ser interfaces (ports) |
| Excecoes estendem DominioException | Hierarquia de excecoes consistente |

#### Grupo 5: Convencoes de Nomenclatura (13 testes)

| Componente | Sufixo obrigatorio | Exemplo |
|------------|-------------------|---------|
| Use Cases | `UseCase` | CriarRestauranteUseCase |
| Controllers | `Controller` | RestauranteController |
| Excecoes | `Exception` | ValidacaoException |
| REST Mappers | `RestMapper` | RestauranteRestMapper |
| Domain Gateways | `Gateway` | RestauranteGateway |
| JPA Entities | `JpaEntity` | RestauranteJpaEntity |
| Persistence Mappers | `PersistenceMapper` | RestaurantePersistenceMapper |
| Gateway Impls | contem `Gateway` | RestauranteGatewayJpa |
| Repositories | `Repository` | RestauranteRepository |
| Application Outputs | `Output` | RestauranteOutput |
| Application Inputs | `Input` | CriarRestauranteInput |
| REST Responses | `Response` | RestauranteResponse |
| REST Requests | `Request` | CriarRestauranteRequest |

**Importancia do ArchUnit para o projeto:**

1. **Previne degradacao arquitetural:** Sem ArchUnit, a Clean Architecture se degradaria gradualmente conforme novos desenvolvedores adicionam "atalhos" (ex: importar JPA no domain).
2. **Feedback imediato:** O desenvolvedor descobre a violacao no `./mvnw test`, nao em uma revisao de codigo dias depois.
3. **Documentacao viva:** Os 35 testes servem como documentacao executavel das regras arquiteturais.
4. **Escala com o projeto:** Cada nova classe adicionada e automaticamente validada contra as 35 regras.

### 9.5 Cobertura JaCoCo

| Metrica | Valor |
|---------|-------|
| **Meta minima configurada** | 80% de cobertura de linhas (BUNDLE level) |
| **Cobertura alcancada** | 86% |
| **Plugin** | JaCoCo 0.8.12 |
| **Relatorio** | `target/site/jacoco/index.html` |

Para gerar o relatorio de cobertura:

```bash
./mvnw test jacoco:report
```

O arquivo HTML gerado pode ser aberto em qualquer navegador para visualizacao detalhada por pacote e classe.

### 9.6 Spotless (Formatacao)

| Configuracao | Valor |
|-------------|-------|
| **Estilo** | Google Java Format -- variante AOSP (4 espacos de indentacao) |
| **Plugin** | Spotless 2.43.0 |
| **Fase Maven** | `validate` (build falha se codigo nao estiver formatado) |
| **Import order** | java\|javax, jakarta, org, com, com.grupo3.postech |
| **Regras adicionais** | Remove imports nao usados, trim whitespace, newline no final |

Para formatar o codigo (obrigatorio antes de cada commit):

```bash
./mvnw spotless:apply
```

Para verificar a formatacao sem aplicar:

```bash
./mvnw spotless:check
```

### 9.7 Como Executar os Testes

```bash
# Executar todos os 194 testes
./mvnw test

# Build completo (Spotless + Testes + JaCoCo)
./mvnw clean verify

# Apenas testes de arquitetura (ArchUnit)
./mvnw test -Dtest=CleanArchitectureTest

# Apenas testes de uma entidade especifica
./mvnw test -Dtest="*Restaurante*"

# Gerar relatorio de cobertura JaCoCo
./mvnw test jacoco:report

# Formatar codigo (obrigatorio antes de commit)
./mvnw spotless:apply
```

### 9.8 Total de Testes

O projeto possui **194 testes automatizados** distribuidos da seguinte forma:

| Categoria | Arquivos de Teste | Testes Aprox. |
|-----------|------------------|---------------|
| Domain (Entidades + VOs) | TipoUsuarioTest, UsuarioTest, RestauranteTest, CardapioTest, CpfTest, EmailTest | ~40 |
| Application (UseCases) | 5x TipoUsuario + 5x Usuario + 5x Restaurante + 5x Cardapio + 1x Saude + 1x Auth | ~60 |
| Application (DTOs) | 6 arquivos de teste de serializacao | ~30 |
| Interfaces (Controllers) | TipoUsuarioControllerTest, UsuarioControllerTest, RestauranteControllerTest, CardapioControllerTest, SaudeControllerTest | ~27 |
| Arquitetura | CleanArchitectureTest (5 grupos) | 35 |
| **Total** | **44 arquivos de teste** | **~194** |

### 9.9 OpenAPI / Swagger

A documentacao interativa da API esta disponivel via SpringDoc OpenAPI 2.8.6:

| Recurso | URL |
|---------|-----|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/api-docs |

Configuracoes:

- Operacoes ordenadas por metodo HTTP (`operationsSorter=method`)
- Tags ordenadas alfabeticamente (`tagsSorter=alpha`)
- Esquema de seguranca JWT Bearer configurado no `OpenApiConfig`

### 9.10 Collection Postman

Uma collection completa do Postman esta disponivel para validacao de todos os endpoints da API.

**Informacoes da collection:**

| Propriedade | Valor |
|-------------|-------|
| **Arquivo** | `docs/postman/Jilo-com-Jurubeba-API.postman_collection.json` |
| **Total de requisicoes** | 26 |
| **Total de pastas** | 7 (Auth, Saude, TipoUsuario, Usuario, Restaurante, Cardapio, Testes de Erro) |
| **Credenciais** | `admin@jilocomjurubeba.com` / `admin123` |
| **Variavel base_url** | `http://localhost:8080` |
| **Resultado dos testes** | 53 assertions passando / 0 falhas |

**Funcionalidades automaticas da collection:**

- **Auto-login:** Pre-request script que realiza login automaticamente se o token estiver vazio e o endpoint exigir autenticacao.
- **IDs dinamicos:** Os IDs criados nos POST sao salvos automaticamente em variaveis (`{{tipoUsuarioId}}`, `{{usuarioId}}`, `{{restauranteId}}`, `{{cardapioId}}`) para uso nos requests seguintes.
- **CPF e email unicos:** Scripts de pre-request geram CPF e email unicos para evitar conflitos de unicidade.
- **Testes negativos:** 4 testes de cenarios de erro (400, 404, 422) na pasta "Testes de Erro".

**Variaveis da collection:**

| Variavel | Valor Padrao | Descricao |
|----------|-------------|-----------|
| `base_url` | `http://localhost:8080` | URL base da API |
| `token` | *(preenchido automaticamente)* | Token JWT |
| `admin_login` | `admin@jilocomjurubeba.com` | Email do admin |
| `admin_password` | `admin123` | Senha do admin |
| `tipoUsuarioId` | *(preenchido automaticamente)* | ID do ultimo tipo criado |
| `usuarioId` | *(preenchido automaticamente)* | ID do ultimo usuario criado |
| `restauranteId` | *(preenchido automaticamente)* | ID do ultimo restaurante |
| `cardapioId` | *(preenchido automaticamente)* | ID do ultimo item criado |

**Passo a passo para importar no Postman:**

1. Abra o **Postman** (desktop ou versao web).
2. Clique no botao **"Import"** no canto superior esquerdo.
3. Na janela que abrir, escolha uma das opcoes:
   - **Arrastar e soltar:** arraste o arquivo diretamente para a janela do Postman.
   - **Upload:** clique em **"Upload Files"** e navegue ate:
     ```
     docs/postman/Jilo-com-Jurubeba-API.postman_collection.json
     ```
4. O Postman mostrara um preview da collection -- clique em **"Import"** para confirmar.
5. A collection **"Jilo com Jurubeba API"** aparecera na barra lateral esquerda com 7 pastas.

**Ordem sugerida de execucao:**

| # | Pasta | Descricao | Requests | Auth |
|---|-------|-----------|----------|------|
| 1 | **Auth** | Login e obtencao do token JWT | 1 | Nao |
| 2 | **Saude** | Verificacao de saude da API | 1 | Nao |
| 3 | **TipoUsuario** | CRUD de tipos de usuario | 5 | Sim |
| 4 | **Usuario** | CRUD de usuarios (POST e publico) | 5 | Misto |
| 5 | **Restaurante** | CRUD de restaurantes | 5 | Sim |
| 6 | **Cardapio** | CRUD de itens do cardapio | 5 | Sim |
| 7 | **Testes de Erro** | Cenarios negativos (400, 404, 422) | 4 | Sim |

**Executar todos os testes de uma vez (Runner):**

1. Clique com botao direito na collection -> **"Run collection"**
2. Marque todas as pastas (ou selecione as desejadas)
3. Clique em **"Run Jilo com Jurubeba API"**
4. O Postman executara todos os 26 requests em sequencia e mostrara os resultados dos testes

---

## X. Geracao do Hash BCrypt

Para o ambiente Docker com MySQL, a senha do administrador e armazenada como hash BCrypt no banco via Flyway seed. O `docker-compose.yml` ja inclui um hash padrao correspondente a senha `admin123`, tornando a geracao manual opcional.

**Senha padrao:** `admin123`

Caso seja necessario gerar um hash BCrypt customizado (por exemplo, para alterar a senha em producao), existem diversas formas:

**Via Java (linha de comando):**

```bash
java -cp "target/dependency/*" org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
```

**Via ferramenta online (apenas para desenvolvimento):**

Sites como [bcrypt-generator.com](https://bcrypt-generator.com/) podem ser utilizados para gerar hashes de teste. NAO utilize ferramentas online para senhas de producao.

**Definindo no Docker Compose:**

Para utilizar um hash customizado, defina a variavel de ambiente antes de executar o Docker Compose:

```bash
export MASTER_PASSWORD_HASH='$2a$10$SeuHashAqui'
docker-compose up --build -d
```

**Observacao:** Se a variavel `MASTER_PASSWORD_HASH` nao for definida, o `docker-compose.yml` utilizara o hash padrao da senha `admin123` automaticamente.

---

## XI. Proximos Passos

As seguintes evolucoes estao planejadas para futuras fases do projeto:

1. **Avaliacao de Restaurantes:** Implementar sistema de avaliacoes (notas e comentarios) por clientes, com calculo de media de avaliacao por restaurante.

2. **Busca e Filtragem Avancada:** Endpoints de busca por tipo de cozinha, localizacao (CEP/coordenadas), horario de funcionamento e faixa de preco.

3. **Paginacao e Ordenacao:** Implementar paginacao nos endpoints de listagem utilizando Spring Data Pageable.

4. **Upload de Imagens:** Substituir `caminhoFoto` (String) por upload real de imagens com armazenamento em servico de cloud storage (AWS S3 ou equivalente).

5. **Testes com Testcontainers:** Adicionar testes de integracao com MySQL real via Testcontainers para validar queries especificas do dialeto.

6. **Monitoramento e Observabilidade:** Integrar Micrometer + Prometheus + Grafana para metricas de performance e disponibilidade.

7. **Cache:** Implementar cache com Redis para endpoints de leitura com alto volume de acesso (GET restaurantes e cardapios).

8. **CI/CD:** Configurar pipeline de integracao e entrega continua com GitHub Actions.

9. **Kubernetes:** Criar manifests Kubernetes para deploy em cluster com auto-scaling.

10. **Eventos de Dominio:** Implementar eventos de dominio (ex: RestauranteCriado, CardapioAtualizado) para comunicacao assincrona entre bounded contexts.

---

## XII. Diagramas

### 12.1 Diagrama de Containers (C4 -- Nivel 2)

O diagrama de containers C4 ilustra os principais blocos de implantacao do sistema:

- **API Spring Boot** (Java 21): Aplicacao principal contendo as 4 camadas da Clean Architecture.
- **MySQL 8.0** (ou H2 em memoria): Banco de dados relacional para persistencia.
- **Cliente HTTP** (Postman/Swagger/Frontend): Consome a API REST.

*Nota: O diagrama completo esta disponivel em formato PlantUML no arquivo `docs/ClassDiagram_jilo-com-jurubeba.puml`.*

### 12.2 Diagrama de Componentes (C4 -- Nivel 3)

O diagrama de componentes detalha a organizacao interna da API Spring Boot, mostrando as 4 camadas e seus componentes:

- **Interfaces:** Controllers (Auth, Saude, TipoUsuario, Usuario, Restaurante, Cardapio), DTOs REST, RestMappers, GlobalExceptionHandler.
- **Application:** 22 Use Cases, DTOs Input/Output, interfaces funcionais base.
- **Domain:** 4 Entidades, 3 Value Objects, 6 Gateways (interfaces), 4 Excecoes.
- **Infrastructure:** 4 conjuntos de persistencia (JpaEntity + Repository + PersistenceMapper + GatewayJpa), SecurityConfig + JwtService + JwtAuthFilter, UseCaseConfig, DataSeeders.

*Nota: Os diagramas detalhados estao disponiveis em `docs/architecture/README.md` no formato Mermaid.*

---

## XIII. Artefatos

| Artefato | Localizacao |
|----------|-------------|
| **Repositorio GitHub** | https://github.com/danilobossanova/jilo-com-jurubeba |
| **Collection Postman** | `docs/postman/Jilo-com-Jurubeba-API.postman_collection.json` (no repositorio) |
| **Resultado dos Testes Postman** | `docs/postman/test_run.json` (no repositorio) |
| **Docker Compose** | `docker-compose.yml` (raiz do repositorio) |
| **Dockerfile** | `Dockerfile` (raiz do repositorio) |
| **Documentacao de Arquitetura** | `docs/architecture/README.md` |
| **ADR -- Clean Architecture** | `docs/adr/ADR-0001-clean-architecture.md` |
| **Convencoes de Codigo** | `docs/standards/conventions.md` |
| **Diagrama de Classes (PlantUML)** | `docs/ClassDiagram_jilo-com-jurubeba.puml` |
| **Swagger UI** | http://localhost:8080/swagger-ui.html (com a aplicacao rodando) |
| **Video de Apresentacao** | `13-42-16.mp4` (raiz do repositorio) |

---

## XIV. Referencias Bibliograficas

MARTIN, Robert C. **Clean Architecture: A Craftsman's Guide to Software Structure and Design.** 1. ed. New York: Prentice Hall, 2017.

FOWLER, Martin. **Patterns of Enterprise Application Architecture.** Boston: Addison-Wesley, 2002.

EVANS, Eric. **Domain-Driven Design: Tackling Complexity in the Heart of Software.** Boston: Addison-Wesley, 2003.

SPRING. **Spring Boot Reference Documentation.** Disponivel em: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/. Acesso em: mar. 2026.

SPRING. **Spring Security Reference.** Disponivel em: https://docs.spring.io/spring-security/reference/index.html. Acesso em: mar. 2026.

IETF. **RFC 7807 -- Problem Details for HTTP APIs.** Disponivel em: https://www.rfc-editor.org/rfc/rfc7807. Acesso em: mar. 2026.

IETF. **RFC 7519 -- JSON Web Token (JWT).** Disponivel em: https://www.rfc-editor.org/rfc/rfc7519. Acesso em: mar. 2026.

ARCHUNIT. **ArchUnit User Guide.** Disponivel em: https://www.archunit.org/userguide/html/000_Index.html. Acesso em: mar. 2026.

MAPSTRUCT. **MapStruct Reference Guide.** Disponivel em: https://mapstruct.org/documentation/stable/reference/html/. Acesso em: mar. 2026.

SPRINGDOC. **SpringDoc OpenAPI Documentation.** Disponivel em: https://springdoc.org/. Acesso em: mar. 2026.

---

**Grupo 3 -- Tech Challenge POSTECH FIAP -- Fase 2 -- jilo-com-jurubeba**

Sao Paulo, marco de 2026.
