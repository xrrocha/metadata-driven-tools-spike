import { chromium } from '@playwright/test';

/**
 * Quick demo: Create simple app and view generated code
 */

(async () => {
  console.log('🐍 OUROBOROS CODE GENERATION DEMO');
  console.log('Creating a simple app and watching the code generator...\n');

  const browser = await chromium.launch({ headless: false, slowMo: 500 });
  const page = await browser.newPage();

  // Step 1: Create a DomainApp
  console.log('📦 Step 1: Creating DomainApp "library"...');
  await page.goto('http://localhost:8080/metamodel/createDomainApp');
  await page.fill('.inputname', 'library');
  await page.click('button:has-text("Save")');
  await page.waitForLoadState('networkidle');
  console.log('   ✓ DomainApp created');

  // Step 2: Create an entity "Book"
  console.log('📚 Step 2: Creating Entity "Book"...');
  await page.goto('http://localhost:8080/metamodel/createDomainEntity');
  await page.fill('.inputname', 'Book');
  await page.selectOption('.inputapplication', { label: 'library' });
  await page.click('button:has-text("Save")');
  await page.waitForLoadState('networkidle');
  console.log('   ✓ Entity "Book" created');

  // Step 3: Add property "title"
  console.log('📝 Step 3: Adding property "title"...');
  await page.goto('http://localhost:8080/metamodel/createEntityProperty');
  await page.fill('.inputname', 'title');
  await page.fill('.inputpropertyType', 'String');
  await page.selectOption('.inputentity', { label: 'Book' });
  await page.click('button:has-text("Save")');
  await page.waitForLoadState('networkidle');
  console.log('   ✓ Property "title" added');

  // Step 4: Add property "author"
  console.log('✍️  Step 4: Adding property "author"...');
  await page.goto('http://localhost:8080/metamodel/createEntityProperty');
  await page.fill('.inputname', 'author');
  await page.fill('.inputpropertyType', 'String');
  await page.selectOption('.inputentity', { label: 'Book' });
  await page.click('button:has-text("Save")');
  await page.waitForLoadState('networkidle');
  console.log('   ✓ Property "author" added');

  // Step 5: View generated code!
  console.log('\n🎉 Step 5: VIEWING GENERATED CODE...');
  await page.goto('http://localhost:8080/metamodel/codeGenerator');
  await page.waitForLoadState('networkidle');

  // Click "View Code" link
  await page.click('a:has-text("View Code")');
  await page.waitForLoadState('networkidle');

  console.log('\n🐍 OUROBOROS MOMENT: The metamodel has generated WebDSL code!');
  console.log('The browser is now showing the generated .app file\n');
  console.log('Press Ctrl+C when ready to close...');

  // Keep browser open
  await new Promise(() => {});
})();
