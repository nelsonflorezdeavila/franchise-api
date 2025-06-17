package com.nequi.franchise.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class ArchitectureTest {

    private static final String DOMAIN_PACKAGE = "com.nequi.franchise.domain..";
    private static final String APPLICATION_PACKAGE = "com.nequi.franchise.application..";
    private static final String INFRASTRUCTURE_PACKAGE = "com.nequi.franchise.infrastructure..";
    private static final String PRESENTATION_PACKAGE = "com.nequi.franchise.presentation..";
    
    private static JavaClasses importedClasses;
    
    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("com.nequi.franchise");
    }

    @Test
    void testCleanArchitectureLayers() {
        Architectures.layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("Domain").definedBy(DOMAIN_PACKAGE)
            .layer("Application").definedBy(APPLICATION_PACKAGE)
            .layer("Infrastructure").definedBy(INFRASTRUCTURE_PACKAGE)
            .layer("Presentation").definedBy(PRESENTATION_PACKAGE)
            .whereLayer("Presentation").mayOnlyBeAccessedByLayers("Presentation")
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Presentation", "Infrastructure")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure")
            .check(importedClasses);
    }

    @Test
    void testControllerLayerNaming() {
        classes()
            .that().resideInAPackage("..presentation..controller..")
            .should().haveSimpleNameEndingWith("Controller")
            .check(importedClasses);
    }

    @Test
    void testServiceLayerNaming() {
        classes()
            .that().resideInAPackage("..application..service..")
            .should().haveSimpleNameEndingWith("Service")
            .orShould().haveSimpleNameEndingWith("ServiceImpl")
            .check(importedClasses);
    }
}
