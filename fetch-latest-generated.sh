#!/bin/bash

# Fetch Latest Generated Code
# Usage: After clicking "Generate Code" in the UI, run:
#   ./fetch-latest-generated.sh <code-uuid>
#
# The UUID is in the URL after clicking "View Code"
# Example URL: http://localhost:8080/metamodel/viewGeneratedCode/abc123
# Then run: ./fetch-latest-generated.sh abc123

CODE_UUID="$1"
APP_NAME="${2:-generated}"
METAMODEL_URL="http://localhost:8080/metamodel"

if [ -z "$CODE_UUID" ]; then
  echo "Usage: $0 <code-uuid> [app-name]"
  echo ""
  echo "Steps:"
  echo "1. Visit http://localhost:8080/metamodel/codeGenerator"
  echo "2. Click 'Generate Code'"
  echo "3. Click 'View Code'"
  echo "4. Copy the UUID from the URL (e.g., /viewGeneratedCode/UUID_HERE)"
  echo "5. Run: $0 UUID_HERE scott"
  exit 1
fi

echo "=== Fetching generated code ==="
echo "UUID: $CODE_UUID"
echo "App name: $APP_NAME"

# Create directory
mkdir -p "$APP_NAME"

# Fetch and extract code
curl -s "$METAMODEL_URL/viewGeneratedCode/$CODE_UUID" > /tmp/generated-page.html

awk '/<pre>/{flag=1; sub(/.*<pre>/, "")} /<\/pre>/{sub(/<\/pre>.*/, ""); print; flag=0} flag' \
  /tmp/generated-page.html \
  | python3 -c "import sys, html; print(html.unescape(sys.stdin.read()), end='')" \
  > "$APP_NAME/$APP_NAME.app"

if [ ! -s "$APP_NAME/$APP_NAME.app" ]; then
  echo "ERROR: Generated code is empty or UUID invalid"
  exit 1
fi

echo ""
echo "✅ Downloaded to: $APP_NAME/$APP_NAME.app"
echo "📊 Size: $(wc -l $APP_NAME/$APP_NAME.app | awk '{print $1}') lines"
echo ""
echo "Preview:"
head -20 "$APP_NAME/$APP_NAME.app"
echo "..."
echo ""
echo "To compile and run:"
echo "  cd $APP_NAME"
echo "  ../webdsl/bin/webdsl run $APP_NAME"
