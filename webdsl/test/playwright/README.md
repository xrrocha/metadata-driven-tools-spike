# WebDSL Ouroboros E2E Tests

End-to-end tests for verifying the metamodel's Ouroboros closure property using Playwright.

## Purpose

These tests verify that the WebDSL metamodel can:
1. **Bootstrap itself** - Create self-describing data (23 records)
2. **Generate code** - Read from database and produce valid WebDSL source
3. **Achieve convergence** - v1 (handwritten) ≈ v2 (generated) ≈ v3 (generated from v2)

## Prerequisites

1. **Node.js** - Version 18 or higher
2. **WebDSL app running** - Metamodel app must be running on `http://localhost:8080/metamodel`

## Installation

```bash
cd test/playwright
npm install
npx playwright install chromium  # Install browser
```

## Running Tests

### Start WebDSL App First

```bash
# In terminal 1 (from webdsl-spike root)
cd metamodel
export PATH="$HOME/.sdkman/candidates/ant/current/bin:$PATH"
../webdsl/bin/webdsl run metamodel

# Wait for "Server startup complete" message
# App available at: http://localhost:8080/metamodel/
```

### Run Tests

```bash
# In terminal 2 (from test/playwright directory)
cd test/playwright

# Run all tests (headless)
npm test

# Run with browser visible (headed mode)
npm run test:headed

# Run with Playwright UI (best for development)
npm run test:ui

# Run just the convergence test
npm run test:convergence

# Debug mode (step through tests)
npm run test:debug
```

## Test Suite Overview

### `ouroboros-convergence.spec.ts`

Core test suite verifying Phase 1 (Structural) implementation:

**Tests:**
1. ✅ **Root page display** - Verifies app is running and navigation links exist
2. ✅ **Bootstrap self-description** - Creates 23 records describing metamodel
3. ✅ **Verify properties** - Checks all entity properties were created
4. ✅ **Verify relationships** - Checks all entity relationships were created
5. ✅ **Generate code** - Produces metamodel-v2.app and saves to fixtures
6. ✅ **Structural convergence** - Verifies v2 ≈ v1 (same entities, properties, relationships)
7. ✅ **CRUD validation** - Tests forms work (create entities, properties)
8. ✅ **Error handling** - Validates form error handling

## Test Artifacts

### Generated Files

- `fixtures/generated-metamodel-*.app` - Generated code snapshots (timestamped)
- `test-results/` - Screenshots, videos, traces (only on failure)
- `playwright-report/` - HTML test report

### Viewing Results

```bash
# After running tests, view HTML report
npm run show-report
```

## Test Results Interpretation

### Success Criteria

**All tests pass** = Ouroboros closure achieved ✅

Specifically:
- 4 entities generated (DomainApp, DomainEntity, EntityProperty, Relationship)
- All properties present (name fields, types)
- All relationships correct (inverse, references, collections)
- `derive CRUD` statements generated
- Root page structure correct

### Failure Scenarios

**Screenshots/videos captured on failure** in `test-results/`

Common issues:
- WebDSL app not running → `ECONNREFUSED`
- Database empty → Bootstrap test may fail
- Code generation broken → Convergence test fails

## Project Structure

```
test/playwright/
├── package.json              # Dependencies
├── tsconfig.json             # TypeScript config
├── playwright.config.ts      # Playwright settings
├── README.md                 # This file
├── tests/
│   └── ouroboros-convergence.spec.ts  # Main test suite
├── fixtures/                 # Generated code snapshots
│   └── generated-metamodel-*.app
├── test-results/             # Screenshots, videos, traces (gitignored)
└── playwright-report/        # HTML report (gitignored)
```

## Development Workflow

### Adding New Tests

1. Create test file in `tests/` directory
2. Import test utilities: `import { test, expect } from '@playwright/test';`
3. Use `test.describe()` for grouping, `test()` for individual tests
4. Run with `npm run test:ui` for live development

### Debugging Failed Tests

```bash
# Run in debug mode (opens inspector)
npm run test:debug

# Or run in headed mode to see browser
npm run test:headed
```

### CI/CD Integration

Tests are designed to run in CI:
- Retries configured (2 retries on CI)
- JSON output for reporting
- Screenshots/videos on failure

## Configuration

### Browser Selection

Edit `playwright.config.ts` to enable additional browsers:

```typescript
projects: [
  { name: 'chromium', use: { ...devices['Desktop Chrome'] } },
  { name: 'firefox', use: { ...devices['Desktop Firefox'] } },  // Uncomment
  { name: 'webkit', use: { ...devices['Desktop Safari'] } },    // Uncomment
],
```

### Timeouts

- **Per action:** 10s (clicks, fills, etc.)
- **Per test:** 60s
- **Global suite:** 5 minutes

Adjust in `playwright.config.ts` if needed.

## Future Tests

### Phase 2: Behavioral
- Test validation rule generation
- Test derived property generation
- Test entity function generation

### Phase 3: UI/Pages
- Test page generation from metamodel
- Test template generation
- Test form generation

### Phase 4+: Advanced
- Access control rules
- Ajax operations
- Services, email templates

## Troubleshooting

### Tests timing out

**Issue:** WebDSL app slow to respond

**Solution:** Increase `actionTimeout` in `playwright.config.ts`

### Bootstrap data already exists

**Issue:** Bootstrap test fails because data exists

**Solution:**
- Clean database (delete `.servletapp/` and restart)
- Or modify test to handle existing data (TODO: add cleanup)

### Port 8080 in use

**Issue:** WebDSL app can't start

**Solution:**
```bash
# Find process using port 8080
lsof -i :8080

# Kill it
kill -9 <PID>
```

## Resources

- [Playwright Documentation](https://playwright.dev)
- [Playwright Best Practices](https://playwright.dev/docs/best-practices)
- [WebDSL Reference](https://webdsl.org/reference/)

## Reflections

### Why TypeScript?

Choice made based on:
1. Playwright's primary language (best docs/examples)
2. Type safety for testing a type-based metamodel
3. Quick setup and good IDE support
4. Author familiarity: C-family syntax + strong types + practical ecosystem

Alternative considered: Python (more readable, good Playwright support)

Decision: TypeScript chosen for this project, aligning with team preferences and ecosystem fit.

---

**Tests validate that the snake can bite its tail.** 🐍

Each test run proves Ouroboros closure - the metamodel describing itself and generating code identical to its handwritten source.
