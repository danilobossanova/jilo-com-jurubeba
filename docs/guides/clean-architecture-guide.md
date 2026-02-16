# Clean Architecture na Pratica — Guia Completo com Exemplos do Projeto

> **Projeto:** jilo-com-jurubeba (Tech Challenge FIAP Pos-Tech Fase 2)
> **Stack:** Java 21, Spring Boot 3.5.10, Maven
> **Componente de referencia:** TipoUsuario (CRUD completo)

Este guia responde, com codigo real do nosso projeto, as duvidas mais comuns de quem esta implementando Clean Architecture pela primeira vez.

---

## Indice

1. [O que e Clean Architecture e por que usamos](#1-o-que-e-clean-architecture-e-por-que-usamos)
2. [As 4 camadas: o que faz cada uma](#2-as-4-camadas-o-que-faz-cada-uma)
3. [A Dependency Rule — a unica regra que importa](#3-a-dependency-rule--a-unica-regra-que-importa)
4. [Entidade de dominio vs Entidade JPA](#4-entidade-de-dominio-vs-entidade-jpa)
5. [Onde o Spring fica? Como nao vazar framework pro dominio](#5-onde-o-spring-fica-como-nao-vazar-framework-pro-dominio)
6. [Use Case e Service? E o tal Application Service?](#6-use-case-e-service-e-o-tal-application-service)
7. [DTO fica onde? Quem mapeia?](#7-dto-fica-onde-quem-mapeia)
8. [Validacao: Bean Validation no dominio ou no controller?](#8-validacao-bean-validation-no-dominio-ou-no-controller)
9. [@Transactional em qual camada?](#9-transactional-em-qual-camada)
10. [Repositorio e interface no dominio ou na application?](#10-repositorio-e-interface-no-dominio-ou-na-application)
11. [Como tratar excecoes? DomainException vs HTTP 400/404](#11-como-tratar-excecoes-domainexception-vs-http-400404)
12. [Seguranca (Spring Security) entra onde?](#12-seguranca-spring-security-entra-onde)
13. [Eventos de dominio vs eventos de aplicacao](#13-eventos-de-dominio-vs-eventos-de-aplicacao)
14. [Testes: o que testar em cada camada?](#14-testes-o-que-testar-em-cada-camada)
15. [Como organizar pacotes sem virar bagunca?](#15-como-organizar-pacotes-sem-virar-bagunca)
16. [Ate que ponto vale a pureza? Isso nao e burocracia?](#16-ate-que-ponto-vale-a-pureza-isso-nao-e-burocracia)

---

## 1. O que e Clean Architecture e por que usamos

Clean Architecture e um modelo proposto por Robert C. Martin (Uncle Bob) em 2012. A ideia central e simples:

> **As regras de negocio nao devem depender de nenhum framework, banco de dados, ou mecanismo de entrega (HTTP, CLI, fila, etc).**

Imagine que amanha voce precise trocar Spring por Quarkus, ou PostgreSQL por MongoDB. Em uma aplicacao bem estruturada, voce so muda a "casca" (infrastructure) — o core de negocio continua intacto.

### No nosso projeto

Nossas regras de negocio (ex: "o nome do TipoUsuario deve ser unico e em UPPERCASE") vivem em `domain/` e `application/`. Essas camadas **nao importam nenhuma classe do Spring, JPA ou Hibernate**. Isso e validado automaticamente por 35 testes ArchUnit que rodam a cada build.

---

## 2. As 4 camadas: o que faz cada uma

```
┌────────────────────────────────────────────┐
│            INTERFACES (REST)               │  ← Recebe HTTP, valida formato, devolve JSON
│  Controller, Request/Response DTOs         │
├────────────────────────────────────────────┤
│            APPLICATION (Use Cases)         │  ← Orquestra: "o que o sistema FAZ"
│  UseCases, Input/Output DTOs              │
├────────────────────────────────────────────┤
│            DOMAIN (Core)                   │  ← Regras de negocio: "o que o negocio E"
│  Entidades, Gateways (interfaces), Erros  │
├────────────────────────────────────────────┤
│            INFRASTRUCTURE (Adapters)       │  ← Implementa detalhes: banco, config, email
│  JPA Entity, Repository, GatewayJpa       │
└────────────────────────────────────────────┘
```

### 2.1 Domain — O coracao do negocio

**O que tem:** Entidades de dominio, Value Objects, interfaces Gateway, excecoes de negocio, eventos de dominio.

**O que NAO tem:** NENHUMA anotacao de framework. Zero `@Entity`, zero `@Component`, zero `@Autowired`. E Java puro.

**No nosso projeto:**
```java
// domain/entity/tipousuario/TipoUsuario.java
// Repare: nenhum import de Spring, JPA, Lombok ou qualquer framework
public class TipoUsuario {
    private Long id;
    private String nome;
    private String descricao;
    private boolean ativo;

    // Construtor de CRIACAO (sem id, ativo=true por padrao)
    public TipoUsuario(String nome, String descricao) {
        validarCamposObrigatorios(nome, descricao);
        this.nome = nome.trim().toUpperCase();
        this.descricao = descricao.trim();
        this.ativo = true;
    }

    // Construtor de RECONSTITUICAO (vem do banco, com todos os campos)
    public TipoUsuario(Long id, String nome, String descricao, boolean ativo) {
        validarCamposObrigatorios(nome, descricao);
        this.id = id;
        this.nome = nome.trim().toUpperCase();
        this.descricao = descricao.trim();
        this.ativo = ativo;
    }

    // Comportamento rico — a entidade SABE se atualizar
    public void atualizarDados(String nome, String descricao) {
        validarCamposObrigatorios(nome, descricao);
        this.nome = nome.trim().toUpperCase();
        this.descricao = descricao.trim();
    }

    // Soft delete — a entidade SABE se desativar
    public void desativar() { this.ativo = false; }
    public void ativar() { this.ativo = true; }
}
```

**Por que e POJO puro?** Se amanha precisar usar essa mesma regra de negocio em um microservico sem Spring, em um batch job, ou em um teste unitario simples — ela funciona. Sem container, sem contexto, sem nada.

### 2.2 Application — O que o sistema FAZ

**O que tem:** Use Cases (logica de orquestracao), DTOs de Input/Output.

**O que NAO tem:** Annotations Spring, JPA, nada de framework. Depende APENAS do domain.

**No nosso projeto:**
```java
// application/usecase/tipousuario/CriarTipoUsuarioUseCase.java
public class CriarTipoUsuarioUseCase
        implements UseCase<CriarTipoUsuarioInput, TipoUsuarioOutput> {

    private final TipoUsuarioGateway tipoUsuarioGateway; // Interface do DOMAIN

    public CriarTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    @Override
    public TipoUsuarioOutput executar(CriarTipoUsuarioInput input) {
        // 1. Regra de negocio: nome deve ser unico
        if (tipoUsuarioGateway.existePorNome(input.nome().trim().toUpperCase())) {
            throw new RegraDeNegocioException(
                "Ja existe um tipo de usuario com o nome '" + input.nome() + "'");
        }
        // 2. Cria a entidade (validacao acontece no construtor)
        TipoUsuario tipoUsuario = new TipoUsuario(input.nome(), input.descricao());
        // 3. Salva via Gateway (nao sabe se e JPA, MongoDB, ou arquivo)
        TipoUsuario salvo = tipoUsuarioGateway.salvar(tipoUsuario);
        // 4. Converte para Output (nunca expoe a entidade diretamente)
        return toOutput(salvo);
    }
}
```

**Perceba:** O UseCase nao sabe se por tras do `tipoUsuarioGateway` tem JPA, MongoDB ou um arquivo CSV. Ele so sabe que pode chamar `.salvar()` e `.existePorNome()`. Isso e **Inversao de Dependencia**.

### 2.3 Interfaces — O ponto de contato com o mundo externo

**O que tem:** Controllers REST, DTOs de Request/Response (com @Schema, @NotBlank), Mappers REST, Exception Handlers.

**Pode usar:** Spring Web, OpenAPI annotations, Bean Validation.

**No nosso projeto:**
```java
// interfaces/rest/tipousuario/TipoUsuarioController.java
@RestController
@RequestMapping("/v1/tipos-usuario")
public class TipoUsuarioController {

    // Injecao dos UseCases + Mapper
    private final CriarTipoUsuarioUseCase criarTipoUsuarioUseCase;
    private final TipoUsuarioRestMapper mapper;

    @PostMapping
    public ResponseEntity<TipoUsuarioResponse> criar(
            @Valid @RequestBody CriarTipoUsuarioRequest request) {

        CriarTipoUsuarioInput input = mapper.toInput(request);          // Request → Input
        TipoUsuarioOutput output = criarTipoUsuarioUseCase.executar(input); // Executa UseCase
        TipoUsuarioResponse response = mapper.toResponse(output);       // Output → Response

        URI location = URI.create("/v1/tipos-usuario/" + output.id());
        return ResponseEntity.created(location).body(response);
    }
}
```

**O Controller e "burro" de proposito.** Ele so faz: receber → converter → delegar → converter → responder. Zero logica de negocio.

### 2.4 Infrastructure — Os detalhes tecnicos

**O que tem:** JPA entities, Spring Data repositories, implementacao dos Gateways, configuracao de beans, Security, Data Seeders.

**Pode usar:** Spring, JPA, Lombok, Hibernate, qualquer framework.

**No nosso projeto:**
```java
// infrastructure/persistence/tipousuario/gateway/TipoUsuarioGatewayJpa.java
@Component
public class TipoUsuarioGatewayJpa implements TipoUsuarioGateway {

    private final TipoUsuarioRepository repository;     // Spring Data
    private final TipoUsuarioPersistenceMapper mapper;   // MapStruct

    @Override
    public TipoUsuario salvar(TipoUsuario tipoUsuario) {
        TipoUsuarioJpaEntity jpaEntity = mapper.toJpaEntity(tipoUsuario); // Domain → JPA
        TipoUsuarioJpaEntity salvo = repository.save(jpaEntity);           // Spring Data
        return mapper.toDomain(salvo);                                     // JPA → Domain
    }
}
```

---

## 3. A Dependency Rule — A unica regra que importa

```
INTERFACES ───→ APPLICATION ───→ DOMAIN ←─── INFRASTRUCTURE
```

> **Dependencias so apontam para dentro.** Nunca para fora.

| Camada           | Pode depender de          | NAO pode depender de               |
|------------------|---------------------------|-------------------------------------|
| `domain`         | **Nada**                  | application, interfaces, infrastructure |
| `application`    | domain                    | interfaces, infrastructure          |
| `interfaces`     | application, domain       | infrastructure                      |
| `infrastructure` | application, domain       | interfaces                          |

**Como garantimos isso no projeto?** Com **35 testes ArchUnit** que rodam automaticamente:

```java
// CleanArchitectureTest.java — Grupo 1
@Test
@DisplayName("Domain nao deve depender de nenhuma outra camada")
void domainNaoDeveDependeDeOutrasCamadas() {
    noClasses()
        .that().resideInAPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAnyPackage("..application..", "..interfaces..", "..infrastructure..")
        .check(classes);
}
```

Se alguem colocar um `import org.springframework` dentro de `domain/`, **o build quebra**. Isso nao e convencao — e **garantia automatizada**.

---

## 4. Entidade de dominio vs Entidade JPA

Esta e provavelmente a duvida mais comum. Temos DUAS classes para a "mesma coisa":

### Entidade de Dominio (domain/)
```java
// domain/entity/tipousuario/TipoUsuario.java
public class TipoUsuario {        // POJO puro
    private Long id;
    private String nome;           // Validacao no construtor
    private String descricao;
    private boolean ativo;
    // Comportamento: atualizarDados(), desativar(), ativar()
    // Equals/hashCode por id
}
```

### Entidade JPA (infrastructure/)
```java
// infrastructure/persistence/tipousuario/entity/TipoUsuarioJpaEntity.java
@Entity                            // JPA
@Table(name = "tipos_usuario")     // JPA
@Getter @Setter                    // Lombok
@NoArgsConstructor @AllArgsConstructor // Lombok
public class TipoUsuarioJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nome;

    @Column(nullable = false, length = 255)
    private String descricao;

    private boolean ativo = true;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;     // so existe no JPA

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm; // so existe no JPA
}
```

### Por que a separacao?

| Aspecto                | Entidade Dominio           | Entidade JPA                |
|------------------------|----------------------------|-----------------------------|
| **Proposito**          | Representar regras de negocio | Mapear tabela do banco    |
| **Anotacoes**          | Zero                       | @Entity, @Column, @Id, Lombok |
| **Validacao**          | No construtor (lanca excecao) | Via constraints do banco   |
| **Comportamento**      | Metodos ricos              | Getters/Setters (anemico)  |
| **Timestamps**         | Nao tem (nao e negocio)    | criadoEm, atualizadoEm     |
| **Construtores**       | 2 (criacao + reconstituicao) | Padrao JPA (no-args)       |
| **Framework**          | Nenhum                     | JPA, Hibernate, Lombok     |

### A conversao entre elas

Quem converte? O `TipoUsuarioPersistenceMapper` (MapStruct):

```java
// infrastructure/persistence/tipousuario/mapper/TipoUsuarioPersistenceMapper.java
@Mapper(componentModel = "spring")
public interface TipoUsuarioPersistenceMapper {

    // JPA → Domain (manual, porque TipoUsuario tem 2 construtores)
    default TipoUsuario toDomain(TipoUsuarioJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        return new TipoUsuario(
            jpaEntity.getId(),
            jpaEntity.getNome(),
            jpaEntity.getDescricao(),
            jpaEntity.isAtivo());
    }

    // Domain → JPA (MapStruct gera automaticamente)
    @Mapping(target = "criadoEm", ignore = true)    // Hibernate gerencia
    @Mapping(target = "atualizadoEm", ignore = true) // Hibernate gerencia
    TipoUsuarioJpaEntity toJpaEntity(TipoUsuario tipoUsuario);
}
```

### "Isso nao e verboso demais?"

Sim, sao mais arquivos. Mas pense assim:

1. **Sem separacao:** Voce coloca `@Entity` na classe de dominio. Agora sua regra de negocio depende do JPA. Se precisar trocar pra MongoDB, precisa reescrever a entidade. E pior: os testes unitarios precisam do contexto JPA pra rodar.

2. **Com separacao:** Sua entidade de dominio roda em qualquer lugar. Teste unitario? 0ms, sem Spring. Trocar de banco? So muda a JpaEntity e o GatewayJpa. O dominio nem percebe.

**A "verbosidade" e o preco que voce paga por independencia.** E um bom negocio quando o projeto vai crescer (e o nosso vai — Usuario, Restaurante, Cardapio).

---

## 5. Onde o Spring fica? Como nao vazar framework pro dominio

### Regra de ouro

| Camada           | Spring/JPA permitido? | Exemplos que ficam aqui              |
|------------------|-----------------------|--------------------------------------|
| `domain`         | **NAO**               | Entidades, Gateways (interfaces), Excecoes |
| `application`    | **NAO**               | UseCases, DTOs Input/Output          |
| `interfaces`     | **SIM** (Spring Web)  | Controllers, Request/Response, OpenAPI |
| `infrastructure` | **SIM** (tudo)        | JPA, Security, Config, Seeders       |

### Como evitamos que o Spring "vaze" pro dominio?

**Problema real:** UseCases precisam ser beans Spring para serem injetados no Controller. A solucao obvia seria colocar `@Component` no UseCase. Mas isso faria o UseCase depender do Spring.

**Nossa solucao:** Registramos os beans **manualmente** em `UseCaseConfig.java`:

```java
// infrastructure/config/UseCaseConfig.java
@Configuration
public class UseCaseConfig {

    @Bean
    public CriarTipoUsuarioUseCase criarTipoUsuarioUseCase(TipoUsuarioGateway gateway) {
        return new CriarTipoUsuarioUseCase(gateway);
    }

    @Bean
    public BuscarTipoUsuarioPorIdUseCase buscarTipoUsuarioPorIdUseCase(TipoUsuarioGateway gateway) {
        return new BuscarTipoUsuarioPorIdUseCase(gateway);
    }

    // ... demais use cases
}
```

**Resultado:** O UseCase e um POJO puro. O Spring so aparece na infrastructure, que e a camada que **pode** conhecer tudo.

### Validacao com ArchUnit

Se alguem esquecer e colocar `@Service` num UseCase, o teste abaixo falha:

```java
@Test
@DisplayName("Application nao deve usar anotacoes Spring")
void applicationNaoDeveUsarAnnotacoesSpring() {
    noClasses()
        .that().resideInAPackage("..application..")
        .should().beAnnotatedWith("org.springframework.stereotype.Service")
        .orShould().beAnnotatedWith("org.springframework.stereotype.Component")
        .check(classes);
}
```

---

## 6. Use Case e Service? E o tal Application Service?

### Comparacao

| Conceito                | Em Clean Architecture       | Em MVC Tradicional          |
|-------------------------|-----------------------------|-----------------------------|
| Logica de orquestracao  | UseCase                     | Service (`@Service`)        |
| Onde fica               | `application/usecase/`      | `service/`                  |
| Anotacao                | **Nenhuma**                 | `@Service`, `@Component`    |
| Responsabilidade        | **Uma unica operacao**      | Varias operacoes agrupadas  |

### Como evitamos o "GodService"

No MVC tradicional, e comum ter:

```java
// MVC tradicional — o temido GodService
@Service
public class TipoUsuarioService {
    public TipoUsuario criar(...) { ... }
    public TipoUsuario buscar(...) { ... }
    public List<TipoUsuario> listar() { ... }
    public TipoUsuario atualizar(...) { ... }
    public void deletar(...) { ... }
    // + 20 metodos que so crescem...
}
```

No nosso projeto, cada operacao e uma classe:

```
application/usecase/tipousuario/
├── CriarTipoUsuarioUseCase.java       ← 1 classe = 1 operacao
├── BuscarTipoUsuarioPorIdUseCase.java
├── ListarTiposUsuarioUseCase.java
├── AtualizarTipoUsuarioUseCase.java
└── DeletarTipoUsuarioUseCase.java
```

**Vantagens:**
- **Single Responsibility:** cada UseCase faz UMA coisa
- **Testes menores:** testar `CriarTipoUsuarioUseCase` nao envolve `listar` ou `deletar`
- **Facil de encontrar:** "onde fica a logica de criacao?" → `CriarTipoUsuarioUseCase.java`
- **Nao cresce descontrolado:** em vez de um Service com 50 metodos, voce tem 50 classes pequenas

### A interface funcional

Todos os UseCases implementam uma das 3 interfaces:

```java
UseCase<I, O>           // Com entrada e saida:  O executar(I input)
UseCaseSemEntrada<O>    // Sem entrada:          O executar()
UseCaseSemSaida<I>      // Sem saida:            void executar(I input)
```

Isso padroniza. Todo UseCase tem um metodo `executar()`. E como a interface e `@FunctionalInterface`, pode ser usada com lambdas se necessario.

---

## 7. DTO fica onde? Quem mapeia?

### Temos DTOs em DUAS camadas

```
┌──────────────────────────────────────────────────────────────┐
│  INTERFACES (REST)                                           │
│  ┌──────────────────────┐     ┌───────────────────────────┐  │
│  │ CriarTipoUsuarioRequest │  │ TipoUsuarioResponse       │  │
│  │ @NotBlank, @Schema      │  │ @Schema                   │  │
│  └──────────┬───────────┘     └───────────▲───────────────┘  │
│             │ RestMapper                  │ RestMapper        │
│             ▼                             │                   │
├──────────────────────────────────────────────────────────────┤
│  APPLICATION (Use Cases)                                      │
│  ┌──────────────────────┐     ┌───────────────────────────┐  │
│  │ CriarTipoUsuarioInput│     │ TipoUsuarioOutput         │  │
│  │ (Java Record puro)   │     │ (Java Record puro)        │  │
│  └──────────────────────┘     └───────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
```

### Por que dois niveis de DTO?

| DTO                      | Camada       | Tem anotacoes? | Proposito                         |
|--------------------------|-------------|-----------------|-----------------------------------|
| `CriarTipoUsuarioRequest` | interfaces  | `@NotBlank`, `@Schema` | Validar HTTP, documentar Swagger |
| `CriarTipoUsuarioInput`   | application | **Nenhuma**     | Transportar dados pro UseCase     |
| `TipoUsuarioOutput`       | application | **Nenhuma**     | Retorno do UseCase               |
| `TipoUsuarioResponse`     | interfaces  | `@Schema`       | Formato da resposta JSON          |

**"Nao da pra usar um so?"** Da. Mas ai voce acopla. O dia que o Swagger precisa de um campo extra (ex: links HATEOAS), voce teria que mudar o DTO de application. Ou se um job batch chamar o UseCase, teria que importar OpenAPI annotations sem usar.

### Quem mapeia?

- **RestMapper** (interfaces): Request → Input, Output → Response
- **PersistenceMapper** (infrastructure): Domain ↔ JpaEntity

Ambos usam **MapStruct** (`@Mapper(componentModel = "spring")`), que gera o codigo em tempo de compilacao — zero overhead em runtime.

---

## 8. Validacao: Bean Validation no dominio ou no controller?

### Tres niveis de validacao

```
HTTP Request
    │
    ▼
┌─────────────────────────────────────────────────┐
│ CONTROLLER (@Valid)                              │
│ Valida: formato (nao nulo, tamanho max, regex)  │
│ Exemplo: @NotBlank, @Size(max=50)               │
│ Resultado: HTTP 400 com erros por campo          │
└────────────────────┬────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────┐
│ USE CASE (regras de aplicacao)                   │
│ Valida: regras que envolvem consulta ao banco    │
│ Exemplo: "nome deve ser unico"                   │
│ Resultado: RegraDeNegocioException → HTTP 422    │
└────────────────────┬────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────┐
│ ENTIDADE DE DOMINIO (construtor)                 │
│ Valida: invariantes da entidade                  │
│ Exemplo: "nome nao pode ser nulo"                │
│ Resultado: ValidacaoException → HTTP 400         │
└─────────────────────────────────────────────────┘
```

### No nosso projeto — Nivel 1: Controller

```java
// interfaces/rest/dto/tipousuario/CriarTipoUsuarioRequest.java
public record CriarTipoUsuarioRequest(
    @NotBlank(message = "Nome e obrigatorio")
    @Size(max = 50, message = "Nome deve ter no maximo 50 caracteres")
    String nome,

    @NotBlank(message = "Descricao e obrigatoria")
    @Size(max = 255, message = "Descricao deve ter no maximo 255 caracteres")
    String descricao
) {}
```

Isso valida **formato**. Se nome vier vazio, nem chega ao UseCase — retorna 400 imediatamente.

### No nosso projeto — Nivel 2: UseCase

```java
// CriarTipoUsuarioUseCase.java
if (tipoUsuarioGateway.existePorNome(input.nome().trim().toUpperCase())) {
    throw new RegraDeNegocioException(
        "Ja existe um tipo de usuario com o nome '" + input.nome() + "'");
}
```

Isso valida **regra de negocio** que depende do estado do sistema (banco). Retorna 422.

### No nosso projeto — Nivel 3: Entidade

```java
// TipoUsuario.java (construtor)
private void validarCamposObrigatorios(String nome, String descricao) {
    if (nome == null || nome.trim().isEmpty()) {
        throw new ValidacaoException("Nome do tipo de usuario e obrigatorio");
    }
    if (descricao == null || descricao.trim().isEmpty()) {
        throw new ValidacaoException("Descricao do tipo de usuario e obrigatoria");
    }
}
```

Isso e a **ultima barreira**. Mesmo que alguem chame `new TipoUsuario(null, null)` de um job batch (sem controller), a validacao funciona. A entidade protege seus proprios invariantes.

### "Nao e redundante validar 'nome nao nulo' duas vezes?"

Nao. Cada validacao tem um **proposito diferente**:
- **Controller:** feedback rapido pro usuario HTTP, com mensagens formatadas por campo
- **Entidade:** protecao dos invariantes, funciona mesmo sem HTTP

Se amanha voce criar um CLI ou um Kafka consumer que cria TipoUsuario, a validacao do controller nao existe — mas a da entidade continua protegendo.

---

## 9. @Transactional em qual camada?

### Resposta curta

No nosso projeto: **nao usamos `@Transactional` explicitamente (ainda)**. O Spring Data JPA ja gerencia transacoes automaticamente no `repository.save()`.

### Onde ficaria quando necessario?

Na **infrastructure**, nunca no dominio ou application:

```java
// ERRADO — vazamento de framework no UseCase
@Transactional // ← PROIBIDO em application
public class CriarTipoUsuarioUseCase { ... }

// CERTO — na infrastructure (GatewayJpa)
@Component
public class TipoUsuarioGatewayJpa implements TipoUsuarioGateway {

    @Transactional  // ← Permitido em infrastructure
    @Override
    public TipoUsuario salvar(TipoUsuario tipoUsuario) { ... }
}
```

**Por que?** `@Transactional` e do Spring. O UseCase nao pode depender do Spring. A responsabilidade de controlar transacao e um **detalhe de infraestrutura**.

### Para operacoes complexas (futuro)

Se um UseCase precisar salvar em 2 tabelas atomicamente, temos duas opcoes:
1. Colocar `@Transactional` no Gateway que faz as duas operacoes
2. Criar um `TransactionManager` como servico de infrastructure injetado via interface

---

## 10. Repositorio e interface no dominio ou na application?

### A duvida: Ports & Adapters vs Clean Architecture

Em **Hexagonal Architecture (Ports & Adapters)**, as portas ficam assim:
```
application/
├── ports/
│   ├── in/     ← Ports de entrada (UseCases)
│   └── out/    ← Ports de saida (Repositories)
```

Em **Clean Architecture (Uncle Bob)**, as interfaces ficam no dominio:
```
domain/
├── gateway/    ← Interfaces de persistencia
```

### Nossa escolha: Uncle Bob

```java
// domain/gateway/tipousuario/TipoUsuarioGateway.java
public interface TipoUsuarioGateway {
    TipoUsuario salvar(TipoUsuario tipoUsuario);
    Optional<TipoUsuario> buscarPorId(Long id);
    List<TipoUsuario> listarTodos();
    boolean existePorNome(String nome);
    // ...
}
```

**Por que no dominio?** Porque a entidade TipoUsuario precisa ser persistida — e o dominio e quem define esse contrato. O dominio diz "EU PRECISO de alguem que saiba salvar". A infrastructure diz "EU SEI como salvar (com JPA)".

A implementacao fica na infrastructure:
```java
// infrastructure/persistence/tipousuario/gateway/TipoUsuarioGatewayJpa.java
@Component
public class TipoUsuarioGatewayJpa implements TipoUsuarioGateway {
    // Usa Spring Data + MapStruct internamente
}
```

### Ambas abordagens sao validas

| Abordagem        | Gateway em...    | Quando usar                          |
|------------------|------------------|--------------------------------------|
| Uncle Bob        | `domain/gateway/` | Quando o dominio define o contrato  |
| Hexagonal        | `application/ports/out/` | Quando a application orquestra   |

Nos escolhemos Uncle Bob porque o projeto segue Clean Architecture de ponta a ponta, e manter consistencia e mais importante que debater teoria.

---

## 11. Como tratar excecoes? DomainException vs HTTP 400/404

### Hierarquia de excecoes do dominio

```
DominioException (abstract) ← base de todas
├── ValidacaoException              → dados invalidos
├── EntidadeNaoEncontradaException  → recurso nao existe
└── RegraDeNegocioException         → regra violada
```

### Quem lanca o que

| Quem lanca      | Excecao                         | Exemplo                                    |
|-----------------|---------------------------------|--------------------------------------------|
| **Entidade**    | `ValidacaoException`            | `new TipoUsuario(null, "desc")` → nome nulo |
| **UseCase**     | `RegraDeNegocioException`       | Nome duplicado ao criar                    |
| **UseCase**     | `EntidadeNaoEncontradaException`| Buscar por ID que nao existe               |

### Quem traduz para HTTP

O `GlobalExceptionHandler` (interfaces/rest/) faz o mapeamento:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidacaoException.class)
    public ProblemDetail tratarValidacaoDominio(ValidacaoException ex) {
        // → HTTP 400 Bad Request (RFC 7807)
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ProblemDetail tratarEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex) {
        // → HTTP 404 Not Found (RFC 7807)
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ProblemDetail tratarRegraDeNegocio(RegraDeNegocioException ex) {
        // → HTTP 422 Unprocessable Entity (RFC 7807)
    }
}
```

### Por que o dominio NAO lanca HTTP 400?

Porque o dominio nao sabe que existe HTTP. Ele lanca `ValidacaoException` — uma excecao de negocio. Se amanha a interface de entrada for uma fila Kafka em vez de REST, a excecao continua funcionando — so muda quem a traduz.

Formato de resposta (RFC 7807 ProblemDetail):
```json
{
  "type": "https://api.jilo-com-jurubeba.com.br/erros/regra-negocio",
  "title": "Regra de Negocio Violada",
  "status": 422,
  "detail": "Ja existe um tipo de usuario com o nome 'MASTER'",
  "timestamp": "2026-02-16T10:30:00Z"
}
```

---

## 12. Seguranca (Spring Security) entra onde?

### Na infrastructure, nunca no dominio

```java
// infrastructure/config/SecurityConfig.java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
        "/swagger-ui/**", "/api-docs/**", "/actuator/**",
        "/h2-console/**", "/v1/health"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .anyRequest().authenticated())
            .build();
    }
}
```

### "Meu UseCase pode acessar SecurityContextHolder?"

**NAO.** `SecurityContextHolder` e uma classe do Spring Security. Se o UseCase importar ela, acopla a application ao framework.

**Solucao correta (quando necessario):** Criar uma interface no dominio:

```java
// Futuro: domain/gateway/UsuarioAutenticadoProvider.java
public interface UsuarioAutenticadoProvider {
    Long obterIdDoUsuarioAutenticado();
}

// Futuro: infrastructure/security/UsuarioAutenticadoProviderSpring.java
@Component
public class UsuarioAutenticadoProviderSpring implements UsuarioAutenticadoProvider {
    @Override
    public Long obterIdDoUsuarioAutenticado() {
        // Aqui SIM usa SecurityContextHolder
        return ((UserDetails) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal()).getId();
    }
}
```

O UseCase recebe a interface (do dominio), nao a implementacao (do Spring).

---

## 13. Eventos de dominio vs eventos de aplicacao

### O que ja temos planejado

Criamos o pacote `domain/event/` com documentacao:

```java
// domain/event/package-info.java (resumo)
// Eventos de dominio:
// - Imutaveis (Java Records)
// - Nomeados no passado: TipoUsuarioCriadoEvent
// - Auto-contidos (carregam todos os dados necessarios)
// - SEM dependencia de framework (nao usam Spring ApplicationEvent)
```

### Quando publicar eventos?

| Tipo de evento     | Onde publicar     | Quem consome            | Exemplo                                |
|--------------------|-------------------|--------------------------|----------------------------------------|
| **Dominio**        | Entidade/UseCase  | Outros UseCases          | `TipoUsuarioCriadoEvent`              |
| **Aplicacao**      | UseCase           | Infrastructure           | `NotificarAdminEvent`                  |
| **Integracao**     | Infrastructure    | Outros microservicos     | Kafka: `usuario.tipo.criado`           |

### Como fariamos (futuro)

```java
// domain/event/TipoUsuarioCriadoEvent.java
public record TipoUsuarioCriadoEvent(Long id, String nome, LocalDateTime ocorridoEm) {}

// application/usecase/tipousuario/CriarTipoUsuarioUseCase.java
// Publicaria o evento via interface DominioEventPublisher (no domain)
// Implementacao do publisher fica na infrastructure (pode usar Spring Events, Kafka, etc)
```

A chave: o **dominio define o evento**. A **infrastructure decide como entregar** (Spring Events, Kafka, RabbitMQ).

---

## 14. Testes: o que testar em cada camada?

### Visao geral dos nossos testes

| Camada        | Tipo        | Ferramentas              | Usa Spring? | Exemplo                            |
|---------------|-------------|--------------------------|-------------|-------------------------------------|
| Domain        | Unitario    | JUnit 5 + AssertJ        | **NAO**     | `TipoUsuarioTest`                  |
| Application   | Unitario    | JUnit 5 + Mockito        | **NAO**     | `CriarTipoUsuarioUseCaseTest`      |
| Interfaces    | Integracao  | MockMvc + @SpringBootTest | SIM        | `TipoUsuarioControllerTest`        |
| Architecture  | Validacao   | ArchUnit                 | NAO         | `CleanArchitectureTest`            |

### Domain — Testa regras de negocio (sem nada)

```java
// TipoUsuarioTest.java — 15 testes, 0 frameworks
@Test
@DisplayName("Deve lancar excecao quando nome for nulo")
void deveLancarExcecaoQuandoNomeNulo() {
    assertThatThrownBy(() -> new TipoUsuario(null, "Descricao"))
        .isInstanceOf(ValidacaoException.class)
        .hasMessageContaining("Nome");
}

@Test
@DisplayName("Deve converter nome para uppercase e trim")
void deveConverterNomeParaUppercase() {
    TipoUsuario tipo = new TipoUsuario("  cliente  ", "Consumidor");
    assertThat(tipo.getNome()).isEqualTo("CLIENTE");
}
```

**Zero dependencias externas.** Roda em milissegundos. Se esse teste falhar, e BUG de regra de negocio.

### Application — Testa orquestracao (com Mockito)

```java
// CriarTipoUsuarioUseCaseTest.java — Mockito, SEM Spring
@ExtendWith(MockitoExtension.class)
class CriarTipoUsuarioUseCaseTest {

    @Mock private TipoUsuarioGateway gateway;

    @Test
    @DisplayName("Deve lancar excecao quando nome ja existe")
    void deveLancarExcecaoQuandoNomeJaExiste() {
        when(gateway.existePorNome("MASTER")).thenReturn(true);

        assertThatThrownBy(() -> useCase.executar(input))
            .isInstanceOf(RegraDeNegocioException.class);

        verify(gateway, never()).salvar(any()); // Nao salvou!
    }
}
```

O Gateway e **mockado**. Nao toca no banco. Testa APENAS a logica de orquestracao.

### Interfaces — Testa fluxo HTTP completo

```java
// TipoUsuarioControllerTest.java — MockMvc + H2
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TipoUsuarioControllerTest {

    @Test
    @WithMockUser  // Simula autenticacao
    void deveCriarNovoTipo() throws Exception {
        mockMvc.perform(post("/v1/tipos-usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"nome": "MODERADOR", "descricao": "Moderador do sistema"}
                """))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.nome", is("MODERADOR")));
    }
}
```

Testa o fluxo completo: HTTP → Controller → UseCase → Gateway → JPA → H2 → Response.

### Contagem atual: 77 testes, todos passando

```
Domain:        15 testes (TipoUsuarioTest)
Application:   11 testes (5 UseCase tests)
Interfaces:     9 testes (TipoUsuarioControllerTest)
Architecture:  35 testes (CleanArchitectureTest)
Outros:         7 testes (Saude, AppContext)
```

---

## 15. Como organizar pacotes sem virar bagunca?

### O problema classico

MVC tradicional agrupa **por tipo tecnico**:
```
com.app/
├── controller/
│   ├── TipoUsuarioController.java
│   ├── UsuarioController.java
│   └── RestauranteController.java
├── service/
│   ├── TipoUsuarioService.java
│   ├── UsuarioService.java
│   └── RestauranteService.java
├── repository/
│   ├── TipoUsuarioRepository.java
│   ...
```

Problema: para entender TipoUsuario, voce precisa pular entre 5 pastas diferentes.

### Nossa abordagem: camada + subpasta por entidade

```
domain/
├── entity/
│   └── tipousuario/TipoUsuario.java
├── gateway/
│   └── tipousuario/TipoUsuarioGateway.java

application/
├── usecase/
│   └── tipousuario/
│       ├── CriarTipoUsuarioUseCase.java
│       ├── BuscarTipoUsuarioPorIdUseCase.java
│       └── ...
├── dto/
│   └── tipousuario/
│       ├── TipoUsuarioOutput.java
│       └── CriarTipoUsuarioInput.java

infrastructure/
├── persistence/
│   └── tipousuario/
│       ├── entity/TipoUsuarioJpaEntity.java
│       ├── repository/TipoUsuarioRepository.java
│       ├── mapper/TipoUsuarioPersistenceMapper.java
│       └── gateway/TipoUsuarioGatewayJpa.java

interfaces/
├── rest/
│   └── tipousuario/TipoUsuarioController.java
│   └── dto/tipousuario/
│   └── mapper/tipousuario/
```

### Por que assim?

1. **Primeiro nivel (camada):** Respeita a Dependency Rule — voce VE a separacao arquitetural
2. **Segundo nivel (entidade):** Agrupa tudo de TipoUsuario junto — facil de encontrar
3. **Escalavel:** Para criar "Usuario", replica a estrutura trocando o nome

### Template para nova feature

Para criar `Restaurante`, basta copiar a estrutura de `tipousuario/` em cada camada:

```
domain/entity/restaurante/Restaurante.java
domain/gateway/restaurante/RestauranteGateway.java
application/usecase/restaurante/CriarRestauranteUseCase.java
application/dto/restaurante/RestauranteOutput.java
infrastructure/persistence/restaurante/entity/RestauranteJpaEntity.java
... etc
```

---

## 16. Ate que ponto vale a pureza? Isso nao e burocracia?

### A pergunta honesta

> "20 arquivos para um CRUD de TipoUsuario? No MVC eu faria em 4 (Controller, Service, Repository, Entity)."

### A resposta honesta

**Para um CRUD simples isolado: sim, e mais codigo.**

Mas vamos olhar o que ganhamos:

### O que a "burocracia" nos da

| Beneficio                       | Como conseguimos                                       |
|---------------------------------|--------------------------------------------------------|
| **Testabilidade**               | Testes unitarios rodam em 0ms, sem Spring               |
| **Independencia de framework**  | Troca Spring por Quarkus? So muda infrastructure        |
| **Independencia de banco**      | Troca JPA por MongoDB? So muda persistence              |
| **Codigo auto-documentado**     | `CriarTipoUsuarioUseCase` diz exatamente o que faz     |
| **Sem GodService**              | Cada operacao e uma classe pequena e focada             |
| **Validacao garantida**         | Entidade protege invariantes mesmo sem HTTP             |
| **Arquitetura validada**        | 35 testes ArchUnit impedem degradacao                   |
| **Onboarding facilitado**       | Novo dev abre a pasta e ja entende a estrutura          |

### Quando NAO usar Clean Architecture

- Prototipo descartavel / POC
- Projeto com 1-2 entidades que nunca vai crescer
- Time de 1 pessoa em projeto pessoal
- CRUD sem regras de negocio (so salvar e consultar)

### Quando USAR

- Projeto que vai crescer (nosso caso: TipoUsuario → Usuario → Restaurante → Cardapio)
- Multiplos devs trabalhando juntos (nosso caso: grupo de pos-graduacao)
- Regras de negocio complexas (futuro: horarios de funcionamento, cardapios, pedidos)
- Requisito de entrega academica que exige arquitetura justificada

### O equilibrio que encontramos

Nao fomos puristas ao extremo. Alguns pragmatismos que aplicamos:

1. **Nao separamos em modulos Maven** — um unico modulo, pacotes bem organizados
2. **Usamos MapStruct** — gera boilerplate em compile-time, nao somos masoquistas
3. **DataSeeder usa JPA direto** — nao passa pelo UseCase (dados de setup, nao e negocio)
4. **Lombok na JpaEntity** — e infrastructure, pode usar qualquer coisa
5. **ddl-auto=update** — para dev; em producao, usariamos Flyway

> **Clean Architecture nao e sobre criar o maximo de arquivos possivel. E sobre colocar cada responsabilidade no lugar certo, para que o software seja facil de testar, facil de mudar, e facil de entender.**

---

## Resumo Visual: O Fluxo Completo de uma Requisicao

```
[Cliente HTTP]
      │
      ▼
┌─────────────────────────────────────────────────────────────┐
│ INTERFACES                                                   │
│                                                              │
│  CriarTipoUsuarioRequest  ──→  TipoUsuarioRestMapper        │
│  (@NotBlank, @Size)              .toInput(request)           │
│                                       │                      │
│  TipoUsuarioController               │                      │
│  @PostMapping                         ▼                      │
│                              CriarTipoUsuarioInput           │
├─────────────────────────────────┬───────────────────────────┤
│ APPLICATION                     │                            │
│                                 ▼                            │
│  CriarTipoUsuarioUseCase                                    │
│  1. gateway.existePorNome()  → regra de negocio              │
│  2. new TipoUsuario()       → validacao no construtor        │
│  3. gateway.salvar()         → persiste                      │
│  4. return toOutput()        → converte                      │
├─────────────────────────────────┬───────────────────────────┤
│ DOMAIN                          │                            │
│                                 │                            │
│  TipoUsuario (POJO)            │ TipoUsuarioGateway (IF)    │
│  - validarCamposObrigatorios()  │ - salvar()                 │
│  - nome.trim().toUpperCase()    │ - existePorNome()          │
├─────────────────────────────────┼───────────────────────────┤
│ INFRASTRUCTURE                  │                            │
│                                 ▼                            │
│  TipoUsuarioGatewayJpa implements TipoUsuarioGateway         │
│  1. mapper.toJpaEntity()    → Domain → JPA                   │
│  2. repository.save()       → Spring Data                    │
│  3. mapper.toDomain()       → JPA → Domain                   │
│                                                              │
│  TipoUsuarioJpaEntity ←→ banco H2/PostgreSQL                │
└─────────────────────────────────────────────────────────────┘
      │
      ▼
[Resposta HTTP 201 Created + Location header]
```

---

## Arquivos de Referencia

Para estudar o componente TipoUsuario completo:

| Camada          | Arquivo                                  | Qtd |
|-----------------|------------------------------------------|-----|
| Domain          | `TipoUsuario.java`, `TipoUsuarioGateway.java` | 2 |
| Application     | 3 DTOs + 5 UseCases                     | 8   |
| Interfaces      | Controller + 3 DTOs + RestMapper         | 5   |
| Infrastructure  | JpaEntity + Repository + Mapper + Gateway + Seeder | 5 |
| Testes          | 7 classes de teste (77 testes total)     | 7   |
| **Total**       |                                          | **27** |
