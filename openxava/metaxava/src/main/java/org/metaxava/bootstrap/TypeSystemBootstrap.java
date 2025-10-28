package org.metaxava.bootstrap;

import org.metaxava.model.JDBCTypeMetadata;
import org.metaxava.model.OXPrimitiveType;
import org.metaxava.model.OXPrimitiveWrapperType;
import org.metaxava.model.OXStringType;
import org.metaxava.model.OXBigDecimalType;
import org.metaxava.model.OXBigIntegerType;
import org.metaxava.model.OXDateType;
import org.metaxava.model.OXSqlDateType;
import org.metaxava.model.OXSqlTimeType;
import org.metaxava.model.OXSqlTimestampType;
import javax.persistence.EntityManager;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TypeSystemBootstrap - Bootstrap primitives + wrappers + JDBC types + mappings
 *
 * CURRENT SCOPE:
 * 1. JDBCTypeMetadata (16 types with rich metadata)
 * 2. OXPrimitiveType (8 Java primitives)
 * 3. OXPrimitiveWrapperType (8 wrapper types: Integer, Boolean, etc.)
 * 4. Bidirectional primitive ↔ wrapper relationships
 * 5. M:N mappings between types and JDBC types
 *
 * NOT YET MODELED (deferred):
 * - OXReferenceType (String, BigDecimal, LocalDate, etc.)
 * - OXClassType, OXEntityType, etc.
 *
 * EXECUTION:
 * Run once at application startup.
 *
 * @author MetaXava Bootstrap Session 2025-10-28
 */
public class TypeSystemBootstrap {

