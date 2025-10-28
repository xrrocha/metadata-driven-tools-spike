package org.metaxava.model;

import java.sql.JDBCType;
import java.util.Collection;
import java.util.List;

/**
 * OXBasicType - Cross-cutting concern for types that map to @Basic JPA properties
 *
 * DESIGN RATIONALE:
 * "Basic" types are those that can be mapped to a single database column with a JDBC type.
 * This is a MIXIN interface (not an inheritable class) because the "basic-ness" of a type
 * cuts across Java's natural type hierarchy:
 *
 * - Primitives (int, boolean) are basic
 * - Some classes (String, BigDecimal) are basic
 * - But NOT all classes are basic (entities, embeddables are NOT basic)
 *
 * WHY INTERFACE (not abstract class):
 * Java's single inheritance prevents using abstract class:
 *   - OXStringType must extend OXClassType (type hierarchy position)
 *   - OXStringType must also be basic (cross-cutting concern)
 *   - Solution: OXStringType extends OXClassType implements OXBasicType ✅
 *
 * JPA ANNOTATION REPETITION:
 * JPA does NOT support annotations on interfaces (they're ignored).
 * Each implementing class MUST repeat the following annotations:
 *
 * REQUIRED ANNOTATIONS (copy to each implementation):
 *
 * @ManyToMany
 * @JoinTable(
 *     name = "type_jdbc_mappings",
 *     joinColumns = @JoinColumn(name = "type_id"),
 *     inverseJoinColumns = @JoinColumn(name = "jdbc_metadata_id")
 * )
 * private Collection<JDBCTypeMetadata> compatibleJdbcTypes;
 *
 * @ManyToOne
 * @JoinColumn(name = "preferred_jdbc_type_id")
 * private JDBCTypeMetadata preferredJdbcType;
 *
 * VALIDATION:
 * - preferredJdbcType MUST be a member of compatibleJdbcTypes
 * - Enforced at bootstrap or via @PrePersist/@PreUpdate callback
 *
 * IMPLEMENTING CLASSES:
 * - OXPrimitiveType (byte, short, int, long, float, double, char, boolean)
 * - OXStringType (java.lang.String)
 * - OXBigDecimalType, OXBigIntegerType (java.math.*)
 * - OXWrapperType (Integer, Boolean, etc. - when modeled)
 * - OXDateType, OXTimeType, etc. (java.time.*, java.util.Date, etc.)
 *
 * NOT IMPLEMENTING:
 * - OXEntityType (entities are compositional, not basic)
 * - OXEmbeddableType (embeddables are compositional, not basic)
 *
 * @author MetaXava OXBasicType Session 2025-10-28
 */
public interface OXBasicType {

    /**
     * Get compatible JDBC types for this basic type
     *
     * Examples:
     * - int → [INTEGER, SMALLINT, TINYINT, BIGINT, NUMERIC]
     * - String → [VARCHAR, CHAR, CLOB]
     * - BigDecimal → [NUMERIC, DECIMAL]
     *
     * @return Collection of compatible JDBC types (never null, never empty)
     */
    Collection<JDBCTypeMetadata> getCompatibleJdbcTypes();

    /**
     * Set compatible JDBC types
     *
     * @param types Collection of compatible types
     */
    void setCompatibleJdbcTypes(Collection<JDBCTypeMetadata> types);

    /**
     * Get preferred/recommended JDBC type for this basic type
     *
     * Examples:
     * - int → INTEGER (preferred over SMALLINT/NUMERIC)
     * - String → VARCHAR (preferred over CHAR/CLOB)
     * - BigDecimal → NUMERIC (preferred over DECIMAL)
     *
     * CONSTRAINT: Must be a member of compatibleJdbcTypes
     *
     * @return Preferred JDBC type (never null)
     */
    JDBCTypeMetadata getPreferredJdbcType();

    /**
     * Set preferred JDBC type
     *
     * @param jdbcType Preferred type (must be in compatibleJdbcTypes)
     */
    void setPreferredJdbcType(JDBCTypeMetadata jdbcType);

    /**
     * Declare compatible JDBC types for this basic type
     *
     * This method allows types to be self-describing about their JDBC requirements.
     * Bootstrap resolves these declarations to actual JDBCTypeMetadata entities.
     *
     * Examples:
     * - int → [JDBCType.INTEGER, JDBCType.BIGINT, JDBCType.SMALLINT]
     * - String → [JDBCType.VARCHAR, JDBCType.CHAR, JDBCType.CLOB]
     * - BigDecimal → [JDBCType.NUMERIC, JDBCType.DECIMAL]
     *
     * DESIGN RATIONALE:
     * Rather than hardcoding JDBC mappings in bootstrap, each type declares
     * its own requirements. Bootstrap becomes mechanical: resolve and wire.
     *
     * @return List of compatible JDBC types (never null, never empty)
     */
    List<JDBCType> declareCompatibleJdbcTypes();

    /**
     * Declare preferred JDBC type for this basic type
     *
     * Examples:
     * - int → JDBCType.INTEGER
     * - String → JDBCType.VARCHAR
     * - BigDecimal → JDBCType.NUMERIC
     *
     * CONSTRAINT: Must be a member of declareCompatibleJdbcTypes()
     *
     * @return Preferred JDBC type (never null)
     */
    JDBCType declarePreferredJdbcType();
}
