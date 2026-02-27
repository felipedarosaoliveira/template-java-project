package br.com.company.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Architecture Tests using ArchUnit
 * These tests enforce the architectural rules defined in architecture.md
 *
 * This ensures that:
 * - Domain layer remains framework-independent
 * - Dependency direction is correct
 * - Layer boundaries are respected
 */
class ArchitectureTest {

    private static JavaClasses classes;

    @BeforeAll
    static void setUp() {
        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("br.com.company");
    }

    /**
     * CRITICAL RULE: Domain layer must not depend on any framework
     */
    @Test
    void domainLayerShouldNotDependOnSpring() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..core.domain..")
                .should().dependOnClassesThat().resideInAnyPackage("org.springframework..")
                .because("Domain layer must remain framework-independent as per architecture.md section 1.2");

        rule.check(classes);
    }

    /**
     * CRITICAL RULE: Domain layer must not depend on Application layer
     */
    @Test
    void domainLayerShouldNotDependOnApplicationLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..core.domain..")
                .should().dependOnClassesThat().resideInAPackage("..application..")
                .because("Domain must not depend on outer layers (Dependency Rule)");

        rule.check(classes);
    }

    /**
     * Domain layer should only contain pure Java classes
     */
    @Test
    void domainClassesShouldNotHaveSpringAnnotations() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..core.domain..")
                .should().beAnnotatedWith("org.springframework.stereotype.Service")
                .orShould().beAnnotatedWith("org.springframework.stereotype.Component")
                .orShould().beAnnotatedWith("org.springframework.stereotype.Repository")
                .because("Domain classes must be framework-independent POJOs");

        rule.check(classes);
    }

    /**
     * Controllers should only call Application Services, not Domain directly
     */
    @Test
    void controllersShouldOnlyDependOnApplicationUseCases() {
        ArchRule rule = classes()
                .that().resideInAPackage("..infrastructure.web..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "..application.usecase..",
                        "..infrastructure.web..",
                        "..core.domain.model..",  // DTOs from domain are OK
                        "java..",
                        "org.springframework..",
                        "lombok.."
                )
                .because("Controllers should call Application Services, not Domain directly");

        rule.check(classes);
    }

    /**
     * Enforce layered architecture
     */
    @Test
    void shouldFollowLayeredArchitecture() {
        layeredArchitecture()
                .consideringAllDependencies()

                .layer("Domain").definedBy("..core.domain..")
                .layer("Application").definedBy("..application.usecase..")
                .layer("Infrastructure").definedBy("..infrastructure..")

                .whereLayer("Domain").mayNotAccessAnyLayer()
                .whereLayer("Application").mayOnlyAccessLayers("Domain")
                .whereLayer("Infrastructure").mayOnlyAccessLayers("Application", "Domain")

                .because("Layered architecture must follow the Dependency Rule")
                .check(classes);
    }

    /**
     * Domain repositories must be interfaces, not concrete classes
     */
    @Test
    void domainRepositoriesShouldBeInterfaces() {
        ArchRule rule = classes()
                .that().resideInAPackage("..core.domain.repository..")
                .should().beInterfaces()
                .because("Domain should define repository interfaces (ports), not implementations");

        rule.check(classes);
    }

    /**
     * Repository implementations must be in infrastructure layer
     */
    @Test
    void repositoryImplementationsShouldBeInInfrastructureLayer() {
        ArchRule rule = classes()
                .that().implement(com.tngtech.archunit.base.DescribedPredicate.describe(
                        "repository interfaces",
                        clazz -> clazz.getPackageName().contains("core.domain.repository")
                ))
                .should().resideInAPackage("..infrastructure.persistence..")
                .because("Repository implementations (adapters) belong in infrastructure layer");

        rule.check(classes);
    }
}

