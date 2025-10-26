import { chromium } from '@playwright/test';

(async () => {
  const browser = await chromium.launch();
  const page = await browser.newPage();
  
  // Navigate to bootstrap page
  await page.goto('http://localhost:8080/metamodel/bootstrapPage');
  
  // Click bootstrap button
  await page.getByRole('button', { name: 'Create Metamodel Self-Description' }).click();
  
  // Wait for redirect
  await page.waitForLoadState('networkidle');
  
  console.log('Bootstrap completed!');
  console.log('Page URL:', page.url());
  
  await browser.close();
})();
