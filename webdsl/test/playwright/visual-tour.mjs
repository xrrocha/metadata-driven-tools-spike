import { chromium } from '@playwright/test';
import { mkdir } from 'fs/promises';

(async () => {
  console.log('🐍 OUROBOROS VISUAL TOUR 🐍');
  console.log('The Snake That Models Itself\n');

  // Create screenshots directory
  await mkdir('./screenshots', { recursive: true });

  const browser = await chromium.launch({
    headless: false,
    slowMo: 1000  // 1 second delay between actions
  });
  const page = await browser.newPage();

  let step = 0;
  const screenshot = async (name) => {
    step++;
    await page.screenshot({ path: `./screenshots/${step.toString().padStart(2, '0')}-${name}.png` });
  };

  // ========================================
  // ACT 1: THE ROOT - THREE PHASES UNITED
  // ========================================
  console.log('\n📖 ACT 1: THE ROOT PAGE');
  console.log('Showing the three phases of self-description...\n');

  await page.goto('http://localhost:8080/metamodel/');
  await screenshot('root-page');

  console.log('✅ Phase 1: STRUCTURAL (entities, properties, relationships)');
  await page.getByRole('link', { name: 'Manage Applications' }).hover();
  await page.waitForTimeout(1500);
  await page.getByRole('link', { name: 'Manage Entities' }).hover();
  await page.waitForTimeout(1500);
  await page.getByRole('link', { name: 'Manage Properties' }).hover();
  await page.waitForTimeout(1500);
  await page.getByRole('link', { name: 'Manage Relationships' }).hover();
  await page.waitForTimeout(1500);
  await screenshot('phase1-structural');

  console.log('\n✅ Phase 2: BEHAVIORAL (validation, derived properties, functions)');
  await page.getByRole('link', { name: 'Manage ValidationRules' }).hover();
  await page.waitForTimeout(1500);
  await page.getByRole('link', { name: 'Manage DerivedPropertys' }).hover();
  await page.waitForTimeout(1500);
  await page.getByRole('link', { name: 'Manage EntityFunctions' }).hover();
  await page.waitForTimeout(1500);
  await screenshot('phase2-behavioral');

  console.log('\n✅ Phase 3: UI (pages, page elements)');
  await page.getByRole('link', { name: 'Manage Pages' }).hover();
  await page.waitForTimeout(1500);
  await page.getByRole('link', { name: 'Manage PageElements' }).hover();
  await page.waitForTimeout(1500);
  await screenshot('phase3-ui');

  // ========================================
  // ACT 2: THE BOOTSTRAP - CREATION MOMENT
  // ========================================
  console.log('\n\n🌱 ACT 2: THE BOOTSTRAP');
  console.log('Where the snake first bites its tail...\n');

  await page.getByRole('link', { name: 'Bootstrap' }).click();
  await page.waitForLoadState('networkidle');
  await screenshot('bootstrap-page');

  console.log('Bootstrap status: Data already exists!');
  console.log('The metamodel already describes itself.\n');
  await page.waitForTimeout(2000);

  // ========================================
  // ACT 3: STRUCTURAL ENTITIES (PHASE 1)
  // ========================================
  console.log('\n📐 ACT 3: STRUCTURAL ENTITIES');
  console.log('The foundation of self-description...\n');

  // DomainApp
  console.log('Entity 1/9: DomainApp - The container');
  await page.goto('http://localhost:8080/metamodel/manageDomainApp');
  await page.waitForLoadState('networkidle');
  await screenshot('entity-domainapp');
  await page.getByRole('link', { name: 'metamodel' }).first().click();
  await page.waitForLoadState('networkidle');
  await screenshot('entity-domainapp-detail');
  console.log('  → Has 9 entities modeling itself');
  console.log('  → Has 1 page modeling the root UI\n');
  await page.waitForTimeout(2000);

  // DomainEntity
  console.log('Entity 2/9: DomainEntity - The meta-entity');
  await page.goto('http://localhost:8080/metamodel/manageDomainEntity');
  await page.waitForLoadState('networkidle');
  await screenshot('entity-domainentity');
  console.log('  → 9 entities total');
  console.log('  → Each models a WebDSL construct\n');
  await page.waitForTimeout(2000);

  // Click into DomainEntity itself to show recursion
  await page.getByRole('link', { name: 'DomainEntity' }).first().click();
  await page.waitForLoadState('networkidle');
  await screenshot('entity-domainentity-detail');
  console.log('  🐍 RECURSION MOMENT: DomainEntity describes itself!');
  console.log('  → Has properties: name, application');
  console.log('  → Has relationships: properties, relationships, validationRules, derivedProperties, functions\n');
  await page.waitForTimeout(3000);

  // EntityProperty
  console.log('Entity 3/9: EntityProperty - Property metadata');
  await page.goto('http://localhost:8080/metamodel/manageEntityProperty');
  await page.waitForLoadState('networkidle');
  await screenshot('entity-property');
  console.log('  → 20+ properties describing all entities');
  console.log('  → Each property knows its type and owner\n');
  await page.waitForTimeout(2000);

  // Relationship
  console.log('Entity 4/9: Relationship - Connection metadata');
  await page.goto('http://localhost:8080/metamodel/manageRelationship');
  await page.waitForLoadState('networkidle');
  await screenshot('entity-relationship');
  console.log('  → 20+ relationships connecting entities');
  console.log('  → Bidirectional with inverses\n');
  await page.waitForTimeout(2000);

  // ========================================
  // ACT 4: BEHAVIORAL ENTITIES (PHASE 2)
  // ========================================
  console.log('\n⚡ ACT 4: BEHAVIORAL ENTITIES');
  console.log('Where structure gains behavior...\n');

  // ValidationRule
  console.log('Entity 5/9: ValidationRule - Constraints as data');
  await page.goto('http://localhost:8080/metamodel/manageValidationRule');
  await page.waitForLoadState('networkidle');
  await screenshot('entity-validation');
  console.log('  → Validation logic stored as strings');
  console.log('  → "age >= 18" becomes executable code\n');
  await page.waitForTimeout(2000);

  // DerivedProperty
  console.log('Entity 6/9: DerivedProperty - Computed properties as data');
  await page.goto('http://localhost:8080/metamodel/manageDerivedProperty');
  await page.waitForLoadState('networkidle');
  await screenshot('entity-derived');
  console.log('  → Expressions stored as strings');
  console.log('  → "firstName + \\" \\" + lastName" becomes code\n');
  await page.waitForTimeout(2000);

  // EntityFunction
  console.log('Entity 7/9: EntityFunction - Functions as data');
  await page.goto('http://localhost:8080/metamodel/manageEntityFunction');
  await page.waitForLoadState('networkidle');
  await screenshot('entity-function');
  console.log('  → Function bodies stored as strings');
  console.log('  → Generated as extend entity blocks\n');
  await page.waitForTimeout(2000);

  // ========================================
  // ACT 5: UI ENTITIES (PHASE 3)
  // ========================================
  console.log('\n🎨 ACT 5: UI ENTITIES');
  console.log('Where the snake paints itself...\n');

  // Page
  console.log('Entity 8/9: Page - Page definitions as data');
  await page.goto('http://localhost:8080/metamodel/managePage');
  await page.waitForLoadState('networkidle');
  await screenshot('entity-page');
  console.log('  → Pages can model UI structure');
  console.log('  → Each page has elements defining content\n');
  await page.waitForTimeout(2000);

  // PageElement
  console.log('Entity 9/9: PageElement - UI elements as data');
  await page.goto('http://localhost:8080/metamodel/managePageElement');
  await page.waitForLoadState('networkidle');
  await screenshot('entity-pageelement');
  console.log('  → Title, header, paragraph, navigate');
  console.log('  → Each element ordered by orderIndex\n');
  await page.waitForTimeout(2000);

  // ========================================
  // ACT 6: THE GENERATOR - CODE FROM DATA
  // ========================================
  console.log('\n🔧 ACT 6: THE CODE GENERATOR');
  console.log('Where data becomes executable code...\n');

  await page.goto('http://localhost:8080/metamodel/codeGenerator');
  await page.waitForLoadState('networkidle');
  await screenshot('generator-page');

  console.log('Viewing generated code...');
  await page.getByRole('link', { name: 'View Code' }).click();
  await page.waitForLoadState('networkidle');
  await screenshot('generator-result');

  console.log('\n✨ Generated code includes:');
  console.log('  → 9 entity definitions');
  console.log('  → 20+ properties');
  console.log('  → 20+ relationships');
  console.log('  → Validation rules');
  console.log('  → Derived properties');
  console.log('  → Entity functions');
  console.log('  → Page definitions');
  console.log('  → Page elements\n');

  await page.waitForTimeout(3000);

  // Scroll through the generated code slowly
  console.log('Scrolling through generated code...');
  for (let i = 0; i < 5; i++) {
    await page.keyboard.press('PageDown');
    await page.waitForTimeout(1000);
  }
  await screenshot('generator-scrolled');

  // ========================================
  // FINALE: THE OUROBOROS COMPLETE
  // ========================================
  console.log('\n\n🐍 FINALE: THE OUROBOROS COMPLETE 🐍\n');
  console.log('═══════════════════════════════════════════════════════');
  console.log('  A system that models:');
  console.log('    ✅ Its own data structures (Phase 1)');
  console.log('    ✅ Its own behavior (Phase 2)');
  console.log('    ✅ Its own user interface (Phase 3)');
  console.log('');
  console.log('  9 entities, 40+ records, infinite recursion');
  console.log('  Built in ONE DAY from zero WebDSL knowledge');
  console.log('  "300 years early" - The future arrived ahead of schedule');
  console.log('═══════════════════════════════════════════════════════\n');

  await page.goto('http://localhost:8080/metamodel/');
  await screenshot('finale-root');

  await page.waitForTimeout(5000);

  console.log('🎬 Tour complete! Screenshots saved to ./screenshots/\n');
  console.log('The snake has shown you its tail. 🐍✨\n');

  await browser.close();
})();
