package org.metaxava.model;

import javax.persistence.*;
import lombok.*;

/**
 * OXType - Root of the type hierarchy for MetaXava
 *
 * Represents all types in the Java/JPA type system: primitives, wrappers,
 * classes, entities, embeddables, enums, and parameterized types.
 *
 * This is the foundation for modeling JPA's type system within JPA itself
 * (the Ouroboros principle).
 *
 * INHERITANCE STRATEGY:
 * - @Entity (not @MappedSuperclass) - enables polymorphic queries and relationships
 * - SINGLE_TABLE - all types in one table for query simplicity
 * - Discriminator column distinguishes type kinds (PRIMITIVE, STRING, ENTITY, etc.)
 *
 * WHY ENTITY:
 * 1. OXProperty will reference OXType polymorphically: @ManyToOne private OXType type;
 * 2. Enables shared join table for JDBC mappings (all OXBasicType implementations)
 * 3. Allows polymorphic queries: "SELECT t FROM OXType t WHERE ..."
 *
 * @author MetaXava Architecture Session 2025-10-27
 */
@Entity
@Table(name = "ox_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_kind", discriminatorType = DiscriminatorType.STRING, length = 20)
@Getter @Setter
public abstract class OXType {

    @Id
    @GeneratedValue
    private Long id;

    // JDBC type mappings removed - now in OXBasicType interface (cross-cutting concern)
    // Only types implementing OXBasicType have JDBC mappings (primitives, String, BigDecimal, etc.)
    // Entities and embeddables do NOT have JDBC types (they're compositional)

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

    // TODO (FUTURE - Object Types): generateImport() belongs on object types, not here
    //
    // DESIGN RATIONALE FOR REMOVAL:
    // - Primitives never need imports (int, boolean, double)
    // - java.lang.* never needs imports (String, Integer, auto-imported)
    // - Import generation is code-generation concern, not type-system concern
    //
    // WHEN TO ADD BACK:
    // When modeling OXObjectType (entities, embeddables, custom classes):
    //   @Column
    //   private String packageName;  // "com.example.domain"
    //
    //   public String generateImport() {
    //       if (packageName.equals("java.lang")) return null;  // Auto-imported
    //       return "import " + packageName + "." + simpleName + ";";
    //   }
    //
    // Until then: YAGNI. Don't pollute OXType API with premature abstraction.
}
