import { defineConfig, devices } from '@playwright/test';

/**
 * WebDSL Ouroboros E2E Test Configuration
 *
 * Tests the metamodel's ability to achieve iterative closure (v1 → v2 → v3).
 */
export default defineConfig({
  testDir: './tests',

  /* Run tests in files in parallel */
  fullyParallel: false, // Sequential for now - metamodel state dependent

  /* Fail the build on CI if you accidentally left test.only in the source code. */
  forbidOnly: !!process.env.CI,

  /* Retry on CI only */
  retries: process.env.CI ? 2 : 0,

  /* Single worker - metamodel tests share database state */
  workers: 1,

  /* Reporter to use */
  reporter: [
    ['html'],
    ['list'],
    ['json', { outputFile: 'test-results/results.json' }]
  ],

  /* Shared settings for all the projects below */
  use: {
    /* Base URL for the metamodel app */
    baseURL: 'http://localhost:8080',

    /* Collect trace on failure for debugging */
    trace: 'on-first-retry',

    /* Screenshot on failure */
    screenshot: 'only-on-failure',

    /* Video on failure */
    video: 'retain-on-failure',

    /* Timeout for each action (click, fill, etc.) */
    actionTimeout: 10000,
  },

  /* Configure projects for major browsers */
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
    // Uncomment to test on other browsers:
    // {
    //   name: 'firefox',
    //   use: { ...devices['Desktop Firefox'] },
    // },
    // {
    //   name: 'webkit',
    //   use: { ...devices['Desktop Safari'] },
    // },
  ],

  /* Global timeout for the entire test suite */
  globalTimeout: 60000 * 5, // 5 minutes

  /* Timeout for each test */
  timeout: 60000, // 1 minute per test

  /* Folder for test artifacts such as screenshots, videos, traces, etc. */
  outputDir: 'test-results/',

  /* Web Server configuration - WebDSL app must be running */
  // Note: WebDSL app should be started manually before running tests
  // Future: Could add webServer config to auto-start, but WebDSL compilation
  // is slow, so manual start recommended
});
