package org.metaxava.bootstrap;

import org.metaxava.model.JDBCTypeMetadata;
import javax.persistence.EntityManager;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBCTypeBootstrap - Populate JDBC type metadata
 *
 * DESIGN:
 * This is a one-time bootstrap to populate the jdbc_type_metadata table
 * with rich metadata for all JDBC types we support.
 *
 * EXECUTION:
 * Run once at application first start or via migration script.
 * Can be re-run safely (checks for existing data).
 *
 * METADATA IMPACT:
 * This data becomes the vocabulary for the entire tool:
 * - UI: Show/hide fields based on supportsLength/supportsPrecision
 * - Validation: "VARCHAR requires length"
 * - Defaults: VARCHAR(255), NUMERIC(19,4)
 * - Code generation: @Column(length=255) vs @Column(precision=19, scale=4)
 *
 * @author MetaXava Bootstrap Session 2025-10-28
 */
public class JDBCTypeBootstrap {

    /**
     * Bootstrap all JDBC type metadata
     *
     * @param em EntityManager for persistence
     * @return Number of types created
     */
    public static int bootstrap(EntityManager em) {
        // Check if already bootstrapped
        Long count = em.createQuery("SELECT COUNT(t) FROM JDBCTypeMetadata t", Long.class)
                       .getSingleResult();
        if (count > 0) {
            System.out.println("JDBC types already bootstrapped (" + count + " types)");
            return 0;
        }

        List<JDBCTypeMetadata> types = createAllTypes();

        em.getTransaction().begin();
        for (JDBCTypeMetadata type : types) {
            em.persist(type);
        }
        em.getTransaction().commit();

        System.out.println("Bootstrapped " + types.size() + " JDBC types");
        return types.size();
    }

