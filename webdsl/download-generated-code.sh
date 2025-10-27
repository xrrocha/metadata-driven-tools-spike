#!/bin/bash

# Download Generated Code from Metamodel
# Usage: ./download-generated-code.sh [app-name]

APP_NAME="${1:-scott}"
METAMODEL_URL="http://localhost:8080/metamodel"

echo "=== Downloading generated code for: $APP_NAME ==="

# Trigger code generation
echo "Triggering code generation..."
GENERATE_RESPONSE=$(curl -s -X POST \
  -d "codeGenerator_generateCodeForm=1" \
  "$METAMODEL_URL/codeGenerator")

# Extract UUID
CODE_UUID=$(echo "$GENERATE_RESPONSE" | grep -oE 'viewGeneratedCode/[^"]+' | head -1 | cut -d/ -f2)

if [ -z "$CODE_UUID" ]; then
  echo "ERROR: Could not find generated code UUID"
  echo "Make sure you've bootstrapped the metamodel first!"
  exit 1
fi

echo "Generated code UUID: $CODE_UUID"

# Fetch and extract code
curl -s "$METAMODEL_URL/viewGeneratedCode/$CODE_UUID" > /tmp/generated-page.html

awk '/<pre>/{flag=1; sub(/.*<pre>/, "")} /<\/pre>/{sub(/<\/pre>.*/, ""); print; flag=0} flag' \
  /tmp/generated-page.html \
  | python3 -c "import sys, html; print(html.unescape(sys.stdin.read()), end='')" \
  > "$APP_NAME/$APP_NAME.app"

if [ ! -s "$APP_NAME/$APP_NAME.app" ]; then
  echo "ERROR: Generated code is empty"
  exit 1
fi

echo ""
echo "✅ Downloaded to: $APP_NAME/$APP_NAME.app"
echo "📊 Size: $(wc -l $APP_NAME/$APP_NAME.app | awk '{print $1}') lines"
echo ""
echo "To view:"
echo "  cat $APP_NAME/$APP_NAME.app"
echo ""
echo "To compile and run:"
echo "  cd $APP_NAME"
echo "  ../webdsl/bin/webdsl run $APP_NAME"
