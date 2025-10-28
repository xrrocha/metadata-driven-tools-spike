package org.metaxava.model;

import javax.persistence.*;
import lombok.*;
import java.sql.JDBCType;

/**
 * JDBCTypeMetadata - Rich metadata for JDBC types
 *
 * DESIGN RATIONALE:
 * MetaXava is a TOOL, not an application. The metadata we model becomes the
 * vocabulary for users building their applications. Rich metadata enables:
 * - Contextual validation (disable length input for INTEGER)
 * - Smart UI (show precision/scale only for NUMERIC types)
 * - Code generation (know which JPA annotations to emit)
 * - User guidance (typical sizes, database portability notes)
 *
 * This entity captures the characteristics of each JDBC type to enable
 * precise modeling and generation.
 *
 * JDBC TYPES ENUMERATION:
 * java.sql.JDBCType is the authoritative enum (Java 8+, JDBC 4.2)
 * Contains ~40 standard SQL types: VARCHAR, INTEGER, TIMESTAMP, etc.
 *
 * BOOTSTRAP:
 * One row per JDBC type (static, populated at first run)
 * Example: JDBCType.VARCHAR → supportsLength=true, typical 4000 chars
 *
 * @author MetaXava Architecture Session 2025-10-28
 */
@Entity
@Table(name = "jdbc_type_metadata")
@Getter @Setter
public class JDBCTypeMetadata {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * The JDBC type this metadata describes
     *
     * DESIGN:
     * - Stored as STRING (readable, portable)
     * - Unique constraint (one metadata row per JDBC type)
     * - java.sql.JDBCType enum (Java 8+ standard)
     *
     * Examples: VARCHAR, INTEGER, TIMESTAMP, NUMERIC, CLOB
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "jdbc_type", unique = true, nullable = false, length = 50)
    private JDBCType jdbcType;

    /**
     * Type category for grouping in UI
     *
     * DESIGN:
     * - Helps organize types in dropdowns/wizards
     * - Enables category-based filtering
     *
     * Categories:
     * - STRING: CHAR, VARCHAR, LONGVARCHAR, CLOB
     * - NUMERIC: INTEGER, BIGINT, SMALLINT, TINYINT, NUMERIC, DECIMAL
     * - TEMPORAL: DATE, TIME, TIMESTAMP
     * - BINARY: BINARY, VARBINARY, LONGVARBINARY, BLOB
     * - BOOLEAN: BOOLEAN, BIT
     * - OTHER: ARRAY, REF, JAVA_OBJECT, etc.
     */
    @Column(name = "category", length = 20, nullable = false)
    private String category;

    /**
     * Human-readable description for UI help text
     *
     * Examples:
     * - VARCHAR: "Variable-length character string, specify max length"
     * - NUMERIC: "Exact numeric with precision and scale"
     * - TIMESTAMP: "Date and time with fractional seconds"
     */
    @Column(length = 500)
    private String description;

    /**
     * Does this type support length specification? (e.g., VARCHAR(255))
     *
     * TRUE for: CHAR, VARCHAR, NCHAR, NVARCHAR, BINARY, VARBINARY
     * FALSE for: INTEGER, TIMESTAMP, BOOLEAN
     *
     * UI IMPACT:
     * - If true, show "Length" input field
     * - If false, hide "Length" field
     */
    @Column(nullable = false)
    private boolean supportsLength = false;

    /**
     * Does this type support precision specification? (e.g., NUMERIC(19,4))
     *
     * TRUE for: NUMERIC, DECIMAL
     * FALSE for: VARCHAR, INTEGER, TIMESTAMP
     *
     * UI IMPACT:
     * - If true, show "Precision" input field
     * - Enable scale input only if supportsPrecision is true
     */
    @Column(nullable = false)
    private boolean supportsPrecision = false;

    /**
     * Does this type support scale specification? (e.g., NUMERIC(19,4))
     *
     * TRUE for: NUMERIC, DECIMAL
     * FALSE for: INTEGER (even though it's numeric)
     *
     * NOTE: Scale is typically only valid when precision is specified
     */
    @Column(nullable = false)
    private boolean supportsScale = false;

    /**
     * Typical/recommended maximum length for this type
     *
     * PURPOSE: User guidance and validation
     *
     * Examples:
     * - VARCHAR: 4000 (Oracle), 8000 (SQL Server), 65535 (MySQL)
     * - CHAR: 2000 (Oracle), 8000 (SQL Server)
     * - NULL for types that don't support length
     *
     * DESIGN NOTE:
     * Database-specific limits vary. This stores a conservative "safe" default.
     * Future: Could be per-database-dialect if needed.
     */
    @Column(name = "typical_max_length")
    private Integer typicalMaxLength;

    /**
     * Typical precision for numeric types
     *
     * Examples:
     * - NUMERIC: 38 (Oracle), 38 (SQL Server), 65 (MySQL)
     * - DECIMAL: Same as NUMERIC
     * - NULL for non-numeric types
     */
    @Column(name = "typical_max_precision")
    private Integer typicalMaxPrecision;

    /**
     * Typical scale for numeric types
     *
     * Examples:
     * - NUMERIC: Often 0-4 for money, 0 for integers
     * - Usually smaller than precision
     * - NULL for non-numeric types
     */
    @Column(name = "typical_max_scale")
    private Integer typicalMaxScale;

    /**
     * Is this type commonly used? (for UI prioritization)
     *
     * COMMON (true): VARCHAR, INTEGER, BIGINT, TIMESTAMP, BOOLEAN, NUMERIC
     * UNCOMMON (false): ARRAY, REF, JAVA_OBJECT, DATALINK, ROWID
     *
     * UI IMPACT:
     * - Show common types first in dropdowns
     * - Separate "Advanced Types" section for uncommon types
     */
    @Column(nullable = false)
    private boolean commonlyUsed = true;

    /**
     * Is this type portable across databases?
     *
     * PORTABLE (true): VARCHAR, INTEGER, TIMESTAMP, NUMERIC (SQL standard)
     * NON-PORTABLE (false): LONGTEXT (MySQL), IMAGE (SQL Server)
     *
     * UI IMPACT:
     * - Warning icon for non-portable types
     * - Tooltip: "This type is database-specific"
     */
    @Column(nullable = false)
    private boolean portable = true;

    /**
     * Database-specific notes (portability warnings, limitations)
     *
     * Examples:
     * - CLOB: "Oracle uses CLOB, SQL Server uses VARCHAR(MAX)"
     * - TEXT: "MySQL/PostgreSQL extension, use VARCHAR in standard SQL"
     * - BOOLEAN: "Not supported in Oracle (use NUMBER(1) instead)"
     */
    @Column(length = 1000)
    private String portabilityNotes;

    /**
     * JPA mapping hint: Which @Column attributes to use
     *
     * DESIGN:
     * Code generator uses this to emit correct JPA annotations
     *
     * Examples:
     * - VARCHAR(255): "@Column(length = 255)"
     * - NUMERIC(19,4): "@Column(precision = 19, scale = 4)"
     * - INTEGER: "@Column" (no size attributes)
     * - TIMESTAMP: "@Column" (no size attributes)
     *
     * TODO: This might be better as a separate code generation module
     * rather than stored string. Revisit in code-gen phase.
     */
    @Column(length = 500)
    private String jpaAnnotationPattern;

    // No-arg constructor (required by JPA)
    public JDBCTypeMetadata() {}

    // Constructor for bootstrap convenience
    public JDBCTypeMetadata(JDBCType jdbcType, String category, String description) {
        this.jdbcType = jdbcType;
        this.category = category;
        this.description = description;
    }

    /**
     * Fluent builder methods for bootstrap
     */
    public JDBCTypeMetadata withLength(boolean supports, Integer typicalMax) {
        this.supportsLength = supports;
        this.typicalMaxLength = typicalMax;
        return this;
    }

    public JDBCTypeMetadata withPrecisionScale(boolean supports, Integer typicalPrecision, Integer typicalScale) {
        this.supportsPrecision = supports;
        this.supportsScale = supports;  // Scale only valid with precision
        this.typicalMaxPrecision = typicalPrecision;
        this.typicalMaxScale = typicalScale;
        return this;
    }

    public JDBCTypeMetadata withUsage(boolean common, boolean portable) {
        this.commonlyUsed = common;
        this.portable = portable;
        return this;
    }

    public JDBCTypeMetadata withPortabilityNotes(String notes) {
        this.portabilityNotes = notes;
        return this;
    }

    public JDBCTypeMetadata withJpaPattern(String pattern) {
        this.jpaAnnotationPattern = pattern;
        return this;
    }

    @Override
    public String toString() {
        return String.format("JDBCTypeMetadata[%s, category=%s, length=%s, precision=%s]",
                jdbcType, category,
                supportsLength ? "yes" : "no",
                supportsPrecision ? "yes" : "no");
    }
}
