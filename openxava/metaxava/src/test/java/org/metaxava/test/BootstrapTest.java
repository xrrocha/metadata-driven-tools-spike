package org.metaxava.test;

import org.junit.jupiter.api.Test;
import org.metaxava.bootstrap.TypeSystemBootstrap;
import org.metaxava.model.JDBCTypeMetadata;
import org.metaxava.model.OXPrimitiveType;

import java.sql.JDBCType;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BootstrapTest - Validates TypeSystemBootstrap creates and wires everything correctly
 *
 * CRITICAL VALIDATIONS:
 * 1. All 16 JDBC types created
 * 2. All 8 primitives created
 * 3. All M:N mappings wired
 * 4. Preferred JDBC types set correctly
 * 5. Bidirectional navigation works
 * 6. Can query by JDBC type to find compatible primitives
 * 7. Can reload primitives with their JDBC types (lazy loading works)
 *
 * @author MetaXava Bootstrap Testing 2025-10-28
 */
class BootstrapTest extends JpaTestBase {

    /**
     * TEST 1: Bootstrap succeeds and creates expected counts
     *
     * NOTE: Tests share the same database (H2 in-memory), so bootstrap might
     * already have been called by another test. This test handles both cases.
     */
    @Test
    void bootstrapSucceeds() {
        beginTransaction();

        String result = TypeSystemBootstrap.bootstrap(em);

        commit();

        // Handle both cases: first bootstrap OR already bootstrapped
        boolean isFirstRun = result.contains("Bootstrapped");
        boolean isAlreadyBootstrapped = result.equals("Type system already bootstrapped");

        assertTrue(isFirstRun || isAlreadyBootstrapped,
                  "Result should be either bootstrap success or already bootstrapped. Got: " + result);

        if (isFirstRun) {
            assertTrue(result.contains("16 JDBC"), "Should create 16 JDBC types");
            assertTrue(result.contains("8 primitive"), "Should create 8 primitives");
            assertTrue(result.contains("8 wrapper"), "Should create 8 wrappers");
            assertTrue(result.contains("7 other types"), "Should create 7 other types (String, BigDecimal, BigInteger, Date, sql.Date, sql.Time, sql.Timestamp)");
            assertTrue(result.contains("60 total mapping"), "Should create 60 total mappings");
            System.out.println("✓ Bootstrap result (first run): " + result);
        } else {
            System.out.println("✓ Bootstrap result (already done): " + result);
        }
    }

    /**
     * TEST 2: All JDBC types created with correct metadata
     */
    @Test
    void allJdbcTypesCreated() {
        beginTransaction();
        TypeSystemBootstrap.bootstrap(em);
        commit();

        beginTransaction();

        // Verify count
        Long count = em.createQuery("SELECT COUNT(t) FROM JDBCTypeMetadata t", Long.class)
                      .getSingleResult();
        assertEquals(16, count, "Should have 16 JDBC types");

        // Verify specific types exist with correct categories
        JDBCTypeMetadata varchar = findJdbcType(JDBCType.VARCHAR);
        assertNotNull(varchar, "VARCHAR should exist");
        assertEquals("STRING", varchar.getCategory());
        assertTrue(varchar.isSupportsLength(), "VARCHAR supports length");
        assertTrue(varchar.isCommonlyUsed(), "VARCHAR is commonly used");

        JDBCTypeMetadata integer = findJdbcType(JDBCType.INTEGER);
        assertNotNull(integer, "INTEGER should exist");
        assertEquals("NUMERIC", integer.getCategory());
        assertFalse(integer.isSupportsLength(), "INTEGER doesn't support length");

        JDBCTypeMetadata timestamp = findJdbcType(JDBCType.TIMESTAMP);
        assertNotNull(timestamp, "TIMESTAMP should exist");
        assertEquals("TEMPORAL", timestamp.getCategory());

        commit();

        System.out.println("✓ All 16 JDBC types created with correct metadata");
    }

    /**
     * TEST 3: All primitives created
     */
    @Test
    void allPrimitivesCreated() {
        beginTransaction();
        TypeSystemBootstrap.bootstrap(em);
        commit();

        beginTransaction();

        // Verify count
        Long count = em.createQuery(
            "SELECT COUNT(t) FROM OXPrimitiveType t", Long.class)
            .getSingleResult();
        assertEquals(8, count, "Should have 8 primitives");

        // Verify specific primitives
        OXPrimitiveType intType = findPrimitive("int");
        assertNotNull(intType, "int should exist");
        // Wrapper relationship not yet bootstrapped

        OXPrimitiveType boolType = findPrimitive("boolean");
        assertNotNull(boolType, "boolean should exist");
        // Wrapper relationship not yet bootstrapped

        commit();

        System.out.println("✓ All 8 primitives created");
    }

