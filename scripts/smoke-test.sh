#!/usr/bin/env bash
set -euo pipefail

HOST=${1:-localhost}
PORT=${2:-8080}
AUTH=${3:-admin:password}
BASE="http://$HOST:$PORT"

echo "Smoke test: $BASE"

echo "Waiting for server to become available (timeout ~30s)..."
for i in {1..30}; do
  if curl -sSf -u "$AUTH" "$BASE/story" -o /dev/null 2>/dev/null; then
    echo "Server is responding"
    break
  fi
  sleep 1
done

echo
echo "GET /story"
STORY_RESPONSE=$(curl -sS -u "$AUTH" --get --data-urlencode "prompt=Hello from smoke test" "$BASE/story" || true)
if [[ -z "$STORY_RESPONSE" ]]; then
  echo "  /story returned empty or failed"
else
  echo "  /story responded (truncated):"
  echo "  ${STORY_RESPONSE:0:400}"
fi

echo
echo "GET /chat"
CHAT_RESPONSE=$(curl -sS -u "$AUTH" --get --data-urlencode "sessionId=smoke-session" --data-urlencode "prompt=Ping" "$BASE/chat" || true)
if [[ -z "$CHAT_RESPONSE" ]]; then
  echo "  /chat returned empty or failed"
else
  echo "  /chat responded (truncated):"
  echo "  ${CHAT_RESPONSE:0:400}"
fi

echo
echo "Smoke test complete."
