import { chromium } from '@playwright/test';
import { writeFile } from 'fs/promises';

/**
 * Ouroboros SQL Exporter
 * Simpler approach: Use WebDSL's generated pages to dump data as INSERTs
 */

(async () => {
  console.log('🐍 Ouroboros SQL Exporter (Web Scraping Edition)');
  console.log('Extracting metamodel data from running WebDSL app...\n');

  const browser = await chromium.launch({ headless: true });
  const page = await browser.newPage();

  let sql = `-- Ouroboros Metamodel SQL Export
-- Generated: ${new Date().toISOString()}
-- Extracted via web scraping from WebDSL UI
-- Target: PostgreSQL

BEGIN;

`;

  const sqlEscape = (str) => {
    if (!str || str === '') return 'NULL';
    return "'" + str.replace(/'/g, "''").replace(/\n/g, '\\n') + "'";
  };

  const getListItems = async (url) => {
    await page.goto(url);
    await page.waitForLoadState('networkidle');
    // Generic selector: get all links in list items that aren't edit links
    const items = await page.$$('list li a:not([href*="edit"]):not([href*="create"])');
    const urls = [];
    for (const item of items) {
      const href = await item.getAttribute('href');
      // Only include detail page URLs (not forms)
      if (href && !href.includes('form') && !href.includes('remove')) {
        urls.push(href);
      }
    }
    return urls;
  };

  const getFieldValue = async (fieldName) => {
    // Find the table row with a label containing the field name, then get the value from the next TD
    const rows = await page.$$('table tr');
    for (const row of rows) {
      const labelCell = await row.$('td label');
      if (labelCell) {
        const labelText = await labelCell.textContent();
        if (labelText.trim().toLowerCase().startsWith(fieldName.toLowerCase())) {
          // Get the second TD in this row (the value cell)
          const valueCell = await row.$('td:last-child');
          if (valueCell) {
            const value = await valueCell.textContent();
            return value.trim();
          }
        }
      }
    }
    return null;
  };

  // Strategy: Visit each entity detail page and extract displayed data
  // Then convert to SQL INSERTs

  console.log('📦 Extracting DomainApp...');
  const appUrls = await getListItems('http://localhost:8080/metamodel/manageDomainApp');
  const apps = [];
  for (const url of appUrls) {
    await page.goto(url);
    await page.waitForLoadState('networkidle');
    const name = await getFieldValue('Name:');
    if (name) {
      apps.push({ name });
      sql += `INSERT INTO DomainApp (name) VALUES (${sqlEscape(name)});\n`;
    }
  }
  console.log(`  Found ${apps.length} apps`);
  sql += '\n';

  console.log('🏗️  Extracting DomainEntity...');
  const entityUrls = await getListItems('http://localhost:8080/metamodel/manageDomainEntity');
  const entities = [];
  for (const url of entityUrls) {
    await page.goto(url);
    await page.waitForLoadState('networkidle');
    const name = await getFieldValue('Name:');
    const application = await getFieldValue('Application:');
    if (name) {
      entities.push({ name, application });
      sql += `INSERT INTO DomainEntity (name, application_id) VALUES (${sqlEscape(name)}, (SELECT id FROM DomainApp WHERE name = ${sqlEscape(application || 'metamodel')}));\n`;
    }
  }
  console.log(`  Found ${entities.length} entities`);
  sql += '\n';

  console.log('📝 Extracting EntityProperty...');
  const propUrls = await getListItems('http://localhost:8080/metamodel/manageEntityProperty');
  for (const url of propUrls) {
    await page.goto(url);
    await page.waitForLoadState('networkidle');
    const name = await getFieldValue('Name:');
    const propertyType = await getFieldValue('Property Type:');
    const entity = await getFieldValue('Entity:');
    if (name && entity) {
      sql += `INSERT INTO EntityProperty (name, propertyType, entity_id) VALUES (${sqlEscape(name)}, ${sqlEscape(propertyType || 'String')}, (SELECT id FROM DomainEntity WHERE name = ${sqlEscape(entity)}));\n`;
    }
  }
  console.log(`  Found ${propUrls.length} properties`);
  sql += '\n';

  console.log('🔗 Extracting Relationship...');
  const relUrls = await getListItems('http://localhost:8080/metamodel/manageRelationship');
  for (const url of relUrls) {
    await page.goto(url);
    await page.waitForLoadState('networkidle');
    const name = await getFieldValue('Name:');
    const targetEntity = await getFieldValue('Target Entity:');
    const relationshipType = await getFieldValue('Relationship Type:');
    const inverseName = await getFieldValue('Inverse Name:');
    const sourceEntity = await getFieldValue('Source Entity:');
    if (name && sourceEntity) {
      sql += `INSERT INTO Relationship (name, targetEntity, relationshipType, inverseName, sourceEntity_id) VALUES (${sqlEscape(name)}, ${sqlEscape(targetEntity || '')}, ${sqlEscape(relationshipType || 'OneToMany')}, ${sqlEscape(inverseName || '')}, (SELECT id FROM DomainEntity WHERE name = ${sqlEscape(sourceEntity)}));\n`;
    }
  }
  console.log(`  Found ${relUrls.length} relationships`);
  sql += '\n';

  console.log('✅ Extracting ValidationRule...');
  const valUrls = await getListItems('http://localhost:8080/metamodel/manageValidationRule');
  for (const url of valUrls) {
    await page.goto(url);
    await page.waitForLoadState('networkidle');
    const name = await getFieldValue('Name:');
    const expression = await getFieldValue('Expression:');
    const errorMessage = await getFieldValue('Error Message:');
    const entity = await getFieldValue('Entity:');
    if (name && entity) {
      sql += `INSERT INTO ValidationRule (name, expression, errorMessage, entity_id) VALUES (${sqlEscape(name)}, ${sqlEscape(expression || '')}, ${sqlEscape(errorMessage || '')}, (SELECT id FROM DomainEntity WHERE name = ${sqlEscape(entity)}));\n`;
    }
  }
  console.log(`  Found ${valUrls.length} validation rules`);
  sql += '\n';

  console.log('🧮 Extracting DerivedProperty...');
  const derivedUrls = await getListItems('http://localhost:8080/metamodel/manageDerivedProperty');
  for (const url of derivedUrls) {
    await page.goto(url);
    await page.waitForLoadState('networkidle');
    const name = await getFieldValue('Name:');
    const propertyType = await getFieldValue('Property Type:');
    const expression = await getFieldValue('Expression:');
    const entity = await getFieldValue('Entity:');
    if (name && entity) {
      sql += `INSERT INTO DerivedProperty (name, propertyType, expression, entity_id) VALUES (${sqlEscape(name)}, ${sqlEscape(propertyType || 'String')}, ${sqlEscape(expression || '')}, (SELECT id FROM DomainEntity WHERE name = ${sqlEscape(entity)}));\n`;
    }
  }
  console.log(`  Found ${derivedUrls.length} derived properties`);
  sql += '\n';

  console.log('⚙️  Extracting EntityFunction...');
  const funcUrls = await getListItems('http://localhost:8080/metamodel/manageEntityFunction');
  for (const url of funcUrls) {
    await page.goto(url);
    await page.waitForLoadState('networkidle');
    const name = await getFieldValue('Name:');
    const returnType = await getFieldValue('Return Type:');
    const body = await getFieldValue('Body:');
    const entity = await getFieldValue('Entity:');
    if (name && entity) {
      sql += `INSERT INTO EntityFunction (name, returnType, body, entity_id) VALUES (${sqlEscape(name)}, ${sqlEscape(returnType || 'void')}, ${sqlEscape(body || '')}, (SELECT id FROM DomainEntity WHERE name = ${sqlEscape(entity)}));\n`;
    }
  }
  console.log(`  Found ${funcUrls.length} functions`);
  sql += '\n';

  console.log('📄 Extracting Page...');
  const pageUrls = await getListItems('http://localhost:8080/metamodel/managePage');
  for (const url of pageUrls) {
    await page.goto(url);
    await page.waitForLoadState('networkidle');
    const name = await getFieldValue('Name:');
    const application = await getFieldValue('Application:');
    if (name) {
      sql += `INSERT INTO Page (name, application_id) VALUES (${sqlEscape(name)}, (SELECT id FROM DomainApp WHERE name = ${sqlEscape(application || 'metamodel')}));\n`;
    }
  }
  console.log(`  Found ${pageUrls.length} pages`);
  sql += '\n';

  console.log('🎨 Extracting PageElement...');
  const elemUrls = await getListItems('http://localhost:8080/metamodel/managePageElement');
  for (const url of elemUrls) {
    await page.goto(url);
    await page.waitForLoadState('networkidle');
    const elementType = await getFieldValue('Element Type:');
    const content = await getFieldValue('Content:');
    const orderIndex = await getFieldValue('Order Index:');
    const pageName = await getFieldValue('Page:');
    if (elementType && pageName) {
      sql += `INSERT INTO PageElement (elementType, content, orderIndex, page_id) VALUES (${sqlEscape(elementType)}, ${sqlEscape(content || '')}, ${orderIndex || 0}, (SELECT id FROM Page WHERE name = ${sqlEscape(pageName)}));\n`;
    }
  }
  console.log(`  Found ${elemUrls.length} page elements`);
  sql += '\n';

  sql += `COMMIT;

-- Verify counts
SELECT 'DomainApp' as table_name, COUNT(*) as count FROM DomainApp
UNION ALL SELECT 'DomainEntity', COUNT(*) FROM DomainEntity
UNION ALL SELECT 'EntityProperty', COUNT(*) FROM EntityProperty
UNION ALL SELECT 'Relationship', COUNT(*) FROM Relationship
UNION ALL SELECT 'ValidationRule', COUNT(*) FROM ValidationRule
UNION ALL SELECT 'DerivedProperty', COUNT(*) FROM DerivedProperty
UNION ALL SELECT 'EntityFunction', COUNT(*) FROM EntityFunction
UNION ALL SELECT 'Page', COUNT(*) FROM Page
UNION ALL SELECT 'PageElement', COUNT(*) FROM PageElement;

-- 🐍 Ouroboros complete! The snake has captured its own tail in SQL form.
`;

  const outputPath = './ouroboros-export.sql';
  await writeFile(outputPath, sql, 'utf-8');

  console.log(`\n✅ Export complete!`);
  console.log(`📁 File: ${outputPath}`);
  console.log(`\n🐘 To load into PostgreSQL:`);
  console.log(`   psql -h localhost -p 5432 -U webdsl -d webdsl -f ${outputPath}`);
  console.log(`\n🔍 Then browse with SQL:`);
  console.log(`   SELECT * FROM DomainEntity;`);
  console.log(`   SELECT e.name, p.name as property, p.propertyType`);
  console.log(`     FROM DomainEntity e`);
  console.log(`     JOIN EntityProperty p ON p.entity_id = e.id;`);

  await browser.close();
})();
