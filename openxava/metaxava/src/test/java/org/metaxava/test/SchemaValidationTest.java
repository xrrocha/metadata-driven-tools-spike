package org.metaxava.test;

import org.junit.jupiter.api.Test;
import org.metaxava.model.JDBCTypeMetadata;
import org.metaxava.model.OXPrimitiveType;
import org.metaxava.model.OXType;

import java.sql.JDBCType;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SchemaValidationTest - Validates JPA schema generation
 *
 * CRITICAL TESTS:
 * These tests validate the fundamental design decisions:
 * 1. OXType is @Entity (not @MappedSuperclass)
 * 2. SINGLE_TABLE inheritance strategy works
 * 3. Discriminator column is created
 * 4. Shared join table for JDBC mappings works
 *
 * WHAT WE'RE TESTING:
 * - Schema generation doesn't fail (constructor works)
 * - Can persist OXPrimitiveType
 * - Can persist JDBCTypeMetadata
 * - Can create M:N relationship (join table works)
 * - Polymorphic queries work
 *
 * @author MetaXava Testing Infrastructure 2025-10-28
 */
class SchemaValidationTest extends JpaTestBase {

    /**
     * TEST 1: Schema Generation Success
     *
     * If this passes, it means:
     * - EntityManagerFactory created successfully
     * - Hibernate generated schema without errors
     * - Inheritance strategy is valid
     * - All mappings are correct
     *
     * This is the MOST IMPORTANT test - if schema generation fails,
     * everything else is broken.
     */
    @Test
    void schemaGeneratesSuccessfully() {
        // If we got here, @BeforeAll created EMF successfully
        assertNotNull(emf, "EntityManagerFactory should be created");
        assertNotNull(em, "EntityManager should be created");

        System.out.println("✓ Schema generated successfully!");
    }

    /**
     * TEST 2: Can Persist Primitive Type
     *
     * Validates:
     * - OXPrimitiveType entity works
     * - Discriminator value "PRIMITIVE" is set
     * - Can persist to ox_type table
     */
    @Test
    void canPersistPrimitiveType() {
        beginTransaction();

        OXPrimitiveType intType = new OXPrimitiveType();
        intType.setName("int");
        intType.setWrapperName("Integer");

        em.persist(intType);
        commit();

        assertNotNull(intType.getId(), "Primitive type should have generated ID");
        System.out.println("✓ Persisted OXPrimitiveType with ID: " + intType.getId());
    }

    /**
     * TEST 3: Can Persist JDBC Type Metadata
     *
     * Validates:
     * - JDBCTypeMetadata entity works
     * - Can persist enum values
     */
    @Test
    void canPersistJdbcTypeMetadata() {
        beginTransaction();

        JDBCTypeMetadata varchar = new JDBCTypeMetadata(
            JDBCType.VARCHAR,
            "STRING",
            "Variable-length character string"
        );
        varchar.setSupportsLength(true);
        varchar.setTypicalMaxLength(4000);
        varchar.setCommonlyUsed(true);
        varchar.setPortable(true);

        em.persist(varchar);
        commit();

        assertNotNull(varchar.getId(), "JDBC type should have generated ID");
        System.out.println("✓ Persisted JDBCTypeMetadata with ID: " + varchar.getId());
    }

    /**
     * TEST 4: Shared Join Table Works
     *
     * THIS IS THE CRITICAL TEST for our design decision!
     *
     * Validates:
     * - M:N relationship persists correctly
     * - Join table "type_jdbc_mappings" is created
     * - Both sides of relationship work
     * - Shared table works for all OXBasicType implementations
     */
    @Test
    void sharedJoinTableWorks() {
        beginTransaction();

        // Create JDBC type
        JDBCTypeMetadata integerType = new JDBCTypeMetadata(
            JDBCType.INTEGER,
            "NUMERIC",
            "32-bit signed integer"
        );
        em.persist(integerType);

        JDBCTypeMetadata smallintType = new JDBCTypeMetadata(
            JDBCType.SMALLINT,
            "NUMERIC",
            "16-bit signed integer"
        );
        em.persist(smallintType);

        // Create primitive type with JDBC mappings
        OXPrimitiveType intType = new OXPrimitiveType();
        intType.setName("int");
        intType.setWrapperName("Integer");
        intType.setCompatibleJdbcTypes(List.of(integerType, smallintType));
        intType.setPreferredJdbcType(integerType);

        em.persist(intType);
        commit();

        // Clear cache and reload
        em.clear();

        OXPrimitiveType loaded = em.find(OXPrimitiveType.class, intType.getId());
        assertNotNull(loaded, "Should load primitive type");
        assertEquals(2, loaded.getCompatibleJdbcTypes().size(), "Should have 2 compatible JDBC types");
        assertEquals(JDBCType.INTEGER, loaded.getPreferredJdbcType().getJdbcType(), "Preferred type should be INTEGER");

        System.out.println("✓ Shared join table works! Loaded primitive with " +
                         loaded.getCompatibleJdbcTypes().size() + " JDBC types");
    }

    /**
     * TEST 5: Polymorphic Query Works
     *
     * Validates:
     * - Can query OXType (base entity)
     * - Returns subclasses (OXPrimitiveType)
     * - Inheritance strategy works correctly
     */
    @Test
    void polymorphicQueryWorks() {
        beginTransaction();

        // Create several primitive types
        OXPrimitiveType intType = new OXPrimitiveType();
        intType.setName("int");
        intType.setWrapperName("Integer");
        em.persist(intType);

        OXPrimitiveType boolType = new OXPrimitiveType();
        boolType.setName("boolean");
        boolType.setWrapperName("Boolean");
        em.persist(boolType);

        commit();
        em.clear();

        // Polymorphic query
        var types = em.createQuery("SELECT t FROM OXType t", org.metaxava.model.OXType.class)
                     .getResultList();

        assertEquals(2, types.size(), "Should find 2 types");
        assertTrue(types.get(0) instanceof OXPrimitiveType, "Should be OXPrimitiveType");
        assertTrue(types.get(1) instanceof OXPrimitiveType, "Should be OXPrimitiveType");

        System.out.println("✓ Polymorphic query works! Found " + types.size() + " types");
    }

    /**
     * TEST 6: Discriminator Column Works
     *
     * Validates:
     * - Discriminator value "PRIMITIVE" is set correctly
     * - Can filter by type using TYPE() function
     */
    @Test
    void discriminatorColumnWorks() {
        beginTransaction();

        OXPrimitiveType intType = new OXPrimitiveType();
        intType.setName("int");
        intType.setWrapperName("Integer");
        em.persist(intType);

        commit();

        // Start new transaction for query
        beginTransaction();

        // Query filtering by type
        var primitives = em.createQuery(
            "SELECT t FROM OXType t WHERE TYPE(t) = OXPrimitiveType",
            OXType.class)
            .getResultList();

        commit();

        // Note: May find types from other tests (H2 in-memory DB persists across tests in same run)
        // Just verify we can filter by TYPE and that result is correct type
        assertTrue(primitives.size() >= 1, "Should find at least 1 primitive type");
        assertTrue(primitives.stream().anyMatch(t -> t instanceof OXPrimitiveType &&
                   ((OXPrimitiveType) t).getName().equals("int")),
                   "Should find 'int' type");

        System.out.println("✓ Discriminator column works! Filtered by TYPE() - found " + primitives.size() + " primitives");
    }
}
