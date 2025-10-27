import { chromium } from '@playwright/test';

(async () => {
  const browser = await chromium.launch({ headless: false });
  const page = await browser.newPage();
  
  // Create a test application
  await page.goto('http://localhost:8080/metamodel/createDomainApp');
  await page.getByLabel('Name:').fill('TestApp');
  await page.getByRole('button', { name: 'Save' }).click();
  await page.waitForLoadState('networkidle');
  
  // Create a Person entity
  await page.goto('http://localhost:8080/metamodel/createDomainEntity');
  await page.getByLabel('Name:').fill('Person');
  await page.getByLabel('Application:').selectOption({ label: 'TestApp' });
  await page.getByRole('button', { name: 'Save' }).click();
  await page.waitForLoadState('networkidle');
  
  // Add properties to Person
  await page.goto('http://localhost:8080/metamodel/createEntityProperty');
  await page.getByLabel('Name:').fill('firstName');
  await page.getByLabel('Property Type:').fill('String');
  await page.getByLabel('Entity:').selectOption({ label: 'Person' });
  await page.getByRole('button', { name: 'Save' }).click();
  await page.waitForLoadState('networkidle');
  
  await page.goto('http://localhost:8080/metamodel/createEntityProperty');
  await page.getByLabel('Name:').fill('lastName');
  await page.getByLabel('Property Type:').fill('String');
  await page.getByLabel('Entity:').selectOption({ label: 'Person' });
  await page.getByRole('button', { name: 'Save' }).click();
  await page.waitForLoadState('networkidle');
  
  await page.goto('http://localhost:8080/metamodel/createEntityProperty');
  await page.getByLabel('Name:').fill('age');
  await page.getByLabel('Property Type:').fill('Int');
  await page.getByLabel('Entity:').selectOption({ label: 'Person' });
  await page.getByRole('button', { name: 'Save' }).click();
  await page.waitForLoadState('networkidle');
  
  // Add a validation rule
  await page.goto('http://localhost:8080/metamodel/createValidationRule');
  await page.getByLabel('Name:').fill('adultCheck');
  await page.getByLabel('Expression:').fill('age >= 18');
  await page.getByLabel('Message:').fill('Must be 18 or older');
  await page.getByLabel('Entity:').selectOption({ label: 'Person' });
  await page.getByRole('button', { name: 'Save' }).click();
  await page.waitForLoadState('networkidle');
  
  // Add a derived property
  await page.goto('http://localhost:8080/metamodel/createDerivedProperty');
  await page.getByLabel('Name:').fill('fullName');
  await page.getByLabel('Property Type:').fill('String');
  await page.getByLabel('Expression:').fill('firstName + " " + lastName');
  await page.getByLabel('Entity:').selectOption({ label: 'Person' });
  await page.getByRole('button', { name: 'Save' }).click();
  await page.waitForLoadState('networkidle');
  
  // Add an entity function
  await page.goto('http://localhost:8080/metamodel/createEntityFunction');
  await page.getByLabel('Name:').fill('greet');
  await page.getByLabel('Return Type:').fill('String');
  await page.getByLabel('Body:').fill('return "Hello, " + fullName;');
  await page.getByLabel('Entity:').selectOption({ label: 'Person' });
  await page.getByRole('button', { name: 'Save' }).click();
  await page.waitForLoadState('networkidle');
  
  console.log('Phase 2 test data created successfully!');
  
  await browser.close();
})();
