package org.metaxava.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.JDBCType;
import java.util.Collection;
import java.util.List;

/**
 * OXPrimitiveType - Java primitive types (byte, short, int, long, float, double, char, boolean)
 *
 * CHARACTERISTICS:
 * - 8 primitive types total
 * - Never nullable (enforced at property level, not here)
 * - Each has a wrapper class (Integer, Boolean, etc.)
 * - Implements OXBasicType (primitives map to JDBC types)
 *
 * INHERITANCE:
 * - Discriminator value: "PRIMITIVE"
 * - Stored in ox_type table (SINGLE_TABLE strategy)
 *
 * NULLABILITY VALIDATION:
 * Primitives cannot be null. This is enforced at property definition level:
 * - When user creates OXProperty with primitive type, nullable MUST be false
 * - Attempting to set nullable=true on primitive property → validation error
 *
 * WRAPPER TYPES:
 * - Stored as string for now (incomplete - will become entity relationship later)
 * - Wrapper types will be modeled as OXWrapperType extends OXClassType implements OXBasicType
 *
 * @author MetaXava Primitives Session 2025-10-28
 */
@Entity
@DiscriminatorValue("PRIMITIVE")
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class OXPrimitiveType extends OXType implements OXBasicType {

    /**
     * Primitive type name ("int", "boolean", "char", etc.)
     *
     * NOTE: nullable=false removed due to SINGLE_TABLE inheritance
     * (wrappers don't have this field, so column must accept NULL)
     */
    @Basic
    @Column(name = "primitive_name", length = 7)
    private String name;

    /**
     * Associated wrapper type
     *
     * Examples:
     * - int → Integer
     * - boolean → Boolean
     * - char → Character
     *
     * Bidirectional relationship with OXBasicReferenceType.primitiveType
     */
    @OneToOne(mappedBy = "primitiveType")
    private OXBasicReferenceType wrapperType;

    // ========== OXBasicType Implementation ==========
    // JPA does NOT support annotations on interfaces, so we must repeat them here.

    /**
     * Compatible JDBC types for this primitive type
     *
     * M:N relationship populated at bootstrap.
     * Examples:
     * - int → [INTEGER, SMALLINT, TINYINT, BIGINT, NUMERIC]
     * - boolean → [BOOLEAN, TINYINT]
     * - double → [DOUBLE, FLOAT, NUMERIC]
     */
    @ManyToMany
    @JoinTable(
        name = "type_jdbc_mappings",
        joinColumns = @JoinColumn(name = "type_id"),
        inverseJoinColumns = @JoinColumn(name = "jdbc_metadata_id")
    )
    private Collection<JDBCTypeMetadata> compatibleJdbcTypes;

    /**
     * Preferred JDBC type for this primitive type
     *
     * Examples:
     * - int → INTEGER (preferred over SMALLINT/NUMERIC)
     * - boolean → BOOLEAN (preferred over TINYINT)
     * - double → DOUBLE (preferred over FLOAT/NUMERIC)
     *
     * VALIDATION: Must be member of compatibleJdbcTypes
     */
    @ManyToOne
    @JoinColumn(name = "preferred_jdbc_type_id")
    private JDBCTypeMetadata preferredJdbcType;

    // ========== OXType Overrides ==========

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String generateJavaType() {
        return name;  // Generates "int", not "Integer"
    }

    // ========== OXBasicType JDBC Declaration Methods ==========

    @Override
    public List<JDBCType> declareCompatibleJdbcTypes() {
        return switch (name) {
            case "byte" -> List.of(JDBCType.TINYINT, JDBCType.SMALLINT, JDBCType.INTEGER);
            case "short" -> List.of(JDBCType.SMALLINT, JDBCType.INTEGER, JDBCType.BIGINT);
            case "int" -> List.of(JDBCType.INTEGER, JDBCType.BIGINT, JDBCType.SMALLINT);
            case "long" -> List.of(JDBCType.BIGINT, JDBCType.INTEGER);
            case "float" -> List.of(JDBCType.FLOAT, JDBCType.DOUBLE, JDBCType.NUMERIC);
            case "double" -> List.of(JDBCType.DOUBLE, JDBCType.FLOAT, JDBCType.NUMERIC);
            case "char" -> List.of(JDBCType.CHAR, JDBCType.VARCHAR);
            case "boolean" -> List.of(JDBCType.BOOLEAN, JDBCType.TINYINT);
            default -> throw new IllegalStateException("Unknown primitive type: " + name);
        };
    }

    @Override
    public JDBCType declarePreferredJdbcType() {
        return switch (name) {
            case "byte" -> JDBCType.TINYINT;
            case "short" -> JDBCType.SMALLINT;
            case "int" -> JDBCType.INTEGER;
            case "long" -> JDBCType.BIGINT;
            case "float" -> JDBCType.FLOAT;
            case "double" -> JDBCType.DOUBLE;
            case "char" -> JDBCType.CHAR;
            case "boolean" -> JDBCType.BOOLEAN;
            default -> throw new IllegalStateException("Unknown primitive type: " + name);
        };
    }
}
