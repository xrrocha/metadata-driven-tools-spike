package org.metaxava.model;

import javax.persistence.*;
import lombok.*;
import java.util.Optional;

/**
 * OXType - Root of the type hierarchy for MetaXava
 *
 * Represents all types in the Java/JPA type system: primitives, wrappers,
 * classes, entities, embeddables, enums, and parameterized types.
 *
 * This is the foundation for modeling JPA's type system within JPA itself
 * (the Ouroboros principle).
 *
 * @author MetaXava Architecture Session 2025-10-27
 */
@MappedSuperclass
@Getter @Setter
public abstract class OXType {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Validator for this type
     *
     * DESIGN:
     * - Wildcard Validator<?> because OXType is not generic
     * - Concrete subtypes know what they validate:
     *   - OXValueType<Integer> → Validator<Integer> validates integer values
     *   - OXEntityType(Customer) → Validator<Customer> validates customer entities
     * - Defaults to Validator.CATCH_ALL (always valid, no validation)
     * - Stored as FQN string, instantiated via ValidatorClassConverter
     *
     * TODO: Revisit for better type safety - can we avoid Validator<?> wildcard?
     *
     * INTEGRATION:
     * - Metamodel level: Validator<?> instance
     * - Generated code: javax.validation annotations or custom ConstraintValidator
     */
    @Column(name = "validator_class", length = 255)
    @Convert(converter = ValidatorClassConverter.class)
    private Validator<?> validator = Validator.CATCH_ALL;

    /**
     * Canonical type name (fully qualified for object types, simple for primitives)
     *
     * DESIGN CONSIDERATIONS:
     *
     * 1. PRIMITIVES vs WRAPPERS: Distinct types!
     *    - "int" (primitive) != "java.lang.Integer" (wrapper)
     *    - Both must be modeled separately
     *    - Should support interchangeability in user code generation
     *    - TODO: Design mechanism for primitive <-> wrapper conversion
     *
     * 2. UNIQUENESS: Enforced at DB level (unique constraint)
     *    - Prevents duplicate type definitions
     *    - Enables fast lookup by name
     *    - User can override if schema evolution requires it
     *
     * 3. MUTABILITY: Type names CAN change (package refactoring)
     *    - JPA's @Id is immutable, so renaming type_name is safe
     *    - CAUTION: Updating references to non-PK unique columns
     *    - TODO: Study JPA 3.x spec for reference update semantics
     *    - Schema evolution = user responsibility (minefield!)
     *
     * 4. LENGTH: varchar(255)
     *    - Accommodates deeply nested packages + parameterized types
     *    - Example: "com.acme.pkg.Map<com.acme.Customer, com.acme.Order>"
     *    - 128 too short, 512 overkill, 255 = sweet spot
     *    - Can be increased in future pass if needed
     *
     * Examples:
     *   "int"                      - primitive
     *   "java.lang.Integer"        - wrapper (distinct from int!)
     *   "java.lang.String"         - built-in class
     *   "com.example.Customer"     - entity
     */
    @Column(name = "type_name", length = 255, unique = true, nullable = false)
    public abstract String getName();

    /**
     * Generate Java type syntax for field declarations
     *
     * Subclasses implement based on their representation:
     * - Primitives: "int", "boolean"
     * - Classes: "String", "Customer" (simple name)
     * - Parameterized: "List<Customer>", "Map<String, Order>"
     */
    public abstract String generateJavaType();

    /**
     * Generate import statement if needed
     *
     * DESIGN CONSIDERATION:
     * - May not belong in OXType (TBD - revisit in future pass)
     * - Not all types need imports (primitives, java.lang.*)
     * - Decision deferred to subclasses
     *
     * IMPLEMENTATION NOTE:
     * - NEVER return null! Use Optional<String> for optionality
     * - Subclasses override to provide import when applicable
     *
     * @return Optional.empty() if no import needed, Optional.of("import ...") otherwise
     */
    public Optional<String> generateImport() {
        return Optional.empty();  // Default: no import (primitives, java.lang.*)
    }
}
