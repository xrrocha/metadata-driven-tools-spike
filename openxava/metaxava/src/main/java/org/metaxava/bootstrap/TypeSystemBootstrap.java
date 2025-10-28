package org.metaxava.bootstrap;

import org.metaxava.model.JDBCTypeMetadata;
import org.metaxava.model.OXPrimitiveType;
import javax.persistence.EntityManager;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TypeSystemBootstrap - Bootstrap ONLY what's been modeled so far
 *
 * CURRENT SCOPE:
 * 1. JDBCTypeMetadata (16 types with rich metadata)
 * 2. OXPrimitiveType (8 Java primitives)
 *
 * NOT YET MODELED (deferred):
 * - OXWrapperType (reference types for Integer, Boolean, etc.)
 * - JDBC type mappings (M:N relationship - requires both source and target types)
 * - OXReferenceType, OXClassType, etc.
 *
 * EXECUTION:
 * Run once at application startup.
 *
 * @author MetaXava Bootstrap Session 2025-10-28 (Minimal Edition)
 */
public class TypeSystemBootstrap {

    /**
     * Bootstrap type system (primitives + JDBC types only)
     */
    public static String bootstrap(EntityManager em) {
        // Check if already bootstrapped
        Long jdbcCount = em.createQuery("SELECT COUNT(t) FROM JDBCTypeMetadata t", Long.class)
                          .getSingleResult();

        if (jdbcCount > 0) {
            return "Type system already bootstrapped";
        }

        System.out.println("=== MetaXava Type System Bootstrap (Minimal) ===\n");

        em.getTransaction().begin();

        // PHASE 1: JDBC type metadata
        System.out.println("PHASE 1: Creating JDBC type metadata...");
        Map<JDBCType, JDBCTypeMetadata> jdbcTypes = createJDBCTypes(em);
        System.out.println("  ✓ Created " + jdbcTypes.size() + " JDBC types\n");

        // PHASE 2: Primitive types
        System.out.println("PHASE 2: Creating primitive types...");
        List<OXPrimitiveType> primitives = createPrimitiveTypes(em);
        System.out.println("  ✓ Created " + primitives.size() + " primitive types\n");

        // TODO: PHASE 3: Wire M:N relationships (requires wrapper types modeled)
        System.out.println("TODO: Wire M:N relationships (deferred - need wrapper types)\n");

        em.getTransaction().commit();

        System.out.println("=== Bootstrap Complete ===\n");

        return String.format("Bootstrapped %d JDBC types, %d primitives",
                           jdbcTypes.size(), primitives.size());
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
    private static List<OXPrimitiveType> createPrimitiveTypes(EntityManager em) {
        List<OXPrimitiveType> primitives = new ArrayList<>();

        primitives.add(createPrimitive(em, "byte", "Byte"));
        primitives.add(createPrimitive(em, "short", "Short"));
        primitives.add(createPrimitive(em, "int", "Integer"));
        primitives.add(createPrimitive(em, "long", "Long"));
        primitives.add(createPrimitive(em, "float", "Float"));
        primitives.add(createPrimitive(em, "double", "Double"));
        primitives.add(createPrimitive(em, "char", "Character"));
        primitives.add(createPrimitive(em, "boolean", "Boolean"));

        return primitives;
    }

    /**
     * Helper: Create and persist a primitive type
     */
    private static OXPrimitiveType createPrimitive(EntityManager em, String name, String wrapperName) {
        OXPrimitiveType primitive = new OXPrimitiveType();
        primitive.setName(name);
        primitive.setWrapperName(wrapperName);
        em.persist(primitive);
        System.out.println("  → " + name + " (wrapper: " + wrapperName + ")");
        return primitive;
    }

    /**
     * Helper: Persist and return
     */
    private static <T> T persist(EntityManager em, T entity) {
        em.persist(entity);
        return entity;
    }
}