    /**
     * TEST 4: Primitive-to-JDBC mappings wired correctly
     */
    @Test
    void primitiveMappingsWired() {
        beginTransaction();
        TypeSystemBootstrap.bootstrap(em);
        commit();

        em.clear();

        beginTransaction();

        // Test int → INTEGER, BIGINT, SMALLINT (preferred: INTEGER)
        OXPrimitiveType intType = findPrimitive("int");
        assertNotNull(intType, "int should exist");

        assertNotNull(intType.getPreferredJdbcType(), "int should have preferred JDBC type");
        assertEquals(JDBCType.INTEGER, intType.getPreferredJdbcType().getJdbcType(),
                    "int preferred type should be INTEGER");

        assertNotNull(intType.getCompatibleJdbcTypes(), "int should have compatible JDBC types");
        assertEquals(3, intType.getCompatibleJdbcTypes().size(),
                    "int should have 3 compatible JDBC types");

        List<JDBCType> intJdbcTypes = intType.getCompatibleJdbcTypes().stream()
                .map(JDBCTypeMetadata::getJdbcType)
                .toList();
        assertTrue(intJdbcTypes.contains(JDBCType.INTEGER), "int compatible with INTEGER");
        assertTrue(intJdbcTypes.contains(JDBCType.BIGINT), "int compatible with BIGINT");
        assertTrue(intJdbcTypes.contains(JDBCType.SMALLINT), "int compatible with SMALLINT");

        commit();

        System.out.println("✓ int → 3 JDBC types (preferred: INTEGER)");
    }

    /**
     * TEST 5: All primitives have correct mappings
     */
    @Test
    void allPrimitiveMappingsComplete() {
        beginTransaction();
        TypeSystemBootstrap.bootstrap(em);
        commit();

        em.clear();

        beginTransaction();

        // byte → TINYINT, SMALLINT, INTEGER
        validatePrimitive("byte", JDBCType.TINYINT, 3);

        // short → SMALLINT, INTEGER, BIGINT
        validatePrimitive("short", JDBCType.SMALLINT, 3);

        // int → INTEGER, BIGINT, SMALLINT
        validatePrimitive("int", JDBCType.INTEGER, 3);

        // long → BIGINT, INTEGER
        validatePrimitive("long", JDBCType.BIGINT, 2);

        // float → FLOAT, DOUBLE, NUMERIC
        validatePrimitive("float", JDBCType.FLOAT, 3);

        // double → DOUBLE, FLOAT, NUMERIC
        validatePrimitive("double", JDBCType.DOUBLE, 3);

        // char → CHAR, VARCHAR
        validatePrimitive("char", JDBCType.CHAR, 2);

        // boolean → BOOLEAN, TINYINT
        validatePrimitive("boolean", JDBCType.BOOLEAN, 2);

        commit();

        System.out.println("✓ All primitives have correct mappings");
    }

    /**
     * TEST 6: Join table populated (can query mappings)
     */
    @Test
    void joinTablePopulated() {
        beginTransaction();
        TypeSystemBootstrap.bootstrap(em);
        commit();

        beginTransaction();

        // Query join table directly via native query
        @SuppressWarnings("unchecked")
        List<Object[]> mappings = em.createNativeQuery(
            "SELECT type_id, jdbc_metadata_id FROM type_jdbc_mappings")
            .getResultList();

        assertFalse(mappings.isEmpty(), "Join table should have rows");

        // Total mappings:
        // Primitives:      byte(3) + short(3) + int(3) + long(2) + float(3) + double(3) + char(2) + boolean(2) = 21
        // Wrappers:        Byte(3) + Short(3) + Integer(3) + Long(2) + Float(3) + Double(3) + Character(2) + Boolean(2) = 21
        // String:          VARCHAR + CHAR + CLOB = 3
        // BigDecimal:      NUMERIC + DECIMAL = 2
        // BigInteger:      NUMERIC + DECIMAL + BIGINT = 3
        // util.Date:       TIMESTAMP + DATE + TIME = 3
        // sql.Date:        DATE + TIMESTAMP = 2
        // sql.Time:        TIME + TIMESTAMP = 2
        // sql.Timestamp:   TIMESTAMP + DATE + TIME = 3
        // Total = 60
        assertEquals(60, mappings.size(), "Should have 60 total mappings");

        commit();

        System.out.println("✓ Join table has 60 rows (type_jdbc_mappings)");
    }

