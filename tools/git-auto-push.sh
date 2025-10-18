#!/usr/bin/env bash
set -euo pipefail
# Simple autosave script that creates a timestamped branch, commits local changes
# and pushes to the configured origin remote.
#
# Usage: tools/git-auto-push.sh [--create-pr]
# Requirements:
#  - This repository must already have a remote named 'origin'.
#  - You must have push access (SSH key or credential helper configured, or GitHub CLI auth).

CREATE_PR=0
if [ "${1-}" = "--create-pr" ]; then CREATE_PR=1; fi

if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  echo "Not inside a git repository. Run this from a working clone." >&2
  exit 1
fi

ROOT=$(git rev-parse --show-toplevel)
cd "$ROOT"

if ! git remote get-url origin >/dev/null 2>&1; then
  echo "No 'origin' remote found. Add a remote before using this script." >&2
  exit 1
fi

if [ -n "$(git status --porcelain)" ]; then
  BRANCH="autosave/$(date -u +%Y%m%d-%H%M%SZ)"
  echo "Creating branch $BRANCH and committing changes..."
  git fetch origin >/dev/null 2>&1 || true
  git checkout -b "$BRANCH"
  # Respect .gitignore; add everything staged
  git add -A
  git commit -m "Auto-save: $BRANCH"
  echo "Pushing $BRANCH to origin..."
  git push -u origin "$BRANCH"

  if [ "$CREATE_PR" -eq 1 ] && command -v gh >/dev/null 2>&1; then
    if gh auth status >/dev/null 2>&1; then
      echo "Creating pull request using gh..."
      # Create a PR against default branch (main) — if your default branch differs, update --base
      gh pr create --base main --head "$BRANCH" --title "Auto-save: $BRANCH" --body "Automated backup commit"
    else
      echo "gh CLI is installed but not authenticated. Run 'gh auth login' first to create a PR." >&2
    fi
  fi
  echo "Autosave complete: $BRANCH"
else
  echo "No local changes to commit. Nothing to do."
fi
