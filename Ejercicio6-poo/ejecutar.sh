#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
bash compilar.sh
java -Dfile.encoding=UTF-8 -cp build poo.vista.Principal "$@"
