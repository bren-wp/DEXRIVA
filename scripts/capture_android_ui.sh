#!/usr/bin/env bash
# Capture only real, running Android Inspector UI. No generated/mock images.
set -euo pipefail

APK="android-inspector/app/build/outputs/apk/debug/app-debug.apk"
DEST="docs/screenshots/android-inspector-home.png"
PKG="com.brendigo.dexrive.inspector"
test -s "$APK"
mkdir -p "$(dirname "$DEST")"
echo "Android boot state: $(adb shell getprop sys.boot_completed | tr -d '\r')"

installed=0
for attempt in 1 2 3 4 5 6; do
    if adb install -r "$APK"; then
        installed=1
        break
    fi
    echo "ADB APK installation attempt $attempt failed; restarting ADB before retry"
    adb reconnect || true
    sleep 12
done

if [ "$installed" -ne 1 ]; then
    echo "::error::DEXRIVE Android APK could not be installed after 6 attempts"
    exit 1
fi

adb shell pm list packages "$PKG" | grep -F "package:$PKG"
adb shell am start -W -n "$PKG/.MainActivity"
sleep 8

# Verify that the app, not a launcher/permission prompt, is the foreground activity.
if ! adb shell dumpsys activity activities | grep -F "$PKG" > /dev/null; then
    echo "::error::DEXRIVE Inspector activity is not active"
    exit 1
fi

adb exec-out screencap -p > "$DEST"
python3 - "$DEST" <<'PY'
import pathlib
import struct
import sys
path = pathlib.Path(sys.argv[1])
png = path.read_bytes()
if png[:8] != bytes.fromhex("89504e470d0a1a0a") or len(png) <= 15_000:
    raise RuntimeError("Android framebuffer capture missing or truncated")
width, height = struct.unpack(">II", png[16:24])
if width < 360 or height < 640:
    raise RuntimeError(f"Invalid Android display size: {width}x{height}")
print(f"Captured verified real Android screen: {width}x{height}, {len(png)} bytes")
PY
