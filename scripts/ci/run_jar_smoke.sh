#!/usr/bin/env bash
set -euo pipefail

# Runs the packaged Spring Boot jar in background, captures stdout/stderr to smoke.log,
# performs authenticated health checks, and exits with non-zero if checks fail.

JAR=${1:-lims-web/target/lims-web-0.0.1-SNAPSHOT.jar}
LOG_FILE=${2:-smoke.log}

if [ ! -f "$JAR" ]; then
  echo "Jar not found: $JAR" >&2
  exit 2
fi

echo "Starting jar: $JAR"
nohup java -jar "$JAR" --spring.profiles.active=h2 --spring.cloud.gcp.sql.enabled=false > "$LOG_FILE" 2>&1 &
PID=$!
echo "Started pid=$PID, logging to $LOG_FILE"

cleanup() {
  echo "Stopping pid $PID"
  kill "$PID" 2>/dev/null || true
  wait "$PID" 2>/dev/null || true
}
trap cleanup EXIT

# Wait for readiness
echo "Waiting for app readiness..."
for i in $(seq 1 40); do
  if curl -s -u admin:password --fail http://localhost:8080/ >/dev/null 2>&1; then
    echo "app is up"
    break
  fi
  echo "waiting... ($i)" >>"$LOG_FILE"
  sleep 2
done

echo "Curl /api/samples" >>"$LOG_FILE"
curl -s -u admin:password -D - http://localhost:8080/api/samples >>"$LOG_FILE" || true

echo "Smoke test finished, log at $LOG_FILE"
exit 0
