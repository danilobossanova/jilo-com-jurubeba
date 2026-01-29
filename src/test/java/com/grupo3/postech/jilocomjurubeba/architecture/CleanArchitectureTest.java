package com.grupo3.postech.jilocomjurubeba.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Testes de arquitetura usando ArchUnit.
 *
 * Garante que as regras de dependência da Clean Architecture são respeitadas.
 *
 * Regras principais:
 * - domain: não depende de nenhuma outra camada
 * - application: depende apenas de domain
 * - interfaces: depende de application e domain
 * - infrastructure: depende de application e domain
 *
 * Execução:
 * - mvn test -Dtest=CleanArchitectureTest
 *
 * @author Danilo Fernando
 */
class CleanArchitectureTest {

    private static final String BASE_PACKAGE = "com.grupo3.postech.jilocomjurubeba";

    private static JavaClasses classes;

    @BeforeAll
    static void setUp() {
        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(BASE_PACKAGE);
    }

    @Nested
    @DisplayName("Regras de Dependência entre Camadas")
    class RegrasDependenciaCamadas {

        @Test
        @DisplayName("Arquitetura em camadas deve ser respeitada")
        void devemRespeitarArquiteturaEmCamadas() {
            ArchRule regra = layeredArchitecture()
                    .consideringOnlyDependenciesInLayers()
                    // Definição das camadas
                    .layer("Domain").definedBy(BASE_PACKAGE + ".domain..")
                    .layer("Application").definedBy(BASE_PACKAGE + ".application..")
                    .layer("Interfaces").definedBy(BASE_PACKAGE + ".interfaces..")
                    .layer("Infrastructure").definedBy(BASE_PACKAGE + ".infrastructure..")
                    // Regras de acesso
                    .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Interfaces", "Infrastructure")
                    .whereLayer("Application").mayOnlyBeAccessedByLayers("Interfaces", "Infrastructure")
                    .whereLayer("Interfaces").mayNotBeAccessedByAnyLayer()
                    .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer();

            regra.check(classes);
        }

        @Test
        @DisplayName("Application não pode depender de Infrastructure")
        void applicationNaoDeveDependerDeInfrastructure() {
            ArchRule regra = noClasses()
                    .that().resideInAPackage(BASE_PACKAGE + ".application..")
                    .should().dependOnClassesThat().resideInAPackage(BASE_PACKAGE + ".infrastructure..");

            regra.check(classes);
        }

        @Test
        @DisplayName("Application não pode depender de Interfaces")
        void applicationNaoDeveDependerDeInterfaces() {
            ArchRule regra = noClasses()
                    .that().resideInAPackage(BASE_PACKAGE + ".application..")
                    .should().dependOnClassesThat().resideInAPackage(BASE_PACKAGE + ".interfaces..");

            regra.check(classes);
        }

        @Test
        @DisplayName("Domain não pode depender de nenhuma outra camada")
        void domainNaoDeveDependerDeOutrasCamadas() {
            ArchRule regra = noClasses()
                    .that().resideInAPackage(BASE_PACKAGE + ".domain..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            BASE_PACKAGE + ".application..",
                            BASE_PACKAGE + ".interfaces..",
                            BASE_PACKAGE + ".infrastructure.."
                    );

            regra.check(classes);
        }

        @Test
        @DisplayName("Infrastructure não pode depender de Interfaces")
        void infrastructureNaoDeveDependerDeInterfaces() {
            ArchRule regra = noClasses()
                    .that().resideInAPackage(BASE_PACKAGE + ".infrastructure..")
                    .should().dependOnClassesThat().resideInAPackage(BASE_PACKAGE + ".interfaces..");

            regra.check(classes);
        }
    }

    @Nested
    @DisplayName("Regras de Independência de Frameworks")
    class RegrasIndependenciaFrameworks {

        @Test
        @DisplayName("Domain não deve usar Spring Framework")
        void domainNaoDeveUsarSpring() {
            ArchRule regra = noClasses()
                    .that().resideInAPackage(BASE_PACKAGE + ".domain..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "org.springframework..",
                            "org.springframework.boot.."
                    );

            regra.check(classes);
        }

        @Test
        @DisplayName("Domain não deve usar JPA/Hibernate")
        void domainNaoDeveUsarJpa() {
            ArchRule regra = noClasses()
                    .that().resideInAPackage(BASE_PACKAGE + ".domain..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "jakarta.persistence..",
                            "javax.persistence..",
                            "org.hibernate.."
                    );

            regra.check(classes);
        }

        @Test
        @DisplayName("Application não deve usar Spring Web")
        void applicationNaoDeveUsarSpringWeb() {
            ArchRule regra = noClasses()
                    .that().resideInAPackage(BASE_PACKAGE + ".application..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "org.springframework.web..",
                            "org.springframework.http.."
                    );

            regra.check(classes);
        }

        @Test
        @DisplayName("Application não deve usar JPA/Hibernate")
        void applicationNaoDeveUsarJpa() {
            ArchRule regra = noClasses()
                    .that().resideInAPackage(BASE_PACKAGE + ".application..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "jakarta.persistence..",
                            "javax.persistence..",
                            "org.hibernate.."
                    );

            regra.check(classes);
        }
    }

    @Nested
    @DisplayName("Convenções de Nomenclatura")
    class ConvencoesNomenclatura {

        @Test
        @DisplayName("Use Cases devem terminar com 'UseCase'")
        void useCasesDevemTerminarComUseCase() {
            ArchRule regra = classes()
                    .that().resideInAPackage(BASE_PACKAGE + ".application.usecase..")
                    .and().areNotInterfaces()
                    .and().haveSimpleNameNotContaining("package-info")
                    .should().haveSimpleNameEndingWith("UseCase");

            regra.check(classes);
        }

        @Test
        @DisplayName("Controllers devem terminar com 'Controller'")
        void controllersDevemTerminarComController() {
            ArchRule regra = classes()
                    .that().resideInAPackage(BASE_PACKAGE + ".interfaces.rest")
                    .and().areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
                    .should().haveSimpleNameEndingWith("Controller");

            regra.check(classes);
        }

        @Test
        @DisplayName("Exceções de domínio devem terminar com 'Exception'")
        void excecoesDevemTerminarComException() {
            ArchRule regra = classes()
                    .that().resideInAPackage(BASE_PACKAGE + ".domain.exception..")
                    .and().areAssignableTo(Exception.class)
                    .should().haveSimpleNameEndingWith("Exception");

            regra.check(classes);
        }

        @Test
        @DisplayName("Mappers REST devem terminar com 'RestMapper'")
        void mappersRestDevemTerminarComRestMapper() {
            ArchRule regra = classes()
                    .that().resideInAPackage(BASE_PACKAGE + ".interfaces.rest.mapper..")
                    .and().areInterfaces()
                    .and().haveSimpleNameNotContaining("package-info")
                    .should().haveSimpleNameEndingWith("RestMapper");

            regra.check(classes);
        }
    }
}