    /**
     * Bootstrap type system (primitives + JDBC types only)
     *
     * TRANSACTION MANAGEMENT:
     * Caller must manage transaction boundaries.
     * This method does NOT begin/commit transactions.
     *
     * @param em EntityManager (must have active transaction)
     * @return Bootstrap result message
     */
    public static String bootstrap(EntityManager em) {
        // Check if already bootstrapped
        Long jdbcCount = em.createQuery("SELECT COUNT(t) FROM JDBCTypeMetadata t", Long.class)
                          .getSingleResult();

        if (jdbcCount > 0) {
            return "Type system already bootstrapped";
        }

        System.out.println("=== MetaXava Type System Bootstrap ===\n");

        // PHASE 1: JDBC type metadata
        System.out.println("PHASE 1: Creating JDBC type metadata...");
        Map<JDBCType, JDBCTypeMetadata> jdbcTypes = createJDBCTypes(em);
        System.out.println("  ✓ Created " + jdbcTypes.size() + " JDBC types\n");

        // PHASE 2: Primitive types
        System.out.println("PHASE 2: Creating primitive types...");
        Map<String, OXPrimitiveType> primitives = createPrimitiveTypes(em);
        System.out.println("  ✓ Created " + primitives.size() + " primitive types\n");

        // PHASE 3: Wire primitive-to-JDBC type mappings
        System.out.println("PHASE 3: Wiring primitive-to-JDBC type mappings...");
        int primitiveMappingsCount = wirePrimitiveJdbcMappings(em, primitives, jdbcTypes);
        System.out.println("  ✓ Created " + primitiveMappingsCount + " primitive-JDBC mappings\n");

        // PHASE 4: Wrapper types
        System.out.println("PHASE 4: Creating wrapper types...");
        Map<String, OXPrimitiveWrapperType> wrappers = createWrapperTypes(em);
        System.out.println("  ✓ Created " + wrappers.size() + " wrapper types\n");

        // PHASE 5: Wire bidirectional primitive ↔ wrapper relationships
        System.out.println("PHASE 5: Wiring primitive ↔ wrapper relationships...");
        wireWrapperRelationships(em, primitives, wrappers);
        System.out.println("  ✓ Wired " + primitives.size() + " bidirectional relationships\n");

        // PHASE 6: Wire wrapper-to-JDBC type mappings
        System.out.println("PHASE 6: Wiring wrapper-to-JDBC type mappings...");
        int wrapperMappingsCount = wireWrapperJdbcMappings(em, wrappers, jdbcTypes);
        System.out.println("  ✓ Created " + wrapperMappingsCount + " wrapper-JDBC mappings\n");

        // PHASE 7: String type
        System.out.println("PHASE 7: Creating String type...");
        OXStringType stringType = createStringType(em, jdbcTypes);
        System.out.println("  ✓ Created String type with " +
                         stringType.getCompatibleJdbcTypes().size() + " JDBC mappings\n");

        // PHASE 8: BigDecimal type
        System.out.println("PHASE 8: Creating BigDecimal type...");
        OXBigDecimalType bigDecimalType = createBigDecimalType(em, jdbcTypes);
        System.out.println("  ✓ Created BigDecimal type with " +
                         bigDecimalType.getCompatibleJdbcTypes().size() + " JDBC mappings\n");

        // PHASE 9: BigInteger type
        System.out.println("PHASE 9: Creating BigInteger type...");
        OXBigIntegerType bigIntegerType = createBigIntegerType(em, jdbcTypes);
        System.out.println("  ✓ Created BigInteger type with " +
                         bigIntegerType.getCompatibleJdbcTypes().size() + " JDBC mappings\n");

        // PHASE 10: java.util.Date type
        System.out.println("PHASE 10: Creating java.util.Date type...");
        OXDateType dateType = createDateType(em, jdbcTypes);
        System.out.println("  ✓ Created java.util.Date type with " +
                         dateType.getCompatibleJdbcTypes().size() + " JDBC mappings\n");

        // PHASE 11: java.sql.Date type
        System.out.println("PHASE 11: Creating java.sql.Date type...");
        OXSqlDateType sqlDateType = createSqlDateType(em, jdbcTypes);
        System.out.println("  ✓ Created java.sql.Date type with " +
                         sqlDateType.getCompatibleJdbcTypes().size() + " JDBC mappings\n");

        // PHASE 12: java.sql.Time type
        System.out.println("PHASE 12: Creating java.sql.Time type...");
        OXSqlTimeType sqlTimeType = createSqlTimeType(em, jdbcTypes);
        System.out.println("  ✓ Created java.sql.Time type with " +
                         sqlTimeType.getCompatibleJdbcTypes().size() + " JDBC mappings\n");

        // PHASE 13: java.sql.Timestamp type
        System.out.println("PHASE 13: Creating java.sql.Timestamp type...");
        OXSqlTimestampType sqlTimestampType = createSqlTimestampType(em, jdbcTypes);
        System.out.println("  ✓ Created java.sql.Timestamp type with " +
                         sqlTimestampType.getCompatibleJdbcTypes().size() + " JDBC mappings\n");

        System.out.println("=== Bootstrap Complete ===\n");

        int totalMappings = primitiveMappingsCount + wrapperMappingsCount +
                          stringType.getCompatibleJdbcTypes().size() +
                          bigDecimalType.getCompatibleJdbcTypes().size() +
                          bigIntegerType.getCompatibleJdbcTypes().size() +
                          dateType.getCompatibleJdbcTypes().size() +
                          sqlDateType.getCompatibleJdbcTypes().size() +
                          sqlTimeType.getCompatibleJdbcTypes().size() +
                          sqlTimestampType.getCompatibleJdbcTypes().size();

        return String.format("Bootstrapped %d JDBC types, %d primitives, %d wrappers, 7 other types, %d total mappings",
                           jdbcTypes.size(), primitives.size(), wrappers.size(), totalMappings);
    }

