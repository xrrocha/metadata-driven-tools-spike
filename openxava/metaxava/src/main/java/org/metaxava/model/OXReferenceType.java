package org.metaxava.model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * OXReferenceType - Base for all Java reference types
 *
 * DESIGN:
 * All reference types represent Java classes (unlike primitives which are keywords).
 * This base captures the common characteristics: package name and simple name.
 * The qualified name (FQN) is derived from these two components.
 *
 * SUBCLASSES:
 * - OXBasicReferenceType (String, Integer, BigDecimal, LocalDate, etc.)
 * - OXEnumerationType (user-defined enums)
 * - OXClassType (entities, embeddables)
 *
 * CODE GENERATION:
 * - getQualifiedName() → import statement
 * - simpleName → field type declaration
 *
 * VALIDATION:
 * Bean Validation annotations ensure Java identifier compliance.
 * Currently validates at persist time (JPA lifecycle).
 *
 * TODO: Review all existing types (OXPrimitiveType, etc.) for validation opportunities
 * TODO: Investigate fail-fast validation on entity construction
 *       - Goal: Validate immediately after instance creation (fail-fast)
 *       - Approach: Static singleton validator accessed during construction
 *       - Challenge: Integrate with @SuperBuilder + JPA lifecycle
 *       - Benefit: Catch invalid data at construction, not at persist time
 *       - Deferred: May impose complexity burden; revisit when pain is felt
 *
 * @author MetaXava Reference Types 2025-10-28
 */
@Entity
@DiscriminatorValue("REFERENCE")
@SuperBuilder
@NoArgsConstructor
@Getter @Setter
public abstract class OXReferenceType extends OXType {

    /**
     * Package name
     *
     * Examples:
     * - "java.lang"
     * - "java.time"
     * - "java.math"
     * - "com.acme.domain"
     *
     * VALIDATION:
     * Must be valid Java package name (lowercase, dot-separated identifiers)
     */
    @Column(name = "package_name", length = 300)
    @Pattern(regexp = "^[a-z][a-z0-9_]*(\\.[a-z][a-z0-9_]*)*$",
             message = "Invalid Java package name")
    private String packageName;

    /**
     * Simple class name (no package)
     *
     * Examples:
     * - "String"
     * - "LocalDate"
     * - "BigDecimal"
     * - "Customer"
     *
     * CODE GENERATION:
     * Used in field type declarations
     *
     * VALIDATION:
     * Must be valid Java class name (starts with uppercase, alphanumeric + underscore)
     *
     * NOTE: nullable=false removed due to SINGLE_TABLE inheritance
     * (primitives don't have this field, so column must accept NULL)
     */
    @Column(name = "simple_name", length = 200)
    @Pattern(regexp = "^[A-Z][a-zA-Z0-9_]*$",
             message = "Invalid Java class name")
    private String simpleName;

    /**
     * Qualified class name (FQN)
     *
     * DERIVED from packageName + simpleName
     *
     * Examples:
     * - "java.lang.String"
     * - "java.time.LocalDate"
     * - "java.math.BigDecimal"
     * - "com.acme.domain.Customer"
     *
     * CODE GENERATION:
     * Needed for import statements (unless java.lang.*)
     */
    public String getQualifiedName() {
        if (packageName == null || packageName.isEmpty()) {
            return simpleName;
        }
        return packageName + "." + simpleName;
    }

    /**
     * Needs import statement?
     *
     * java.lang.* types don't need imports
     */
    public boolean needsImport() {
        return !"java.lang".equals(packageName);
    }

    @Override
    public String toString() {
        return String.format("OXReferenceType[%s, %s]",
                           getClass().getSimpleName(),
                           getQualifiedName());
    }
}

