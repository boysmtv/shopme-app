#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_ROOT="${BACKEND_ROOT:-$ROOT_DIR/../backend-mtv-shopme}"
OPENAPI_URL="${OPENAPI_URL:-http://127.0.0.1:8080/v3/api-docs}"
SEED_DEMO_DATA="${SEED_DEMO_DATA:-1}"

if [[ ! -d "$BACKEND_ROOT" ]]; then
  echo "Backend root not found: $BACKEND_ROOT" >&2
  exit 1
fi

if [[ "$SEED_DEMO_DATA" == "1" ]]; then
  "$BACKEND_ROOT/seed-demo-data.sh"
fi

python3 "$ROOT_DIR/scripts/verify_backend_contract.py" \
  --android-root "$ROOT_DIR" \
  --backend-root "$BACKEND_ROOT" \
  --openapi-url "$OPENAPI_URL"

"$ROOT_DIR/gradlew" assembleDebug testDebugUnitTest