    /**
     * TEST 7: Can find primitives by JDBC type (reverse navigation)
     */
    @Test
    void canFindPrimitivesByJdbcType() {
        beginTransaction();
        TypeSystemBootstrap.bootstrap(em);
        commit();

        em.clear();

        beginTransaction();

        // Find all primitives compatible with INTEGER
        @SuppressWarnings("unchecked")
        List<OXPrimitiveType> primitivesUsingInteger = em.createQuery(
            "SELECT p FROM OXPrimitiveType p JOIN p.compatibleJdbcTypes j " +
            "WHERE j.jdbcType = :jdbcType")
            .setParameter("jdbcType", JDBCType.INTEGER)
            .getResultList();

        // byte, short, int, long can all use INTEGER
        assertEquals(4, primitivesUsingInteger.size(),
                    "4 primitives should be compatible with INTEGER");

        List<String> names = primitivesUsingInteger.stream()
                .map(OXPrimitiveType::getName)
                .sorted()
                .toList();
        assertEquals(List.of("byte", "int", "long", "short"), names);

        commit();

        System.out.println("✓ Found 4 primitives compatible with INTEGER: " + names);
    }

    /**
     * TEST 8: Lazy loading works (reload from DB)
     */
    @Test
    void lazyLoadingWorks() {
        beginTransaction();
        TypeSystemBootstrap.bootstrap(em);
        commit();

        em.clear();

        beginTransaction();

        // Load primitive
        OXPrimitiveType intType = findPrimitive("int");
        assertNotNull(intType);

        // Access lazy-loaded collection
        int compatibleCount = intType.getCompatibleJdbcTypes().size();
        assertEquals(3, compatibleCount, "Should load 3 compatible JDBC types");

        // Access lazy-loaded single
        JDBCType preferred = intType.getPreferredJdbcType().getJdbcType();
        assertEquals(JDBCType.INTEGER, preferred, "Should load preferred type");

        commit();

        System.out.println("✓ Lazy loading works for collections and single associations");
    }

    /**
     * TEST 9: Bootstrap is idempotent (can run multiple times)
     */
    @Test
    void bootstrapIsIdempotent() {
        // First bootstrap
        beginTransaction();
        String result1 = TypeSystemBootstrap.bootstrap(em);
        commit();

        assertTrue(result1.contains("16 JDBC types"), "First run should create types");

        // Second bootstrap (should detect existing data)
        beginTransaction();
        String result2 = TypeSystemBootstrap.bootstrap(em);
        commit();

        assertEquals("Type system already bootstrapped", result2,
                    "Second run should skip creation");

        System.out.println("✓ Bootstrap is idempotent");
    }

    // ===== Helper Methods =====

    private JDBCTypeMetadata findJdbcType(JDBCType jdbcType) {
        return em.createQuery(
            "SELECT t FROM JDBCTypeMetadata t WHERE t.jdbcType = :type",
            JDBCTypeMetadata.class)
            .setParameter("type", jdbcType)
            .getSingleResult();
    }

    private OXPrimitiveType findPrimitive(String name) {
        return em.createQuery(
            "SELECT p FROM OXPrimitiveType p WHERE p.name = :name",
            OXPrimitiveType.class)
            .setParameter("name", name)
            .getSingleResult();
    }

    private void validatePrimitive(String name, JDBCType expectedPreferred, int expectedCompatibleCount) {
        OXPrimitiveType primitive = findPrimitive(name);
        assertNotNull(primitive, name + " should exist");

        assertEquals(expectedPreferred, primitive.getPreferredJdbcType().getJdbcType(),
                    name + " preferred type incorrect");

        assertEquals(expectedCompatibleCount, primitive.getCompatibleJdbcTypes().size(),
                    name + " compatible count incorrect");

        System.out.println("  → " + name + " ✓ (preferred: " + expectedPreferred +
                         ", compatible: " + expectedCompatibleCount + ")");
    }
}
