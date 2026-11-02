#!/usr/bin/env bash
set -euo pipefail

UPSTREAM="55982e0af3114a3942f6b5e289d553ff70c1ab9a"
if [[ -f jadx-core/build.gradle.kts ]]; then
  echo "JADX sources already present, skipping bootstrap."
  exit 0
fi
if [[ ! -f .dexriva/rebrand.py ]]; then
  echo "Missing DEXRIVA patch script" >&2
  exit 1
fi
tmp="$(mktemp -d)"
trap 'rm -rf "$tmp"' EXIT
git clone --branch master --depth 1 https://github.com/skylot/jadx.git "$tmp/jadx"
actual="$(git -C "$tmp/jadx" rev-parse HEAD)"
if [[ "$actual" != "$UPSTREAM" ]]; then
  echo "Upstream changed from the reviewed SHA ($UPSTREAM to $actual). Review and repin before importing." >&2
  exit 1
fi

# Preserve all first-party sources, including upstream LICENSE and NOTICE.
# Do not import upstream Actions workflows into our repository.
# Keep DEXRIVA's own README, build workflows and branding automation.
rsync -a --exclude='.git/' --exclude='.github/' --exclude='.run/' --exclude='README.md' "$tmp/jadx/" ./
python3 .dexriva/make_icons.py
python3 .dexriva/rebrand.py
python3 -m unittest discover -s tests -p 'test_*.py' -v
test -f LICENSE && test -f NOTICE && test -f jadx-core/build.gradle.kts
echo "Upstream import and DEXRIVA patches completed."
