#!/usr/bin/env bash
# Lightweight helper to build and run the lims-web jar locally with sensible defaults
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAR_PATH="$ROOT_DIR/lims-web/target/lims-web-0.0.1-SNAPSHOT.jar"
LOG_DIR="$ROOT_DIR/logs"
LOG_FILE="$LOG_DIR/lims-web.log"

show_help() {
  cat <<EOF
Usage: $(basename "$0") [--h2] [--help]

Options:
  --h2      Run with an H2 in-memory datasource (recommended for local dev)
  --help    Show this help

Environment variables honored (optional):
  JAVA_HOME                    Use this java executable if set
  SPRING_DATASOURCE_URL        JDBC URL to use (defaults to H2 when --h2)
  SPRING_DATASOURCE_USERNAME   DB username
  SPRING_DATASOURCE_PASSWORD   DB password
  SPRING_CLOUD_GCP_SQL_ENABLED Disable Cloud SQL auto-config when false

Examples:
  ./run-local.sh --h2
  SPRING_CLOUD_GCP_SQL_ENABLED=false ./run-local.sh --h2
EOF
}

if [[ ${1:-} == "--help" || ${1:-} == "-h" ]]; then
  show_help
  exit 0
fi

USE_H2=false
if [[ ${1:-} == "--h2" ]]; then
  USE_H2=true
fi

# Ensure Java 17 is available (best-effort check)
JAVA_CMD="${JAVA_HOME:-}"/bin/java
if [[ -x "$JAVA_CMD" ]]; then
  JAVA_VERSION=$($JAVA_CMD -version 2>&1 | head -n 1)
else
  JAVA_CMD=$(command -v java || true)
fi

if [[ -z "$JAVA_CMD" ]]; then
  echo "No java found. Please install Java 17 and set JAVA_HOME." >&2
  exit 1
fi

echo "Using java: $($JAVA_CMD -version 2>&1 | head -n 1)"

# Build if jar missing
if [[ ! -f "$JAR_PATH" ]]; then
  echo "Jar not found at $JAR_PATH — building lims-web (skip tests)..."
  (cd "$ROOT_DIR" && mvn -pl lims-web -am -DskipTests package)
fi

mkdir -p "$LOG_DIR"

ENV_VARS=()
if [[ "$USE_H2" == "true" ]]; then
  ENV_VARS+=("SPRING_DATASOURCE_URL=jdbc:h2:mem:limsdb;DB_CLOSE_DELAY=-1")
  ENV_VARS+=("SPRING_DATASOURCE_USERNAME=sa")
  ENV_VARS+=("SPRING_DATASOURCE_PASSWORD=")
  ENV_VARS+=("SPRING_CLOUD_GCP_SQL_ENABLED=false")
  # For H2 local runs enable automatic schema update and globally quoted
  # identifiers so reserved names (for example `user`) are quoted and won't
  # cause syntax errors. Change to 'none' if you prefer to manage schema
  # outside of this script.
  ENV_VARS+=("SPRING_JPA_HIBERNATE_DDL_AUTO=update")
  ENV_VARS+=("SPRING_JPA_PROPERTIES_HIBERNATE_GLOBALLY_QUOTED_IDENTIFIERS=true")
fi

echo "Starting lims-web jar: $JAR_PATH"
echo "Logs -> $LOG_FILE"

CMD=("$JAVA_CMD" -jar "$JAR_PATH")

# Start in background, write pid
env "${ENV_VARS[@]}" nohup "${CMD[@]}" > "$LOG_FILE" 2>&1 &
PID=$!
echo $PID > "$LOG_DIR/lims-web.pid"
echo "Started (PID=$PID) — tail -f $LOG_FILE to follow logs"

exit 0
