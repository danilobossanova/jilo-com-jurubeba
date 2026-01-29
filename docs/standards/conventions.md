# Convenções e Padrões do Projeto jilo-com-jurubeba

> @author Danilo Fernando

Este documento define as convenções de código, nomenclatura e organização do projeto.

---

## Convenções de Pacotes

### Estrutura Base

```
com.grupo3.postech.jilocomjurubeba
├── domain/                    # Camada de Domínio
│   ├── entity/               # Entidades de negócio
│   ├── valueobject/          # Value Objects
│   ├── gateway/              # Interfaces de Gateway
│   └── exception/            # Exceções de domínio
│
├── application/              # Camada de Aplicação
│   ├── usecase/              # Casos de uso
│   │   ├── usuario/
│   │   ├── restaurante/
│   │   └── cardapio/
│   └── dto/                  # DTOs de entrada/saída
│
├── interfaces/               # Camada de Interfaces
│   └── rest/                 # API REST
│       ├── dto/
│       ├── mapper/
│       └── handler/
│
└── infrastructure/           # Camada de Infraestrutura
    ├── persistence/
    │   ├── entity/
    │   ├── repository/
    │   ├── gateway/
    │   └── mapper/
    └── config/
```

---

## Nomenclatura de Classes

| Componente              | Padrão                           | Exemplo                        |
|-------------------------|----------------------------------|--------------------------------|
| Entidade de Domínio     | `[Nome]`                         | `Usuario`, `Restaurante`       |
| Value Object            | `[Nome]`                         | `Email`, `Cpf`, `Preco`        |
| Gateway (interface)     | `[Entidade]Gateway`              | `UsuarioGateway`               |
| Gateway (implementação) | `[Entidade]Gateway[Tecnologia]`  | `UsuarioGatewayJpa`            |
| UseCase                 | `[Verbo][Entidade]UseCase`       | `CriarUsuarioUseCase`          |
| DTO Input (app)         | `[Verbo][Entidade]Input`         | `CriarUsuarioInput`            |
| DTO Output (app)        | `[Entidade]Output`               | `UsuarioOutput`                |
| Controller              | `[Entidade]Controller`           | `UsuarioController`            |
| Request DTO (rest)      | `[Verbo][Entidade]Request`       | `CriarUsuarioRequest`          |
| Response DTO (rest)     | `[Entidade]Response`             | `UsuarioResponse`              |
| JPA Entity              | `[Entidade]JpaEntity`            | `UsuarioJpaEntity`             |
| Repository              | `[Entidade]Repository`           | `UsuarioRepository`            |
| Mapper REST             | `[Entidade]RestMapper`           | `UsuarioRestMapper`            |
| Mapper Persistence      | `[Entidade]PersistenceMapper`    | `UsuarioPersistenceMapper`     |
| Exceção de Domínio      | `[Descrição]Exception`           | `ValidacaoException`           |

---

## Padrão de DTOs

### Application Layer DTOs

Use **Java Records** para imutabilidade:

```java
// Input - dados de entrada para o UseCase
public record CriarUsuarioInput(
    String nome,
    String email,
    String cpf,
    TipoUsuario tipo
) {}

// Output - dados de saída do UseCase
public record UsuarioOutput(
    Long id,
    String nome,
    String email,
    TipoUsuario tipo,
    LocalDateTime criadoEm
) {}
```

### REST Layer DTOs

Use **Java Records** com anotações de validação:

```java
@Schema(description = "Dados para criação de usuário")
public record CriarUsuarioRequest(
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100)
    String nome,

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    String email
) {}
```

---

## Padrão de Exceptions

### Hierarquia

```java
public abstract class DominioException extends RuntimeException { }

public class ValidacaoException extends DominioException { }        // HTTP 400
public class EntidadeNaoEncontradaException extends DominioException { } // HTTP 404
public class RegraDeNegocioException extends DominioException { }   // HTTP 422
```

---

## Padrão de Logs

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CriarUsuarioUseCase {
    private static final Logger log = LoggerFactory.getLogger(CriarUsuarioUseCase.class);

    public UsuarioOutput executar(CriarUsuarioInput input) {
        log.debug("Criando usuário: email={}", input.email());
        // ...
        log.info("Usuário criado: id={}", usuario.getId());
    }
}
```

### Níveis de Log

| Nível   | Quando usar                                              |
|---------|----------------------------------------------------------|
| `ERROR` | Erros que impedem a operação de continuar                |
| `WARN`  | Situações anormais mas recuperáveis                      |
| `INFO`  | Eventos de negócio importantes                           |
| `DEBUG` | Informações detalhadas para debug                        |

---

## Idioma

### Código em Português

O projeto adota **português brasileiro** para:

- Nomes de classes de domínio (`Usuario`, `Restaurante`)
- Nomes de métodos (`executar`, `buscarPorId`)
- Variáveis e parâmetros (`nome`, `email`, `tipo`)
- Mensagens de erro e log

### Exceções em Inglês

Mantemos em inglês apenas:

- Annotations do Spring (`@GetMapping`, `@Valid`)
- Métodos de frameworks (`findById`, `save`)
- Termos técnicos consagrados (`Input`, `Output`, `Gateway`, `UseCase`)

---

## Checklist de Revisão de Código

- [ ] Nomenclatura segue os padrões deste documento
- [ ] Testes unitários escritos (meta: 80% cobertura)
- [ ] Logs adicionados em pontos relevantes
- [ ] ArchUnit passa sem erros
- [ ] Sem warnings de compilação
