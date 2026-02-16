# ADR-0002: Gateway (Port de Saida) no Dominio, nao na Application

**Data:** 2026-02-16

**Status:** Aceito

**Decisor(es):** Grupo 3 - Postech

**Autor:** Danilo Fernando

---

## Contexto

Ao implementar o componente TipoUsuario, precisamos definir onde ficam as interfaces de persistencia (ports de saida). Na literatura existem duas abordagens predominantes:

1. **Clean Architecture (Uncle Bob):** Interfaces de persistencia ficam em `domain/gateway/`
2. **Hexagonal Architecture (Alistair Cockburn):** Ports de saida ficam em `application/ports/out/`

Essa decisao impacta diretamente a estrutura de pacotes, as dependencias entre camadas e a forma como futuros desenvolvedores entendem o projeto.

### O problema concreto

O `CriarTipoUsuarioUseCase` (application) precisa salvar um `TipoUsuario` no banco. Para isso, ele depende de uma interface que define o contrato de persistencia. A implementacao concreta (`TipoUsuarioGatewayJpa`) fica na infrastructure. Mas onde fica a **interface**?

```
Opcao A: domain/gateway/tipousuario/TipoUsuarioGateway.java
Opcao B: application/ports/out/TipoUsuarioGateway.java
```

---

## Decisao

Adotamos a **Opcao A — Gateway no dominio** (`domain/gateway/`), seguindo o modelo original de Robert C. Martin (Clean Architecture).

### Estrutura resultante

```
domain/
├── entity/
│   └── tipousuario/TipoUsuario.java           ← entidade de negocio
├── gateway/
│   └── tipousuario/TipoUsuarioGateway.java     ← interface (port de saida)
├── exception/                                   ← excecoes de dominio

application/
├── usecase/
│   └── tipousuario/CriarTipoUsuarioUseCase.java  ← consome o gateway
├── dto/                                            ← DTOs Input/Output

infrastructure/
├── persistence/
│   └── tipousuario/
│       └── gateway/TipoUsuarioGatewayJpa.java  ← implementacao concreta
```

### Codigo da interface

```java
// domain/gateway/tipousuario/TipoUsuarioGateway.java
package com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario;

public interface TipoUsuarioGateway {
    TipoUsuario salvar(TipoUsuario tipoUsuario);
    Optional<TipoUsuario> buscarPorId(Long id);
    Optional<TipoUsuario> buscarPorNome(String nome);
    List<TipoUsuario> listarTodos();
    void deletar(Long id);
    boolean existePorNome(String nome);
    boolean existePorNomeEIdDiferente(String nome, Long id);
}
```

### Codigo do UseCase consumindo

```java
// application/usecase/tipousuario/CriarTipoUsuarioUseCase.java
public class CriarTipoUsuarioUseCase implements UseCase<CriarTipoUsuarioInput, TipoUsuarioOutput> {

    private final TipoUsuarioGateway tipoUsuarioGateway; // ← Interface do DOMAIN

    @Override
    public TipoUsuarioOutput executar(CriarTipoUsuarioInput input) {
        if (tipoUsuarioGateway.existePorNome(input.nome().trim().toUpperCase())) {
            throw new RegraDeNegocioException("Ja existe...");
        }
        TipoUsuario tipoUsuario = new TipoUsuario(input.nome(), input.descricao());
        TipoUsuario salvo = tipoUsuarioGateway.salvar(tipoUsuario);
        return toOutput(salvo);
    }
}
```

---

## Alternativas Consideradas

### Opcao B: Gateway na Application (Hexagonal / Ports & Adapters)

```
application/
├── ports/
│   ├── in/    ← ports de entrada (interfaces dos UseCases)
│   └── out/   ← ports de saida (interfaces de persistencia)
│       └── TipoUsuarioGateway.java
```

**Argumentos a favor:**

- Segue o modelo Hexagonal de Cockburn mais literalmente
- Explicita a separacao entre ports de entrada e de saida
- A camada de dominio fica ainda mais isolada (nao define contratos externos)
- Alguns autores argumentam que o contrato de persistencia e uma necessidade da **application** (quem orquestra), nao do **domain** (quem define regras)

**Argumentos contra (por que rejeitamos):**

- Cria uma dependencia **application → application** quando a camada de application precisa dos ports. Embora interna a mesma camada, obscurece o fluxo
- No modelo original de Uncle Bob, Entities e Use Case Interfaces ficam nos circulos internos — e o Gateway e uma Use Case Interface
- Nosso projeto ja segue Clean Architecture de ponta a ponta (ADR-0001). Misturar nomenclatura Hexagonal (ports/in/out) com Clean Architecture geraria confusao conceitual
- A entidade `TipoUsuario` e quem precisa ser persistida — faz sentido que o dominio defina o contrato para isso

