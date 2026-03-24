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

## 8. Estrategia de Testes

O projeto possui **193 testes automatizados** organizados em 5 categorias, cobrindo desde regras de negocio isoladas ate o fluxo HTTP completo, com validacao automatica da arquitetura.

### 8.1 Testes Unitarios de Domain (~40 testes)

**Ferramentas:** JUnit 5, AssertJ

**O que testam:** As entidades de dominio (`TipoUsuario`, `Usuario`, `Restaurante`, `Cardapio`), Value Objects (`Cpf`, `Email`) e suas regras de negocio.

**Exemplos de cenarios testados:**
- Criacao com dados validos (dois construtores: criacao e reconstituicao)
- Validacao de campos obrigatorios (lanca `ValidacaoException`)
- Normalizacao de nomes (UPPERCASE + trim)
- Soft delete via `desativar()` e `ativar()`
- Igualdade por ID (`equals/hashCode`)
- Rejeicao de CPFs invalidos (todos digitos iguais, menos de 11 digitos)
- Normalizacao de email (lowercase + trim)
- Metodos de comportamento rico (`atualizarDados`, `pertenceAoDono`, `eDonoDeRestaurante`)

**Por que sao importantes:** Garantem que as regras de negocio funcionam independente de framework, banco de dados ou HTTP. Se o domain quebra, todo o sistema quebra.

### 8.2 Testes Unitarios de Application (~60 testes)

**Ferramentas:** JUnit 5, Mockito, AssertJ

**O que testam:** Os 21 Use Cases com seus gateways mockados. Cada UseCase tem pelo menos 2 testes: cenario de sucesso e cenario de erro.

**Exemplos de cenarios testados:**
- `CriarUsuarioUseCase`: verifica unicidade de CPF/email, busca TipoUsuario, criptografa senha
- `AtualizarRestauranteUseCase`: valida que apenas o dono pode atualizar (regra de propriedade)
- `DeletarCardapioUseCase`: confirma soft delete (chama `desativar()` + `salvar()`, nunca `deletar()`)
- `AutenticarUsuarioUseCase`: delega para o gateway de autenticacao

**Por que sao importantes:** Validam a orquestracao dos fluxos de negocio sem depender de banco de dados ou HTTP. Mockito isola os gateways, garantindo que os testes sao rapidos e deterministicos.

### 8.3 Testes Unitarios de DTOs (~30 testes)

**Ferramentas:** JUnit 5, Jackson ObjectMapper, AssertJ

**O que testam:** Serialiacao e deserializacao JSON dos Records de entrada/saida (Input, Output, Request, Response).

**Por que sao importantes:** Garantem que os contratos da API nao quebram silenciosamente quando campos sao renomeados ou tipos sao alterados.

### 8.4 Testes de Integracao de Controllers (~27 testes)

**Ferramentas:** Spring Boot Test, MockMvc, H2, `@WithMockUser`

**O que testam:** O fluxo completo HTTP: Request -> Controller -> UseCase -> Gateway -> JPA -> H2 -> Response. Usam o contexto Spring real com banco H2 em memoria.

**Controllers testados:**
- `TipoUsuarioController` (9 testes): CRUD completo + validacoes + 404 + 422
- `UsuarioController` (6 testes): CRUD completo + 404
- `RestauranteController` (6 testes): CRUD completo + validacao de dono + 404
- `CardapioController` (6 testes): CRUD completo + validacao de dono + 404
- `SaudeController` (2 testes): health check

**Por que sao importantes:** Validam que todo o stack funciona integrado — Spring Security, Bean Validation, JPA, MapStruct, Exception Handlers, RFC 7807. Se um mapper gera campo errado ou uma query falha, estes testes capturam.

### 8.5 Testes de Arquitetura - ArchUnit (35 testes)

**Ferramenta:** ArchUnit

**O que testam:** As regras estruturais da Clean Architecture, escaneando **todas as 128 classes compiladas** do projeto. Divididos em 5 grupos:

#### Grupo 1: Regras de Dependencia entre Camadas (5 testes)

Validam a **Dependency Rule** — o principio mais importante da Clean Architecture:

| Regra | O que garante |
|-------|---------------|
| Arquitetura em camadas | As 4 camadas (Domain, Application, Interfaces, Infrastructure) respeitam a direcao de dependencia |
| Application !-> Infrastructure | A camada de aplicacao nunca importa classes de infraestrutura |
| Application !-> Interfaces | A camada de aplicacao nunca importa classes de interfaces REST |
| Domain !-> nada | O nucleo do dominio nao depende de nenhuma outra camada |
| Infrastructure !-> Interfaces | A infraestrutura nunca importa controllers ou DTOs REST |