    /**
     * Create metadata for all supported JDBC types
     *
     * CATEGORIES:
     * - STRING: Character data
     * - NUMERIC: Integer and decimal numbers
     * - TEMPORAL: Dates, times, timestamps
     * - BINARY: Binary data
     * - BOOLEAN: Boolean/bit types
     * - OTHER: Arrays, refs, XML, etc.
     */
    private static List<JDBCTypeMetadata> createAllTypes() {
        List<JDBCTypeMetadata> types = new ArrayList<>();

        // ===== STRING TYPES =====

        types.add(new JDBCTypeMetadata(
                JDBCType.VARCHAR,
                "STRING",
                "Variable-length character string. Most common text type. Specify maximum length."
            )
            .withLength(true, 4000)
            .withUsage(true, true)  // common, portable
            .withJpaPattern("@Column(length = %d)")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.CHAR,
                "STRING",
                "Fixed-length character string. Pads with spaces to specified length."
            )
            .withLength(true, 2000)
            .withUsage(false, true)  // uncommon, portable
            .withPortabilityNotes("Less efficient than VARCHAR for variable-length data")
            .withJpaPattern("@Column(length = %d)")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.LONGVARCHAR,
                "STRING",
                "Very long variable-length character string. Use for large text."
            )
            .withLength(true, 2_000_000_000)  // ~2GB
            .withUsage(false, true)  // uncommon (use CLOB instead)
            .withJpaPattern("@Column(length = %d)")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.CLOB,
                "STRING",
                "Character Large Object. For very large text (articles, documents)."
            )
            .withLength(false, null)  // No length specification
            .withUsage(true, false)  // common, but database-specific syntax
            .withPortabilityNotes("Oracle: CLOB, SQL Server: VARCHAR(MAX), MySQL: TEXT")
            .withJpaPattern("@Lob @Column")
        );

        // ===== NUMERIC TYPES =====

        types.add(new JDBCTypeMetadata(
                JDBCType.INTEGER,
                "NUMERIC",
                "32-bit signed integer. Range: -2,147,483,648 to 2,147,483,647."
            )
            .withLength(false, null)
            .withUsage(true, true)  // Most common integer type
            .withJpaPattern("@Column")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.BIGINT,
                "NUMERIC",
                "64-bit signed integer. Range: -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807."
            )
            .withLength(false, null)
            .withUsage(true, true)  // Common for IDs
            .withJpaPattern("@Column")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.SMALLINT,
                "NUMERIC",
                "16-bit signed integer. Range: -32,768 to 32,767."
            )
            .withLength(false, null)
            .withUsage(false, true)  // Less common
            .withJpaPattern("@Column")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.TINYINT,
                "NUMERIC",
                "8-bit signed integer. Range: -128 to 127."
            )
            .withLength(false, null)
            .withUsage(false, false)  // Database-specific semantics
            .withPortabilityNotes("MySQL: 0-255 (unsigned), SQL Server: -128-127 (signed)")
            .withJpaPattern("@Column")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.NUMERIC,
                "NUMERIC",
                "Exact numeric with precision and scale. Use for money, precise calculations."
            )
            .withPrecisionScale(true, 38, 10)
            .withUsage(true, true)  // Standard SQL type
            .withJpaPattern("@Column(precision = %d, scale = %d)")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.DECIMAL,
                "NUMERIC",
                "Exact numeric with precision and scale. Equivalent to NUMERIC."
            )
            .withPrecisionScale(true, 38, 10)
            .withUsage(true, true)
            .withJpaPattern("@Column(precision = %d, scale = %d)")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.FLOAT,
                "NUMERIC",
                "Approximate floating-point number (IEEE 754 double precision)."
            )
            .withLength(false, null)
            .withUsage(false, true)
            .withPortabilityNotes("Approximate arithmetic - not suitable for money")
            .withJpaPattern("@Column")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.DOUBLE,
                "NUMERIC",
                "Double precision floating-point number."
            )
            .withLength(false, null)
            .withUsage(false, true)
            .withPortabilityNotes("Approximate arithmetic - not suitable for money")
            .withJpaPattern("@Column")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.REAL,
                "NUMERIC",
                "Single precision floating-point number."
            )
            .withLength(false, null)
            .withUsage(false, true)
            .withJpaPattern("@Column")
        );

        // ===== TEMPORAL TYPES =====

        types.add(new JDBCTypeMetadata(
                JDBCType.DATE,
                "TEMPORAL",
                "Date without time. Format: YYYY-MM-DD."
            )
            .withLength(false, null)
            .withUsage(true, true)
            .withJpaPattern("@Column")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.TIME,
                "TEMPORAL",
                "Time without date. Format: HH:MM:SS."
            )
            .withLength(false, null)
            .withUsage(false, true)
            .withJpaPattern("@Column")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.TIMESTAMP,
                "TEMPORAL",
                "Date and time with fractional seconds. Most common temporal type."
            )
            .withLength(false, null)
            .withUsage(true, true)
            .withJpaPattern("@Column")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.TIMESTAMP_WITH_TIMEZONE,
                "TEMPORAL",
                "Timestamp with timezone offset. Use for international applications."
            )
            .withLength(false, null)
            .withUsage(false, false)  // Not universally supported
            .withPortabilityNotes("PostgreSQL: TIMESTAMPTZ, Oracle 9i+, not in MySQL")
            .withJpaPattern("@Column")
        );

        // ===== BINARY TYPES =====

        types.add(new JDBCTypeMetadata(
                JDBCType.BINARY,
                "BINARY",
                "Fixed-length binary data."
            )
            .withLength(true, 8000)
            .withUsage(false, true)
            .withJpaPattern("@Column(length = %d)")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.VARBINARY,
                "BINARY",
                "Variable-length binary data. Use for small files, hashes, UUIDs."
            )
            .withLength(true, 8000)
            .withUsage(true, true)
            .withJpaPattern("@Column(length = %d)")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.LONGVARBINARY,
                "BINARY",
                "Very long variable-length binary data."
            )
            .withLength(true, 2_000_000_000)
            .withUsage(false, true)
            .withJpaPattern("@Column(length = %d)")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.BLOB,
                "BINARY",
                "Binary Large Object. For large files, images, documents."
            )
            .withLength(false, null)
            .withUsage(true, false)  // Database-specific
            .withPortabilityNotes("Oracle: BLOB, SQL Server: VARBINARY(MAX), MySQL: BLOB")
            .withJpaPattern("@Lob @Column")
        );

        // ===== BOOLEAN TYPE =====

        types.add(new JDBCTypeMetadata(
                JDBCType.BOOLEAN,
                "BOOLEAN",
                "Boolean true/false value."
            )
            .withLength(false, null)
            .withUsage(true, false)  // Not in Oracle (use NUMBER(1))
            .withPortabilityNotes("Oracle: Use NUMBER(1), SQL Server: BIT, PostgreSQL/MySQL: BOOLEAN")
            .withJpaPattern("@Column")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.BIT,
                "BOOLEAN",
                "Single bit (0 or 1). Database-specific semantics."
            )
            .withLength(false, null)
            .withUsage(false, false)
            .withPortabilityNotes("SQL Server: BIT, MySQL: BIT(n), avoid in portable code")
            .withJpaPattern("@Column")
        );

        // ===== OTHER TYPES (Advanced, less common) =====

        types.add(new JDBCTypeMetadata(
                JDBCType.ARRAY,
                "OTHER",
                "SQL array type. Database-specific."
            )
            .withLength(false, null)
            .withUsage(false, false)
            .withPortabilityNotes("PostgreSQL only. Not in standard SQL.")
        );

        types.add(new JDBCTypeMetadata(
                JDBCType.REF,
                "OTHER",
                "Object reference. Rarely used."
            )
            .withLength(false, null)
            .withUsage(false, false)
            .withPortabilityNotes("Oracle object-relational feature. Avoid in portable code.")
        );

        return types;
    }

    /**
     * Example: Query metadata for tool features
     */
    public static void demonstrateUsage(EntityManager em) {
        // Example 1: Get VARCHAR metadata for smart defaults
        JDBCTypeMetadata varchar = em.createQuery(
            "SELECT t FROM JDBCTypeMetadata t WHERE t.jdbcType = :type",
            JDBCTypeMetadata.class)
            .setParameter("type", JDBCType.VARCHAR)
            .getSingleResult();

        System.out.println("VARCHAR supports length: " + varchar.isSupportsLength());
        System.out.println("Typical max length: " + varchar.getTypicalMaxLength());
        System.out.println("JPA pattern: " + varchar.getJpaAnnotationPattern());

        // Example 2: Find all string types for UI dropdown
        List<JDBCTypeMetadata> stringTypes = em.createQuery(
            "SELECT t FROM JDBCTypeMetadata t WHERE t.category = 'STRING' ORDER BY t.commonlyUsed DESC",
            JDBCTypeMetadata.class)
            .getResultList();

        System.out.println("\nString types (common first):");
        stringTypes.forEach(t -> System.out.println("  - " + t.getJdbcType()));

        // Example 3: Find types that support length (for contextual UI)
        List<JDBCType> lengthSupported = em.createQuery(
            "SELECT t.jdbcType FROM JDBCTypeMetadata t WHERE t.supportsLength = true",
            JDBCType.class)
            .getResultList();

        System.out.println("\nTypes supporting length specification:");
        lengthSupported.forEach(t -> System.out.println("  - " + t));
    }
}
