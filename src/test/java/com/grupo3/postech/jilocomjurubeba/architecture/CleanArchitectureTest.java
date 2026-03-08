package com.grupo3.postech.jilocomjurubeba.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Testes de arquitetura usando ArchUnit.
 *
 * <p>Garante que as regras de dependência da Clean Architecture são respeitadas com rigor purista.
 *
 * <p>Regras principais:
 *
 * <ul>
 *   <li>domain: não depende de nenhuma outra camada nem de frameworks
 *   <li>application: depende apenas de domain, sem frameworks
 *   <li>interfaces: depende de application e domain
 *   <li>infrastructure: depende de application e domain
 * </ul>
 *
 * <p>Execução: {@code mvn test -Dtest=CleanArchitectureTest}
 *
 * @author Danilo Fernando
 */
class CleanArchitectureTest {

  private static final String BASE_PACKAGE = "com.grupo3.postech.jilocomjurubeba";

  private static JavaClasses classes;

  @BeforeAll
  static void setUp() {
    classes =
        new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(BASE_PACKAGE);
  }

  // ========================================================================
  // GRUPO 1: Regras de Dependência entre Camadas (Dependency Rule)
  // ========================================================================

  @Nested
  @DisplayName("Regras de Dependência entre Camadas")
  class RegrasDependenciaCamadas {

    @Test
    @DisplayName("Arquitetura em camadas deve ser respeitada")
    void devemRespeitarArquiteturaEmCamadas() {

      ArchRule regra =
          layeredArchitecture()
              .consideringOnlyDependenciesInLayers()

              // Definição das camadas
              .layer("Domain")
              .definedBy(BASE_PACKAGE + ".domain..")
              .layer("Application")
              .definedBy(BASE_PACKAGE + ".application..")
              .layer("Infrastructure")
              .definedBy(BASE_PACKAGE + ".infrastructure..")

              // Regras
              .whereLayer("Domain")
              .mayOnlyBeAccessedByLayers("Application", "Infrastructure")
              .whereLayer("Application")
              .mayOnlyBeAccessedByLayers("Infrastructure")
              .whereLayer("Infrastructure")
              .mayNotBeAccessedByAnyLayer();

      regra.check(classes);
    }

    @Test
    @DisplayName("Application não pode depender de Infrastructure")
    void applicationNaoDeveDependerDeInfrastructure() {
      ArchRule regra =
          noClasses()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".application..")
              .should()
              .dependOnClassesThat()
              .resideInAPackage(BASE_PACKAGE + ".infrastructure..");

      regra.check(classes);
    }

    @Test
    @DisplayName("Application não pode depender de Interfaces")
    void applicationNaoDeveDependerDeInterfaces() {
      ArchRule regra =
          noClasses()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".application..")
              .should()
              .dependOnClassesThat()
              .resideInAPackage(BASE_PACKAGE + ".interfaces..");

