#!/usr/bin/env bash
set -euo pipefail
x=hello
echo "x=$x"
echo "TMP=$(mktemp -d)"
echo "done"