    /**
     * PHASE 1: Create JDBC type metadata (16 common types)
     */
    private static Map<JDBCType, JDBCTypeMetadata> createJDBCTypes(EntityManager em) {
        Map<JDBCType, JDBCTypeMetadata> types = new HashMap<>();

        // STRING TYPES
        types.put(JDBCType.VARCHAR, persist(em, new JDBCTypeMetadata(
            JDBCType.VARCHAR, "STRING", "Variable-length character string"
        ).withLength(true, 4000).withUsage(true, true)));

        types.put(JDBCType.CHAR, persist(em, new JDBCTypeMetadata(
            JDBCType.CHAR, "STRING", "Fixed-length character string"
        ).withLength(true, 2000).withUsage(false, true)));

        types.put(JDBCType.CLOB, persist(em, new JDBCTypeMetadata(
            JDBCType.CLOB, "STRING", "Character Large Object"
        ).withLength(false, null).withUsage(true, false)
         .withPortabilityNotes("Oracle: CLOB, SQL Server: VARCHAR(MAX)")));

        // NUMERIC TYPES
        types.put(JDBCType.INTEGER, persist(em, new JDBCTypeMetadata(
            JDBCType.INTEGER, "NUMERIC", "32-bit signed integer"
        ).withLength(false, null).withUsage(true, true)));

        types.put(JDBCType.BIGINT, persist(em, new JDBCTypeMetadata(
            JDBCType.BIGINT, "NUMERIC", "64-bit signed integer"
        ).withLength(false, null).withUsage(true, true)));

        types.put(JDBCType.SMALLINT, persist(em, new JDBCTypeMetadata(
            JDBCType.SMALLINT, "NUMERIC", "16-bit signed integer"
        ).withLength(false, null).withUsage(false, true)));

        types.put(JDBCType.TINYINT, persist(em, new JDBCTypeMetadata(
            JDBCType.TINYINT, "NUMERIC", "8-bit signed integer"
        ).withLength(false, null).withUsage(false, false)
         .withPortabilityNotes("MySQL: 0-255, SQL Server: -128-127")));

        types.put(JDBCType.NUMERIC, persist(em, new JDBCTypeMetadata(
            JDBCType.NUMERIC, "NUMERIC", "Exact numeric with precision/scale"
        ).withPrecisionScale(true, 38, 10).withUsage(true, true)));

        types.put(JDBCType.DECIMAL, persist(em, new JDBCTypeMetadata(
            JDBCType.DECIMAL, "NUMERIC", "Exact numeric (equivalent to NUMERIC)"
        ).withPrecisionScale(true, 38, 10).withUsage(true, true)));

        types.put(JDBCType.DOUBLE, persist(em, new JDBCTypeMetadata(
            JDBCType.DOUBLE, "NUMERIC", "Double precision floating-point"
        ).withLength(false, null).withUsage(false, true)
         .withPortabilityNotes("Approximate arithmetic - not for money")));

        types.put(JDBCType.FLOAT, persist(em, new JDBCTypeMetadata(
            JDBCType.FLOAT, "NUMERIC", "Floating-point"
        ).withLength(false, null).withUsage(false, true)
         .withPortabilityNotes("Approximate arithmetic - not for money")));

        // TEMPORAL TYPES
        types.put(JDBCType.DATE, persist(em, new JDBCTypeMetadata(
            JDBCType.DATE, "TEMPORAL", "Date without time"
        ).withLength(false, null).withUsage(true, true)));

        types.put(JDBCType.TIME, persist(em, new JDBCTypeMetadata(
            JDBCType.TIME, "TEMPORAL", "Time without date"
        ).withLength(false, null).withUsage(false, true)));

        types.put(JDBCType.TIMESTAMP, persist(em, new JDBCTypeMetadata(
            JDBCType.TIMESTAMP, "TEMPORAL", "Date and time with fractional seconds"
        ).withLength(false, null).withUsage(true, true)));

        // BOOLEAN TYPE
        types.put(JDBCType.BOOLEAN, persist(em, new JDBCTypeMetadata(
            JDBCType.BOOLEAN, "BOOLEAN", "Boolean true/false"
        ).withLength(false, null).withUsage(true, false)
         .withPortabilityNotes("Oracle: NUMBER(1), SQL Server: BIT")));

        // BINARY TYPES
        types.put(JDBCType.VARBINARY, persist(em, new JDBCTypeMetadata(
            JDBCType.VARBINARY, "BINARY", "Variable-length binary data"
        ).withLength(true, 8000).withUsage(true, true)));

        System.out.println("  Created: VARCHAR, CHAR, CLOB, INTEGER, BIGINT, SMALLINT, TINYINT,");
        System.out.println("           NUMERIC, DECIMAL, DOUBLE, FLOAT, DATE, TIME, TIMESTAMP,");
        System.out.println("           BOOLEAN, VARBINARY");

        return types;
    }

