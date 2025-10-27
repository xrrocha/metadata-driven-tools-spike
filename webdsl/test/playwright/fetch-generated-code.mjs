import puppeteer from 'puppeteer';

const browser = await puppeteer.launch({ headless: true });
const page = await browser.newPage();

// Navigate to code generator
await page.goto('http://localhost:8080/metamodel/codeGenerator', { waitUntil: 'networkidle0' });

// Click on view code link
const viewCodeLink = await page.$('a[href*="viewGeneratedCode"]');
if (viewCodeLink) {
  await viewCodeLink.click();
  await page.waitForSelector('pre', { timeout: 5000 });
  
  // Extract code from <pre> tag
  const code = await page.$eval('pre', el => el.textContent);
  console.log(code);
} else {
  console.error('No generated code link found');
}

await browser.close();
