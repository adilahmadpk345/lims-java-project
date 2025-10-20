#!/usr/bin/env bash
set -euo pipefail

# Usage: fetch_run_logs.sh <run-id>
# If run-id is omitted, fetch the latest run for the current branch.

RUN_ID=${1:-}
OUT_DIR=${2:-run_logs}
RETRIES=${3:-8}
SLEEP=${4:-6}

mkdir -p "$OUT_DIR"

if [ -z "$RUN_ID" ]; then
  echo "No run id provided, finding latest ci.yml run for current branch"
  RUN_ID=$(gh run list --workflow ci.yml --limit 1 --json databaseId -q '.[0].databaseId')
fi

if [ -z "$RUN_ID" ] || [ "$RUN_ID" = "null" ]; then
  echo "Could not determine run id" >&2
  exit 2
fi

OUT_FILE="$OUT_DIR/run-${RUN_ID}.log"
echo "Fetching logs for run $RUN_ID -> $OUT_FILE"

for i in $(seq 1 $RETRIES); do
  echo "Attempt $i/$RETRIES: gh run view $RUN_ID --log"
  if gh run view "$RUN_ID" --log > "$OUT_FILE" 2>&1; then
    echo "Saved logs to $OUT_FILE"
    exit 0
  fi
  echo "Log not available yet, sleeping $SLEEP seconds"
  sleep $SLEEP
done

echo "Failed to fetch logs after $RETRIES attempts" >&2
exit 3