    /**
     * PHASE 2: Create 8 Java primitive types
     */
    private static Map<String, OXPrimitiveType> createPrimitiveTypes(EntityManager em) {
        Map<String, OXPrimitiveType> primitives = new HashMap<>();

        primitives.put("byte", createPrimitive(em, "byte", "Byte"));
        primitives.put("short", createPrimitive(em, "short", "Short"));
        primitives.put("int", createPrimitive(em, "int", "Integer"));
        primitives.put("long", createPrimitive(em, "long", "Long"));
        primitives.put("float", createPrimitive(em, "float", "Float"));
        primitives.put("double", createPrimitive(em, "double", "Double"));
        primitives.put("char", createPrimitive(em, "char", "Character"));
        primitives.put("boolean", createPrimitive(em, "boolean", "Boolean"));

        return primitives;
    }

    /**
     * PHASE 3: Wire primitive types to JDBC types
     *
     * Mappings based on JPA specification and database best practices:
     * - Each primitive can map to multiple JDBC types (compatibility)
     * - Each primitive has ONE preferred JDBC type (code generation default)
     */
    private static int wirePrimitiveJdbcMappings(
            EntityManager em,
            Map<String, OXPrimitiveType> primitives,
            Map<JDBCType, JDBCTypeMetadata> jdbcTypes) {

        int mappingsCount = 0;

        // Wire all primitives using their declared JDBC types
        for (OXPrimitiveType primitive : primitives.values()) {
            List<JDBCTypeMetadata> compatible = resolveJdbcTypes(
                primitive.declareCompatibleJdbcTypes(),
                jdbcTypes
            );
            JDBCTypeMetadata preferred = jdbcTypes.get(primitive.declarePreferredJdbcType());

            primitive.setPreferredJdbcType(preferred);
            primitive.setCompatibleJdbcTypes(compatible);

            System.out.println("  → " + primitive.getName() +
                             " → " + compatible.size() + " JDBC types" +
                             " (preferred: " + preferred.getJdbcType() + ")");

            mappingsCount += compatible.size();
        }

        return mappingsCount;
    }

    /**
     * Helper: Wire a single primitive to its JDBC types
     */
    private static int wirePrimitive(
            EntityManager em,
            OXPrimitiveType primitive,
            JDBCTypeMetadata preferred,
            List<JDBCTypeMetadata> compatible) {

        primitive.setPreferredJdbcType(preferred);
        primitive.setCompatibleJdbcTypes(compatible);

        System.out.println("  → " + primitive.getName() +
                         " → " + compatible.size() + " JDBC types" +
                         " (preferred: " + preferred.getJdbcType() + ")");

        return compatible.size();
    }

    /**
     * Helper: Create and persist a primitive type
     *
     * NOTE: Wrapper relationship (primitiveType ↔ wrapperType) will be wired
     * when we bootstrap wrapper types (OXPrimitiveWrapperType instances).
     */
    private static OXPrimitiveType createPrimitive(EntityManager em, String name, String wrapperName) {
        OXPrimitiveType primitive = new OXPrimitiveType();
        primitive.setName(name);
        // Wrapper relationship deferred - will be set when wrapper types are bootstrapped
        em.persist(primitive);
        System.out.println("  → " + name + " (wrapper: " + wrapperName + " - deferred)");
        return primitive;
    }

    /**
     * Helper: Persist and return
     */
    private static <T> T persist(EntityManager em, T entity) {
        em.persist(entity);
        return entity;
    }

    /**
     * Helper: Resolve declared JDBC types to actual JDBCTypeMetadata entities
     *
     * Types declare their JDBC requirements as List<JDBCType> enums.
     * This method resolves those enums to the actual persisted JDBCTypeMetadata entities.
     *
     * @param declaredTypes List of JDBC type enums declared by a type
     * @param jdbcTypes Map of JDBC enums to metadata entities
     * @return List of resolved JDBCTypeMetadata entities
     */
    private static List<JDBCTypeMetadata> resolveJdbcTypes(
            List<JDBCType> declaredTypes,
            Map<JDBCType, JDBCTypeMetadata> jdbcTypes) {
        return declaredTypes.stream()
            .map(jdbcTypes::get)
            .collect(Collectors.toList());
    }