      regra.check(classes);
    }

    @Test
    @DisplayName("Domain não pode depender de nenhuma outra camada")
    void domainNaoDeveDependerDeOutrasCamadas() {
      ArchRule regra =
          noClasses()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".domain..")
              .should()
              .dependOnClassesThat()
              .resideInAnyPackage(
                  BASE_PACKAGE + ".application..",
                  BASE_PACKAGE + ".interfaces..",
                  BASE_PACKAGE + ".infrastructure..");

      regra.check(classes);
    }

    @Test
    @DisplayName("Infrastructure não pode depender de Interfaces")
    void infrastructureNaoDeveDependerDeInterfaces() {
      ArchRule regra =
          noClasses()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".infrastructure..")
              .should()
              .dependOnClassesThat()
              .resideInAPackage(BASE_PACKAGE + ".interfaces..");

      regra.check(classes);
    }
  }

  // ========================================================================
  // GRUPO 2: Independência de Frameworks nas Camadas Internas
  // ========================================================================

  @Nested
  @DisplayName("Regras de Independência de Frameworks")
  class RegrasIndependenciaFrameworks {

    // --- DOMAIN: Zero dependência de qualquer framework ---

    @Test
    @DisplayName("Domain não deve usar Spring Framework")
    void domainNaoDeveUsarSpring() {
      ArchRule regra =
          noClasses()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".domain..")
              .should()
              .dependOnClassesThat()
              .resideInAnyPackage("org.springframework..", "org.springframework.boot..");

      regra.check(classes);
    }

    @Test
    @DisplayName("Domain não deve usar JPA/Hibernate")
    void domainNaoDeveUsarJpa() {
      ArchRule regra =
          noClasses()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".domain..")
              .should()
              .dependOnClassesThat()
              .resideInAnyPackage(
                  "jakarta.persistence..", "javax.persistence..", "org.hibernate..");

      regra.check(classes);
    }

    @Test
    @DisplayName("Domain não deve usar Lombok")
    void domainNaoDeveUsarLombok() {

      ArchRule domainNaoDeveUsarLombok =
          noClasses()
              .that()
              .resideInAPackage("com.grupo3.postech.jilocomjurubeba.domain.entity..")
              .should()
              .dependOnClassesThat()
              .resideInAPackage("lombok..");

      domainNaoDeveUsarLombok.check(classes);
    }

    @Test
    @DisplayName("Domain não deve usar Jackson (serialização)")
    void domainNaoDeveUsarJackson() {
      ArchRule regra =
          noClasses()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".domain..")
              .should()
              .dependOnClassesThat()
              .resideInAPackage("com.fasterxml.jackson..");

      regra.check(classes);
    }

    @Test
    @DisplayName("Domain não deve usar Bean Validation (jakarta.validation)")
    void domainNaoDeveUsarBeanValidation() {
      ArchRule regra =
          noClasses()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".domain..")
              .should()
              .dependOnClassesThat()
              .resideInAnyPackage("jakarta.validation..", "javax.validation..");

      regra.check(classes);
    }

    // --- APPLICATION: Sem Spring, JPA, Lombok, Jackson ---

    @Test
    @DisplayName("Application não deve usar Spring Framework (nenhum pacote)")
    void applicationNaoDeveUsarSpring() {
      ArchRule regra =
          noClasses()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".application..")
              .should()
              .dependOnClassesThat()
              .resideInAnyPackage("org.springframework..", "org.springframework.boot..");

      regra.check(classes);
    }

    @Test
    @DisplayName("Application não deve usar JPA/Hibernate")
    void applicationNaoDeveUsarJpa() {
      ArchRule regra =
          noClasses()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".application..")
              .should()
              .dependOnClassesThat()
              .resideInAnyPackage(
                  "jakarta.persistence..", "javax.persistence..", "org.hibernate..");

      regra.check(classes);
    }

    @Test
    @DisplayName("Application não deve usar Lombok")
    void applicationNaoDeveUsarLombok() {
      ArchRule regra =
          noClasses()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".application..")
              .should()
              .dependOnClassesThat()
              .resideInAPackage("lombok..");

      regra.check(classes);
    }

    @Test
    @DisplayName("Application não deve usar Jackson (serialização)")
    void applicationNaoDeveUsarJackson() {
      ArchRule regra =
          noClasses()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".application..")
              .should()
              .dependOnClassesThat()
              .resideInAPackage("com.fasterxml.jackson..");

      regra.check(classes);
    }

    @Test
    @DisplayName("Application não deve usar Bean Validation (jakarta.validation)")
    void applicationNaoDeveUsarBeanValidation() {
      ArchRule regra =
          noClasses()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".application..")
              .should()
              .dependOnClassesThat()
              .resideInAnyPackage("jakarta.validation..", "javax.validation..");

      regra.check(classes);
    }
  }

  // ========================================================================
  // GRUPO 3: Localização Correta de Anotações de Framework
  // ========================================================================

  @Nested
  @DisplayName("Regras de Localização de Anotações")
  class RegrasLocalizacaoAnotacoes {

    @Test
    @DisplayName("@Entity JPA deve residir apenas em infrastructure.persistence")
    void entidadesJpaDevemResidirEmInfrastructurePersistence() {
      ArchRule regra =
          classes()
              .that()
              .areAnnotatedWith(jakarta.persistence.Entity.class)
              .should()
              .resideInAPackage(BASE_PACKAGE + ".infrastructure.persistence..")
              .allowEmptyShould(true);

      regra.check(classes);
    }

    @Test
    @DisplayName("@Configuration deve residir apenas em infrastructure")
    void configuracoesDevemResidirEmInfrastructure() {
      ArchRule regra =
          classes()
              .that()
              .areAnnotatedWith(org.springframework.context.annotation.Configuration.class)
              .should()
              .resideInAPackage(BASE_PACKAGE + ".infrastructure..");

      regra.check(classes);
    }

    @Test
    @DisplayName("@RestController deve seguir padrão de arquitetura")
    void restControllersDevemSeguirPadrao() {

      ArchRule regra =
          classes()
              .that()
              .areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
              .should()
              .resideInAPackage(BASE_PACKAGE + ".infrastructure.web.controller..")
              .andShould()
              .haveSimpleNameEndingWith("Controller");

      regra.check(classes);
    }

    @Test
    @DisplayName("Controllers não devem depender de infrastructure")
    void controllersNaoDevemDependerDeInfrastructure() {

      ArchRule regra =
          noClasses()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".infrastructure.web.controller..")
              .should()
              .dependOnClassesThat()
              .resideInAPackage(BASE_PACKAGE + ".infrastructure.persistence..");

      regra.check(classes);
    }

    @Test
    @DisplayName("@RestControllerAdvice deve seguir padrão de arquitetura")
    void restControllerAdviceDevemSeguirPadrao() {

      ArchRule regra =
          classes()
              .that()
              .areAnnotatedWith(org.springframework.web.bind.annotation.RestControllerAdvice.class)
              .should()
              .resideInAPackage(BASE_PACKAGE + ".infrastructure.web.exception..")
              .andShould()
              .haveSimpleNameEndingWith("ExceptionHandler");

      regra.check(classes);
    }

    @Test
    @DisplayName("@Repository Spring deve residir apenas em infrastructure.persistence")
    void repositoriesDevemResidirEmInfrastructurePersistence() {
      ArchRule regra =
          classes()
              .that()
              .areAnnotatedWith(org.springframework.stereotype.Repository.class)
              .should()
              .resideInAPackage(BASE_PACKAGE + ".infrastructure.persistence..")
              .allowEmptyShould(true);

      regra.check(classes);
    }
  }

  // ========================================================================
  // GRUPO 4: Regras Estruturais do Domain
  // ========================================================================

  @Nested
  @DisplayName("Regras Estruturais do Domain")
  class RegrasEstruturaisDomain {

    @Test
    @DisplayName("Gateways do domain devem ser interfaces")
    void gatewaysDevemSerInterfaces() {
      ArchRule regra =
          classes()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".domain.gateway..")
              .and()
              .haveSimpleNameNotContaining("package-info")
              .should()
              .beInterfaces()
              .allowEmptyShould(true);

      regra.check(classes);
    }

    @Test
    @DisplayName("Exceções de domínio devem estender DominioException")
    void excecoesDevemEstenderDominioException() {
      ArchRule regra =
          classes()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".domain.exception..")
              .and()
              .haveSimpleNameNotContaining("package-info")
              .and()
              .doNotHaveSimpleName("DominioException")
              .should()
              .beAssignableTo(
                  com.grupo3.postech.jilocomjurubeba.domain.exception.DominioException.class);

      regra.check(classes);
    }
  }

  // ========================================================================
  // GRUPO 5: Convenções de Nomenclatura
  // ========================================================================

  @Nested
  @DisplayName("Convenções de Nomenclatura")
  class ConvencoesNomenclatura {

    @Test
    @DisplayName("Use Cases devem terminar com 'UseCase'")
    void useCasesDevemTerminarComUseCase() {

      ArchRule regra =
          classes()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".application.usecase..")
              .and()
              .areTopLevelClasses() // ignora classes internas
              .and()
              .areNotInterfaces()
              .and()
              .haveSimpleNameNotContaining("package-info")
              .should()
              .haveSimpleNameEndingWith("UseCase");

      regra.check(classes);
    }

    @Test
    @DisplayName("Controllers devem terminar com 'Controller'")
    void controllersDevemTerminarComController() {

      ArchRule regra =
          classes()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".infrastructure.web.controller..")
              .and()
              .areTopLevelClasses()
              .and()
              .arePublic()
              .and()
              .areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
              .should()
              .haveSimpleNameEndingWith("Controller");

      regra.check(classes);
    }

    @Test
    @DisplayName("Exceções de domínio devem terminar com 'Exception'")
    void excecoesDevemTerminarComException() {
      ArchRule regra =
          classes()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".domain.exception..")
              .and()
              .areAssignableTo(Exception.class)
              .should()
              .haveSimpleNameEndingWith("Exception");

      regra.check(classes);
    }

    @Test
    @DisplayName("Mappers REST devem terminar com 'RestMapper'")
    void mappersRestDevemTerminarComRestMapper() {

      ArchRule regra =
          classes()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".infrastructure.web.mapper..")
              .and()
              .areTopLevelClasses()
              .and()
              .areInterfaces()
              .and()
              .haveSimpleNameNotContaining("package-info")
              .should()
              .haveSimpleNameEndingWith("RestMapper")
              .allowEmptyShould(true);

      regra.check(classes);
    }

    @Test
    @DisplayName("Gateways do domain devem terminar com 'Gateway'")
    void gatewaysDevemTerminarComGateway() {

      ArchRule regra =
          classes()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".domain.gateway..")
              .and()
              .areTopLevelClasses()
              .and()
              .areInterfaces()
              .and()
              .haveSimpleNameNotContaining("package-info")
              .should()
              .haveSimpleNameEndingWith("Gateway");

      regra.check(classes);
    }

    @Test
    @DisplayName("Entidades JPA devem terminar com 'JpaEntity'")
    void entidadesJpaDevemTerminarComJpaEntity() {
      ArchRule regra =
          classes()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".infrastructure.persistence..")
              .and()
              .resideInAPackage("..entity..")
              .and()
              .haveSimpleNameNotContaining("package-info")
              .should()
              .haveSimpleNameEndingWith("JpaEntity")
              .allowEmptyShould(true);

      regra.check(classes);
    }

    @Test
    @DisplayName("Mappers de persistência devem terminar com 'PersistenceMapper'")
    void mappersPersistenciaDevemTerminarComPersistenceMapper() {
      ArchRule regra =
          classes()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".infrastructure.persistence..")
              .and()
              .resideInAPackage("..mapper..")
              .and()
              .areInterfaces()
              .and()
              .haveSimpleNameNotContaining("package-info")
              .should()
              .haveSimpleNameEndingWith("PersistenceMapper")
              .allowEmptyShould(true);

      regra.check(classes);
    }

    @Test
    @DisplayName("Implementações de Gateway devem conter 'Gateway' no nome")
    void implementacoesGatewayDevemConterGateway() {
      ArchRule regra =
          classes()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".infrastructure.persistence..")
              .and()
              .resideInAPackage("..gateway..")
              .and()
              .haveSimpleNameNotContaining("package-info")
              .should()
              .haveSimpleNameContaining("Gateway")
              .allowEmptyShould(true);

      regra.check(classes);
    }

    @Test
    @DisplayName("Repositories Spring Data devem terminar com 'Repository'")
    void repositoriesDevemTerminarComRepository() {
      ArchRule regra =
          classes()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".infrastructure.persistence.repository.")
              .and()
              .resideInAPackage("..repository..")
              .and()
              .haveSimpleNameNotContaining("package-info")
              .should()
              .haveSimpleNameEndingWith("Repository")
              .allowEmptyShould(true);

      regra.check(classes);
    }

    @Test
    @DisplayName("Outputs da application devem terminar com 'Output'")
    void outputsDevemTerminarComOutput() {
      ArchRule regra =
          classes()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".application.dto..")
              .and()
              .haveSimpleNameNotContaining("package-info")
              .and()
              .haveSimpleNameNotContaining("Input")
              .should()
              .haveSimpleNameEndingWith("Output");

      regra.check(classes);
    }

    @Test
    @DisplayName("Inputs da application devem terminar com 'Input'")
    void inputsDevemTerminarComInput() {
      ArchRule regra =
          classes()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".application.dto..")
              .and()
              .haveSimpleNameNotContaining("package-info")
              .and()
              .haveSimpleNameNotContaining("Output")
              .should()
              .haveSimpleNameEndingWith("Input")
              .allowEmptyShould(true);

      regra.check(classes);
    }

    @Test
    @DisplayName("Requests REST devem terminar com 'Request'")
    void requestsDevemTerminarComRequest() {
      ArchRule regra =
          classes()
              .that()
              .resideInAPackage(BASE_PACKAGE + ".interfaces.rest.dto..")
              .and()
              .haveSimpleNameNotContaining("package-info")
              .and()
              .haveSimpleNameNotContaining("Response")
              .should()
              .haveSimpleNameEndingWith("Request")
              .allowEmptyShould(true);

      regra.check(classes);
    }
  }
}
