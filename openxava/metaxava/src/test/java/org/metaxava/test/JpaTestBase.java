package org.metaxava.test;

import org.junit.jupiter.api.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * JpaTestBase - Base class for JPA integration tests
 *
 * DESIGN:
 * - One EntityManagerFactory for all tests (expensive to create)
 * - Fresh EntityManager per test (test isolation)
 * - Automatic transaction rollback on teardown (clean state)
 * - H2 in-memory database (fast, isolated)
 *
 * USAGE:
 * Extend this class and write tests using 'em' field:
 *
 * <pre>
 * class MyEntityTest extends JpaTestBase {
 *     {@literal @}Test
 *     void testSomething() {
 *         em.getTransaction().begin();
 *         MyEntity entity = new MyEntity();
 *         em.persist(entity);
 *         em.getTransaction().commit();
 *
 *         assertNotNull(entity.getId());
 *     }
 * }
 * </pre>
 *
 * SCHEMA VALIDATION:
 * Hibernate generates schema on factory creation (see logs).
 * If schema generation fails, @BeforeAll will throw exception.
 *
 * @author MetaXava Testing Infrastructure 2025-10-28
 */
public abstract class JpaTestBase {

    /**
     * Shared EntityManagerFactory (expensive to create, reuse across tests)
     */
    protected static EntityManagerFactory emf;

    /**
     * EntityManager per test (fresh for each test method)
     */
    protected EntityManager em;

    /**
     * Create EntityManagerFactory once for all tests
     *
     * IMPORTANT: Schema generation happens here!
     * If inheritance/mappings are broken, this will fail.
     */
    @BeforeAll
    static void setupFactory() {
        System.out.println("\n=== Creating EntityManagerFactory (schema generation) ===");
        emf = Persistence.createEntityManagerFactory("metaxava-test");
        System.out.println("=== EntityManagerFactory created successfully ===\n");
    }

    /**
     * Create fresh EntityManager before each test
     */
    @BeforeEach
    void setupEntityManager() {
        em = emf.createEntityManager();
    }

    /**
     * Clean up EntityManager after each test
     * - Rollback active transactions (don't pollute DB)
     * - Close EntityManager
     */
    @AfterEach
    void teardownEntityManager() {
        if (em != null) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    /**
     * Close EntityManagerFactory after all tests
     */
    @AfterAll
    static void teardownFactory() {
        if (emf != null) {
            System.out.println("\n=== Closing EntityManagerFactory ===");
            emf.close();
        }
    }

    /**
     * Helper: Start transaction (convenience method)
     */
    protected void beginTransaction() {
        em.getTransaction().begin();
    }

    /**
     * Helper: Commit transaction (convenience method)
     */
    protected void commit() {
        em.getTransaction().commit();
    }

    /**
     * Helper: Rollback transaction (convenience method)
     */
    protected void rollback() {
        em.getTransaction().rollback();
    }

    /**
     * Helper: Flush and clear (force DB sync + clear cache)
     */
    protected void flushAndClear() {
        em.flush();
        em.clear();
    }
}
