#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
if ! command -v javac >/dev/null 2>&1; then
  echo "Instala un JDK 17 o posterior y agrega su carpeta bin al PATH."
  exit 1
fi
mkdir -p build
javac -encoding UTF-8 --release 17 -Xlint:all -Werror -d build @fuentes.txt
echo "Compilación correcta."
