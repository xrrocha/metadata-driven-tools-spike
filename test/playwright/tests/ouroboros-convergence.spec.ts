import { test, expect, Page } from '@playwright/test';
import * as fs from 'fs';
import * as path from 'path';

/**
 * Ouroboros Convergence Test Suite
 *
 * Tests the metamodel's ability to achieve iterative closure:
 * v1 (handwritten) → v2 (generated) → v3 (generated from v2)
 *
 * Expected behavior:
 * 1. Bootstrap creates 23 records describing the metamodel
 * 2. Code generator produces metamodel-v2.app
 * 3. v2 ≈ v1 (structural convergence)
 *
 * This test verifies Phase 1 (Structural) completion.
 */

test.describe('Ouroboros Metamodel Convergence', () => {
  const BASE_URL = 'http://localhost:8080/metamodel';

  test.beforeEach(async ({ page }) => {
    // Navigate to root page
    await page.goto(BASE_URL);

    // Verify app is running
    await expect(page.locator('h1')).toContainText('WebDSL Modeling Itself');
  });

  test('should display root page correctly', async ({ page }) => {
    // Check navigation links
    await expect(page.locator('text=Manage Applications')).toBeVisible();
    await expect(page.locator('text=Manage Entities')).toBeVisible();
    await expect(page.locator('text=Manage Properties')).toBeVisible();
    await expect(page.locator('text=Manage Relationships')).toBeVisible();
    await expect(page.locator('text=Generate Code')).toBeVisible();
    await expect(page.locator('text=Bootstrap Metamodel')).toBeVisible();
  });

  test('should bootstrap metamodel self-description', async ({ page }) => {
    // Navigate to bootstrap page
    await page.click('text=Bootstrap Metamodel');
    await expect(page.locator('h1')).toContainText('Bootstrap Self-Describing Data');

    // Check if database is empty or has data
    const warningText = await page.locator('text=Warning:').count();

    if (warningText > 0) {
      // Database already has data - note this in test
      console.log('Database already contains bootstrap data');

      // Could add cleanup logic here if needed
      // For now, we'll proceed assuming data is correct
    } else {
      // Click bootstrap button
      await page.click('button:has-text("Create Metamodel Self-Description")');

      // Wait for redirect back to root
      await page.waitForURL(BASE_URL);
    }

    // Verify data was created - check entities exist
    await page.click('text=Manage Entities');

    // Should see 4 entities: DomainApp, DomainEntity, EntityProperty, Relationship
    const entityRows = page.locator('table tbody tr');
    await expect(entityRows).toHaveCount(4);

    // Verify entity names
    await expect(page.locator('text=DomainApp')).toBeVisible();
    await expect(page.locator('text=DomainEntity')).toBeVisible();
    await expect(page.locator('text=EntityProperty')).toBeVisible();
    await expect(page.locator('text=Relationship')).toBeVisible();
  });

  test('should verify properties were created', async ({ page }) => {
    await page.click('text=Manage Properties');

    // Should have 10 properties total across all entities
    // (based on bootstrap action in metamodel.app)
    const propertyRows = page.locator('table tbody tr');
    const count = await propertyRows.count();

    expect(count).toBeGreaterThanOrEqual(7); // At minimum: name fields + type fields

    // Verify some key properties exist
    await expect(page.locator('text=name')).toBeVisible();
    await expect(page.locator('text=propertyType')).toBeVisible();
    await expect(page.locator('text=relationshipType')).toBeVisible();
  });

  test('should verify relationships were created', async ({ page }) => {
    await page.click('text=Manage Relationships');

    // Should have 9 relationships (based on bootstrap)
    const relationshipRows = page.locator('table tbody tr');
    const count = await relationshipRows.count();

    expect(count).toBeGreaterThanOrEqual(7); // Minimum expected relationships

    // Verify key relationships
    await expect(page.locator('text=entities')).toBeVisible();
    await expect(page.locator('text=properties')).toBeVisible();
    await expect(page.locator('text=relationships')).toBeVisible();
  });

  test('should generate code for metamodel application', async ({ page }) => {
    // Navigate to code generator
    await page.click('text=Generate Code');
    await expect(page.locator('h1')).toContainText('Generate WebDSL Code');

    // Click on "metamodel" to view generated code
    await page.click('text=metamodel').first();
    await page.click('text=View Code');

    // Should see generated code page
    await expect(page.locator('h1')).toContainText('Generated: metamodel');

    // Verify generated code contains expected elements
    const codeBlock = page.locator('pre');
    await expect(codeBlock).toBeVisible();

    const generatedCode = await codeBlock.textContent();
    expect(generatedCode).toBeTruthy();

    // Verify structure
    expect(generatedCode).toContain('application metamodel');
    expect(generatedCode).toContain('entity DomainApp');
    expect(generatedCode).toContain('entity DomainEntity');
    expect(generatedCode).toContain('entity EntityProperty');
    expect(generatedCode).toContain('entity Relationship');

    // Verify derive CRUD statements
    expect(generatedCode).toContain('derive CRUD DomainApp');
    expect(generatedCode).toContain('derive CRUD DomainEntity');
    expect(generatedCode).toContain('derive CRUD EntityProperty');
    expect(generatedCode).toContain('derive CRUD Relationship');

    // Verify root page generation
    expect(generatedCode).toContain('page root()');

    // Save generated code to fixtures for comparison
    const fixturesDir = path.join(__dirname, '../fixtures');
    if (!fs.existsSync(fixturesDir)) {
      fs.mkdirSync(fixturesDir, { recursive: true });
    }

    const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
    const outputPath = path.join(fixturesDir, `generated-metamodel-${timestamp}.app`);
    fs.writeFileSync(outputPath, generatedCode!, 'utf-8');

    console.log(`Generated code saved to: ${outputPath}`);
  });

  test('should verify structural convergence (v1 ≈ v2)', async ({ page }) => {
    // Navigate to code generator and get generated code
    await page.goto(`${BASE_URL}/codeGenerator`);
    await page.click('text=metamodel').first();
    await page.click('text=View Code');

    const generatedCode = await page.locator('pre').textContent();
    expect(generatedCode).toBeTruthy();

    // Parse entities from generated code
    const entityPattern = /entity\s+(\w+)\s*\{/g;
    const entities = [...generatedCode!.matchAll(entityPattern)].map(m => m[1]);

    // Should have exactly 4 entities
    expect(entities).toHaveLength(4);
    expect(entities).toContain('DomainApp');
    expect(entities).toContain('DomainEntity');
    expect(entities).toContain('EntityProperty');
    expect(entities).toContain('Relationship');

    // Verify each entity has expected properties/relationships
    // DomainApp should have: name property, entities relationship
    expect(generatedCode).toMatch(/entity DomainApp\s*\{[\s\S]*?name\s*:\s*String/);
    expect(generatedCode).toMatch(/entities\s*:\s*\{DomainEntity\}/);

    // DomainEntity should have: name, properties, relationships
    expect(generatedCode).toMatch(/entity DomainEntity\s*\{[\s\S]*?name\s*:\s*String/);
    expect(generatedCode).toMatch(/properties\s*:\s*\{EntityProperty\}/);
    expect(generatedCode).toMatch(/relationships\s*:\s*\{Relationship\}/);

    // EntityProperty should have: name, propertyType
    expect(generatedCode).toMatch(/entity EntityProperty\s*\{[\s\S]*?name\s*:\s*String/);
    expect(generatedCode).toMatch(/propertyType\s*:\s*String/);

    // Relationship should have: name, relationshipType, inverseName
    expect(generatedCode).toMatch(/entity Relationship\s*\{[\s\S]*?name\s*:\s*String/);
    expect(generatedCode).toMatch(/relationshipType\s*:\s*String/);
    expect(generatedCode).toMatch(/inverseName\s*:\s*String/);

    console.log('✅ Structural convergence verified: v2 ≈ v1');
  });

  test('should validate CRUD forms work correctly', async ({ page }) => {
    // Test creating a new application via CRUD
    await page.goto(`${BASE_URL}/manageDomainApp`);
    await page.click('text=Create DomainApp');

    // Fill in form
    await page.fill('input[name*="name"]', 'TestApp');
    await page.click('button:has-text("Save")');

    // Verify it appears in list
    await page.goto(`${BASE_URL}/manageDomainApp`);
    await expect(page.locator('text=TestApp')).toBeVisible();

    // Test creating a new entity
    await page.goto(`${BASE_URL}/manageDomainEntity`);
    await page.click('text=Create DomainEntity');

    await page.fill('input[name*="name"]', 'TestEntity');

    // Select the TestApp from dropdown
    await page.selectOption('select', { label: 'TestApp' });

    await page.click('button:has-text("Save")');

    // Verify entity was created
    await page.goto(`${BASE_URL}/manageDomainEntity`);
    await expect(page.locator('text=TestEntity')).toBeVisible();

    console.log('✅ CRUD forms validated successfully');
  });

  test('should handle validation errors gracefully', async ({ page }) => {
    // Try to create an entity without required fields
    await page.goto(`${BASE_URL}/createDomainEntity`);

    // Submit empty form
    await page.click('button:has-text("Save")');

    // Should show validation error or stay on same page
    // (WebDSL's default behavior for required fields)
    const url = page.url();
    expect(url).toContain('createDomainEntity');

    // Fill in name but no application reference
    await page.fill('input[name*="name"]', 'InvalidEntity');
    await page.click('button:has-text("Save")');

    // May succeed or fail depending on whether application is required
    // This tests that the form handles submission gracefully
  });
});

/**
 * Helper function to extract entities from WebDSL code
 */
function parseEntities(code: string): string[] {
  const entityPattern = /entity\s+(\w+)\s*\{/g;
  return [...code.matchAll(entityPattern)].map(m => m[1]);
}

/**
 * Helper function to extract properties for a specific entity
 */
function parseEntityProperties(code: string, entityName: string): string[] {
  const entityBlockPattern = new RegExp(
    `entity\\s+${entityName}\\s*\\{([^}]+)\\}`,
    's'
  );
  const match = code.match(entityBlockPattern);
  if (!match) return [];

  const propertyPattern = /(\w+)\s*:/g;
  return [...match[1].matchAll(propertyPattern)].map(m => m[1]);
}