    /**
     * PHASE 4: Create 8 wrapper types
     */
    private static Map<String, OXPrimitiveWrapperType> createWrapperTypes(EntityManager em) {
        Map<String, OXPrimitiveWrapperType> wrappers = new HashMap<>();

        wrappers.put("byte", createWrapper(em, OXPrimitiveWrapperType.BYTE_SIMPLE_NAME));
        wrappers.put("short", createWrapper(em, OXPrimitiveWrapperType.SHORT_SIMPLE_NAME));
        wrappers.put("int", createWrapper(em, OXPrimitiveWrapperType.INTEGER_SIMPLE_NAME));
        wrappers.put("long", createWrapper(em, OXPrimitiveWrapperType.LONG_SIMPLE_NAME));
        wrappers.put("float", createWrapper(em, OXPrimitiveWrapperType.FLOAT_SIMPLE_NAME));
        wrappers.put("double", createWrapper(em, OXPrimitiveWrapperType.DOUBLE_SIMPLE_NAME));
        wrappers.put("char", createWrapper(em, OXPrimitiveWrapperType.CHARACTER_SIMPLE_NAME));
        wrappers.put("boolean", createWrapper(em, OXPrimitiveWrapperType.BOOLEAN_SIMPLE_NAME));

        return wrappers;
    }

    /**
     * Helper: Create and persist a wrapper type using constants
     */
    private static OXPrimitiveWrapperType createWrapper(EntityManager em, String simpleName) {
        OXPrimitiveWrapperType wrapper = new OXPrimitiveWrapperType();
        wrapper.setPackageName(OXPrimitiveWrapperType.PACKAGE_NAME);
        wrapper.setSimpleName(simpleName);
        em.persist(wrapper);
        System.out.println("  → " + wrapper.getQualifiedName());
        return wrapper;
    }

    /**
     * PHASE 5: Wire bidirectional primitive ↔ wrapper relationships
     */
    private static void wireWrapperRelationships(
            EntityManager em,
            Map<String, OXPrimitiveType> primitives,
            Map<String, OXPrimitiveWrapperType> wrappers) {

        wirePrimitiveWrapper(primitives.get("byte"), wrappers.get("byte"));
        wirePrimitiveWrapper(primitives.get("short"), wrappers.get("short"));
        wirePrimitiveWrapper(primitives.get("int"), wrappers.get("int"));
        wirePrimitiveWrapper(primitives.get("long"), wrappers.get("long"));
        wirePrimitiveWrapper(primitives.get("float"), wrappers.get("float"));
        wirePrimitiveWrapper(primitives.get("double"), wrappers.get("double"));
        wirePrimitiveWrapper(primitives.get("char"), wrappers.get("char"));
        wirePrimitiveWrapper(primitives.get("boolean"), wrappers.get("boolean"));

        System.out.println("  → byte ↔ Byte, short ↔ Short, int ↔ Integer, long ↔ Long");
        System.out.println("  → float ↔ Float, double ↔ Double, char ↔ Character, boolean ↔ Boolean");
    }

    /**
     * Helper: Wire bidirectional primitive ↔ wrapper relationship
     */
    private static void wirePrimitiveWrapper(OXPrimitiveType primitive, OXPrimitiveWrapperType wrapper) {
        primitive.setWrapperType(wrapper);
        wrapper.setPrimitiveType(primitive);
    }

    /**
     * PHASE 6: Wire wrapper-to-JDBC type mappings
     *
     * Wrappers use the same JDBC types as their primitive counterparts.
     */
    private static int wireWrapperJdbcMappings(
            EntityManager em,
            Map<String, OXPrimitiveWrapperType> wrappers,
            Map<JDBCType, JDBCTypeMetadata> jdbcTypes) {

        int mappingsCount = 0;

        // Wire all wrappers using their declared JDBC types
        for (OXPrimitiveWrapperType wrapper : wrappers.values()) {
            List<JDBCTypeMetadata> compatible = resolveJdbcTypes(
                wrapper.declareCompatibleJdbcTypes(),
                jdbcTypes
            );
            JDBCTypeMetadata preferred = jdbcTypes.get(wrapper.declarePreferredJdbcType());

            wrapper.setPreferredJdbcType(preferred);
            wrapper.setCompatibleJdbcTypes(compatible);

            System.out.println("  → " + wrapper.getSimpleName() +
                             " → " + compatible.size() + " JDBC types" +
                             " (preferred: " + preferred.getJdbcType() + ")");

            mappingsCount += compatible.size();
        }

        return mappingsCount;
    }

