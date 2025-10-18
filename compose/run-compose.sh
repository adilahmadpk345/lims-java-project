#!/usr/bin/env bash
set -euo pipefail
ROOT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)
cd "$ROOT_DIR"

echo "Building lims-web jar (skip tests)..."
mvn -pl lims-web -am -DskipTests package

echo "Bringing up docker-compose services..."
docker compose up --build
