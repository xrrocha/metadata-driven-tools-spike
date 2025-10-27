#!/bin/bash

# Generate and Run App from Metamodel
# Usage: ./generate-and-run.sh <app-name>

APP_NAME="${1:-scott}"
BASE_DIR="/Users/ricardo/Workspace/webdsl-spike"
METAMODEL_URL="http://localhost:8080/metamodel"

echo "=== Generating $APP_NAME from metamodel ==="

# Step 1: Trigger code generation
echo "Triggering code generation..."
GENERATOR_PAGE=$(curl -s "$METAMODEL_URL/codeGenerator")

# Extract the application dropdown and select the app
echo "Generating code for application: $APP_NAME"
GENERATE_RESPONSE=$(curl -s -X POST \
  -d "codeGenerator_generateCodeForm=1" \
  "$METAMODEL_URL/codeGenerator")

# Step 2: Extract the generated code UUID from the response
CODE_UUID=$(echo "$GENERATE_RESPONSE" | grep -oE 'viewGeneratedCode/[^"]+' | head -1 | cut -d/ -f2)

if [ -z "$CODE_UUID" ]; then
  echo "ERROR: Could not find generated code UUID"
  echo "Make sure you've bootstrapped the metamodel first!"
  exit 1
fi

echo "Generated code UUID: $CODE_UUID"

# Step 3: Fetch the generated code
echo "Fetching generated code..."
curl -s "$METAMODEL_URL/viewGeneratedCode/$CODE_UUID" > /tmp/generated-page.html

# Step 4: Extract code from <pre> tag and decode HTML entities
awk '/<pre>/{flag=1; sub(/.*<pre>/, "")} /<\/pre>/{sub(/<\/pre>.*/, ""); print; flag=0} flag' \
  /tmp/generated-page.html \
  | python3 -c "import sys, html; print(html.unescape(sys.stdin.read()), end='')" \
  > "/tmp/$APP_NAME.app"

# Check if we got valid code
if [ ! -s "/tmp/$APP_NAME.app" ]; then
  echo "ERROR: Generated code is empty"
  exit 1
fi

echo "Generated code size: $(wc -l /tmp/$APP_NAME.app | awk '{print $1}') lines"

# Step 5: Create application directory
APP_DIR="$BASE_DIR/$APP_NAME"
mkdir -p "$APP_DIR"

# Step 6: Copy generated code
cp "/tmp/$APP_NAME.app" "$APP_DIR/$APP_NAME.app"
echo "Saved to: $APP_DIR/$APP_NAME.app"

# Step 7: Clean and compile
echo ""
echo "=== Compiling $APP_NAME ==="
cd "$APP_DIR"
rm -rf .servletapp

# Step 8: Run the application
echo ""
echo "=== Running $APP_NAME ==="
../webdsl/bin/webdsl run "$APP_NAME"