    /**
     * Helper: Wire a single wrapper to its JDBC types
     */
    private static int wireWrapper(
            OXPrimitiveWrapperType wrapper,
            JDBCTypeMetadata preferred,
            List<JDBCTypeMetadata> compatible) {

        wrapper.setPreferredJdbcType(preferred);
        wrapper.setCompatibleJdbcTypes(compatible);

        System.out.println("  → " + wrapper.getSimpleName() +
                         " → " + compatible.size() + " JDBC types" +
                         " (preferred: " + preferred.getJdbcType() + ")");

        return compatible.size();
    }

    /**
     * PHASE 7: Create String type using constants
     */
    private static OXStringType createStringType(
            EntityManager em,
            Map<JDBCType, JDBCTypeMetadata> jdbcTypes) {

        OXStringType stringType = new OXStringType();
        stringType.setPackageName(OXStringType.PACKAGE_NAME);
        stringType.setSimpleName(OXStringType.SIMPLE_NAME);

        // Use type's declared JDBC mappings
        stringType.setCompatibleJdbcTypes(
            resolveJdbcTypes(stringType.declareCompatibleJdbcTypes(), jdbcTypes)
        );
        stringType.setPreferredJdbcType(
            jdbcTypes.get(stringType.declarePreferredJdbcType())
        );

        em.persist(stringType);

        System.out.println("  → " + stringType.getQualifiedName() +
                         " → " + stringType.getCompatibleJdbcTypes().size() + " JDBC types" +
                         " (preferred: " + stringType.getPreferredJdbcType().getJdbcType() + ")");

        return stringType;
    }

    /**
     * PHASE 8: Create BigDecimal type using constants
     */
    private static OXBigDecimalType createBigDecimalType(
            EntityManager em,
            Map<JDBCType, JDBCTypeMetadata> jdbcTypes) {

        OXBigDecimalType bigDecimalType = new OXBigDecimalType();
        bigDecimalType.setPackageName(OXBigDecimalType.PACKAGE_NAME);
        bigDecimalType.setSimpleName(OXBigDecimalType.SIMPLE_NAME);

        // Use type's declared JDBC mappings
        bigDecimalType.setCompatibleJdbcTypes(
            resolveJdbcTypes(bigDecimalType.declareCompatibleJdbcTypes(), jdbcTypes)
        );
        bigDecimalType.setPreferredJdbcType(
            jdbcTypes.get(bigDecimalType.declarePreferredJdbcType())
        );

        em.persist(bigDecimalType);

        System.out.println("  → " + bigDecimalType.getQualifiedName() +
                         " → " + bigDecimalType.getCompatibleJdbcTypes().size() + " JDBC types" +
                         " (preferred: " + bigDecimalType.getPreferredJdbcType().getJdbcType() + ")");

        return bigDecimalType;
    }

    /**
     * PHASE 9: Create BigInteger type using constants
     */
    private static OXBigIntegerType createBigIntegerType(
            EntityManager em,
            Map<JDBCType, JDBCTypeMetadata> jdbcTypes) {

        OXBigIntegerType bigIntegerType = new OXBigIntegerType();
        bigIntegerType.setPackageName(OXBigIntegerType.PACKAGE_NAME);
        bigIntegerType.setSimpleName(OXBigIntegerType.SIMPLE_NAME);

        // Use type's declared JDBC mappings
        bigIntegerType.setCompatibleJdbcTypes(
            resolveJdbcTypes(bigIntegerType.declareCompatibleJdbcTypes(), jdbcTypes)
        );
        bigIntegerType.setPreferredJdbcType(
            jdbcTypes.get(bigIntegerType.declarePreferredJdbcType())
        );

        em.persist(bigIntegerType);

        System.out.println("  → " + bigIntegerType.getQualifiedName() +
                         " → " + bigIntegerType.getCompatibleJdbcTypes().size() + " JDBC types" +
                         " (preferred: " + bigIntegerType.getPreferredJdbcType().getJdbcType() + ")");

        return bigIntegerType;
    }