**Impacto:** Se um desenvolvedor acidentalmente importar um `JpaRepository` dentro de um `UseCase`, ou um `Controller` dentro de um `Gateway`, o build falha imediatamente.

#### Grupo 2: Independencia de Frameworks (10 testes)

Validam que as camadas internas (Domain e Application) sao **livres de framework**:

| Camada | Frameworks proibidos |
|--------|---------------------|
| Domain | Spring, JPA/Hibernate, Lombok, Jackson, Bean Validation |
| Application | Spring, JPA/Hibernate, Lombok, Jackson, Bean Validation |

**Impacto:** Garante que as regras de negocio podem ser testadas e reutilizadas sem Spring Boot. Se alguem adicionar `@Component` em um UseCase ou `@Entity` em uma entidade de dominio, o build falha.

#### Grupo 3: Localizacao de Anotacoes (5 testes)

Validam que anotacoes de framework estao nos locais corretos:

| Anotacao | Local permitido |
|----------|----------------|
| `@Entity` (JPA) | `infrastructure.persistence` |
| `@Configuration` | `infrastructure` |
| `@RestController` | `interfaces.rest` |
| `@RestControllerAdvice` | `interfaces.rest` |
| `@Repository` | `infrastructure.persistence` |

**Impacto:** Impede que controllers sejam criados dentro de `infrastructure` ou que entidades JPA vazem para o domain.

#### Grupo 4: Regras Estruturais do Domain (2 testes)

| Regra | O que garante |
|-------|---------------|
| Gateways sao interfaces | Todas as classes em `domain.gateway` devem ser interfaces (ports) |
| Excecoes estendem DominioException | Hierarquia de excecoes consistente |

**Impacto:** Garante a inversao de dependencia — o domain define contratos (interfaces), nunca implementacoes.

#### Grupo 5: Convencoes de Nomenclatura (13 testes)

| Componente | Sufixo obrigatorio | Exemplo |
|------------|-------------------|---------|
| Use Cases | `UseCase` | `CriarRestauranteUseCase` |
| Controllers | `Controller` | `RestauranteController` |
| Excecoes | `Exception` | `ValidacaoException` |
| REST Mappers | `RestMapper` | `RestauranteRestMapper` |
| Domain Gateways | `Gateway` | `RestauranteGateway` |
| JPA Entities | `JpaEntity` | `RestauranteJpaEntity` |
| Persistence Mappers | `PersistenceMapper` | `RestaurantePersistenceMapper` |
| Gateway Impls | contem `Gateway` | `RestauranteGatewayJpa` |
| Repositories | `Repository` | `RestauranteRepository` |
| Application Outputs | `Output` | `RestauranteOutput` |
| Application Inputs | `Input` | `CriarRestauranteInput` |
| REST Responses | `Response` | `RestauranteResponse` |
| REST Requests | `Request` | `CriarRestauranteRequest` |

**Impacto:** Cria consistencia na base de codigo. Qualquer desenvolvedor novo entende imediatamente o papel de cada classe pelo nome. Se um novo mapper for criado sem o sufixo correto, o build falha.

### 8.6 Por que o ArchUnit e essencial para este projeto

Em um projeto academico com multiplos desenvolvedores, o ArchUnit atua como **guardia automatico da arquitetura**:

1. **Previne degradacao:** Sem ArchUnit, a Clean Architecture se degradaria gradualmente conforme novos desenvolvedores adicionam "atalhos" (ex: importar JPA no domain).

2. **Feedback imediato:** O desenvolvedor descobre a violacao no `./mvnw test`, nao em uma revisao de codigo dias depois.

3. **Documentacao viva:** Os 35 testes servem como documentacao executavel das regras arquiteturais — mais confiavel que qualquer documento que pode ficar desatualizado.

4. **Escala com o projeto:** Cada nova classe adicionada ao projeto e automaticamente validada contra as 35 regras, sem necessidade de escrever novos testes.

### 8.7 Cobertura de Codigo (JaCoCo)

- **Meta minima:** 80% de cobertura de linhas
- **Exclusoes:** Classes de seguranca (`@Profile("!test")`), DTOs (Records gerados), JPA entities (Lombok), MapStruct implementations (geradas em compile-time)
- **Relatorio:** `target/site/jacoco/index.html` (gerado por `./mvnw test jacoco:report`)

### 8.8 Comandos para Executar Testes

```bash
# Todos os testes
./mvnw test

# Build completo (Spotless + testes + JaCoCo)
./mvnw clean verify

# Apenas testes de arquitetura
./mvnw test -Dtest=CleanArchitectureTest

# Apenas testes de uma entidade
./mvnw test -Dtest="*Restaurante*"

# Relatorio de cobertura
./mvnw test jacoco:report
```

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