### Opcao C: Gateway na Application com nomenclatura Clean Arch

```
application/
├── gateway/
│   └── tipousuario/TipoUsuarioGateway.java
```

**Argumentos a favor:**

- Mantem o nome "gateway" (Clean Arch) mas na camada application
- A application orquestra, entao ela define o que precisa

**Argumentos contra (por que rejeitamos):**

- Na Dependency Rule, **application depende de domain**. Se o gateway estiver na application, o dominio nao poderia referencia-lo (nao que precise hoje, mas limitaria o futuro)
- Se futuramente uma entidade de dominio precisar referenciar um gateway (ex: em um Domain Service), teriamos uma dependencia domain → application, que **viola a Dependency Rule**
- Uncle Bob posiciona o Gateway como "Enterprise Business Rules Interface", que fica no mesmo nivel das Entities

---

## Justificativa Detalhada

### 1. Consistencia com ADR-0001

Escolhemos Clean Architecture (ADR-0001). O modelo original de Uncle Bob coloca as interfaces de dados (que ele chama de "Data Access Interfaces") no circulo de Entities/Use Cases — ou seja, no dominio. Manter essa consistencia evita que o time precise estudar dois modelos arquiteturais.

### 2. Dependency Rule preservada

```
application  ───depends on───→  domain
                                   ↑
infrastructure ───implements───────┘
```

Com o gateway no dominio:
- `application` depende de `domain` (permitido) ✅
- `infrastructure` implementa `domain` (permitido) ✅
- `domain` nao depende de nada ✅

### 3. Protecao futura com Domain Services

Se no futuro tivermos um Domain Service (ex: `ValidadorDeUnicidadeService` dentro do dominio), ele poderia precisar consultar o gateway. Com o gateway no dominio, isso e possivel:

```java
// Hipotetico futuro
// domain/service/ValidadorUnicidadeTipoUsuario.java
public class ValidadorUnicidadeTipoUsuario {
    private final TipoUsuarioGateway gateway; // ← Funciona porque ambos estao no domain
    // ...
}
```

Se o gateway estivesse na application, este cenario violaria a Dependency Rule (domain → application).

### 4. Validacao automatizada via ArchUnit

Nossos 35 testes ArchUnit validam que:
- domain nao depende de application, interfaces ou infrastructure
- Gateways no dominio sao interfaces (nunca classes concretas)
- Implementacoes de Gateway ficam em `infrastructure.persistence..gateway..`

```java
// CleanArchitectureTest.java
@Test
@DisplayName("Gateways no dominio devem ser interfaces")
void gatewaysDevemSerInterfaces() {
    classes()
        .that().resideInAPackage("..domain.gateway..")
        .should().beInterfaces()
        .check(classes);
}
```

---

## Consequencias

### Positivas

1. **Consistencia:** Toda a equipe segue um unico modelo (Clean Architecture de Uncle Bob)
2. **Simplicidade:** Nao precisa aprender dois vocabularios (ports/adapters + gateways)
3. **Flexibilidade futura:** Domain Services podem usar gateways sem violar dependencias
4. **Validacao automatica:** ArchUnit garante que gateways no dominio sao sempre interfaces
5. **Padrao replicavel:** Para cada nova entidade, basta criar `domain/gateway/{entidade}/`

### Negativas

1. **Pacote domain maior:** O domain acumula entidades + gateways + excecoes + events
2. **Debate academico:** Alguns membros do grupo poderiam preferir Hexagonal

### Mitigacoes

| Consequencia          | Mitigacao                                               |
|-----------------------|---------------------------------------------------------|
| Domain grande         | Subpastas por entidade mantem organizacao                |
| Debate Hexagonal      | ADR documentado + guia didatico em `docs/guides/`       |

---

## Referencias

- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) — Diagrama original mostra "Entities" e "Use Case Output Port" no mesmo circulo
- [Hexagonal Architecture - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/) — Modelo original de Ports & Adapters
- [ADR-0001: Adocao de Clean Architecture](./ADR-0001-clean-architecture.md) — Decisao base do projeto
- [Domain-Driven Design - Eric Evans, Cap. 5](https://www.domainlanguage.com/ddd/) — Repositories como parte do dominio
- `docs/guides/clean-architecture-guide.md` — Guia didatico com exemplos do projeto