    /**
     * PHASE 10: Create java.util.Date type using constants
     */
    private static OXDateType createDateType(
            EntityManager em,
            Map<JDBCType, JDBCTypeMetadata> jdbcTypes) {

        OXDateType dateType = new OXDateType();
        dateType.setPackageName(OXDateType.PACKAGE_NAME);
        dateType.setSimpleName(OXDateType.SIMPLE_NAME);

        // Use type's declared JDBC mappings
        dateType.setCompatibleJdbcTypes(
            resolveJdbcTypes(dateType.declareCompatibleJdbcTypes(), jdbcTypes)
        );
        dateType.setPreferredJdbcType(
            jdbcTypes.get(dateType.declarePreferredJdbcType())
        );

        em.persist(dateType);

        System.out.println("  → " + dateType.getQualifiedName() +
                         " → " + dateType.getCompatibleJdbcTypes().size() + " JDBC types" +
                         " (preferred: " + dateType.getPreferredJdbcType().getJdbcType() + ")");

        return dateType;
    }

    /**
     * PHASE 11: Create java.sql.Date type using constants
     */
    private static OXSqlDateType createSqlDateType(
            EntityManager em,
            Map<JDBCType, JDBCTypeMetadata> jdbcTypes) {

        OXSqlDateType sqlDateType = new OXSqlDateType();
        sqlDateType.setPackageName(OXSqlDateType.PACKAGE_NAME);
        sqlDateType.setSimpleName(OXSqlDateType.SIMPLE_NAME);

        // Use type's declared JDBC mappings
        sqlDateType.setCompatibleJdbcTypes(
            resolveJdbcTypes(sqlDateType.declareCompatibleJdbcTypes(), jdbcTypes)
        );
        sqlDateType.setPreferredJdbcType(
            jdbcTypes.get(sqlDateType.declarePreferredJdbcType())
        );

        em.persist(sqlDateType);

        System.out.println("  → " + sqlDateType.getQualifiedName() +
                         " → " + sqlDateType.getCompatibleJdbcTypes().size() + " JDBC types" +
                         " (preferred: " + sqlDateType.getPreferredJdbcType().getJdbcType() + ")");

        return sqlDateType;
    }

    /**
     * PHASE 12: Create java.sql.Time type using constants
     */
    private static OXSqlTimeType createSqlTimeType(
            EntityManager em,
            Map<JDBCType, JDBCTypeMetadata> jdbcTypes) {

        OXSqlTimeType sqlTimeType = new OXSqlTimeType();
        sqlTimeType.setPackageName(OXSqlTimeType.PACKAGE_NAME);
        sqlTimeType.setSimpleName(OXSqlTimeType.SIMPLE_NAME);

        // Use type's declared JDBC mappings
        sqlTimeType.setCompatibleJdbcTypes(
            resolveJdbcTypes(sqlTimeType.declareCompatibleJdbcTypes(), jdbcTypes)
        );
        sqlTimeType.setPreferredJdbcType(
            jdbcTypes.get(sqlTimeType.declarePreferredJdbcType())
        );

        em.persist(sqlTimeType);

        System.out.println("  → " + sqlTimeType.getQualifiedName() +
                         " → " + sqlTimeType.getCompatibleJdbcTypes().size() + " JDBC types" +
                         " (preferred: " + sqlTimeType.getPreferredJdbcType().getJdbcType() + ")");

        return sqlTimeType;
    }

    /**
     * PHASE 13: Create java.sql.Timestamp type using constants
     */
    private static OXSqlTimestampType createSqlTimestampType(
            EntityManager em,
            Map<JDBCType, JDBCTypeMetadata> jdbcTypes) {

        OXSqlTimestampType sqlTimestampType = new OXSqlTimestampType();
        sqlTimestampType.setPackageName(OXSqlTimestampType.PACKAGE_NAME);
        sqlTimestampType.setSimpleName(OXSqlTimestampType.SIMPLE_NAME);

        // Use type's declared JDBC mappings
        sqlTimestampType.setCompatibleJdbcTypes(
            resolveJdbcTypes(sqlTimestampType.declareCompatibleJdbcTypes(), jdbcTypes)
        );
        sqlTimestampType.setPreferredJdbcType(
            jdbcTypes.get(sqlTimestampType.declarePreferredJdbcType())
        );

        em.persist(sqlTimestampType);

        System.out.println("  → " + sqlTimestampType.getQualifiedName() +
                         " → " + sqlTimestampType.getCompatibleJdbcTypes().size() + " JDBC types" +
                         " (preferred: " + sqlTimestampType.getPreferredJdbcType().getJdbcType() + ")");

        return sqlTimestampType;
    }
}
