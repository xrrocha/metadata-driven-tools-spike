import { chromium } from '@playwright/test';

(async () => {
  const browser = await chromium.launch();
  const page = await browser.newPage();
  
  await page.goto('http://localhost:8080/metamodel/bootstrapPage');
  await page.getByRole('button', { name: 'Create Metamodel Self-Description' }).click();
  await page.waitForLoadState('networkidle');
  
  console.log('Phase 3 bootstrap completed!');
  
  await browser.close();
})();
