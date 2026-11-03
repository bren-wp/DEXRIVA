# DEXRIVE 1.3.0 — Brendigo Windows + Android release

**Publisher and product brand:** Brendigo · https://brendigo.com

## Highlights
- Unified dark/aqua DEXRIVE splash, adaptive Android launcher and native Android 12+ splash; responsive Inspector and Windows workspace layouts.
- Windows start screen improvements, keyboard navigation for recent projects, responsive APK Inspector and safe cancel-on-close behavior.
- Android local-only archive metadata inspection with readable warning/statistic cards, SHA-256 copy action and bounded document names.
- Polished marketing README with a clearly separated gallery of genuine screenshots captured from CI-launched applications, not concept artwork.
- Updated developer and release metadata for desktop v1.3.0 and Android Inspector v0.4.0.

## Assets
- `DEXRIVE-1.3.0-setup.exe`: per-user Windows installer, bundled Java runtime, original DEXRIVE icon/artwork.
- `DEXRIVE-1.3.0-portable.exe`: portable Windows self-extracting launcher, requires writable directory on first run.
- `DEXRIVE-APK-Inspector-0.4.0-debug.apk`: debug-key-signed development/test build for Android 7.0+.
- `DEXRIVE-APK-Inspector-0.4.0-unsigned.aab`: **unsigned**, requires the legitimate production upload key for Play release.
- `SHA256SUMS.txt`: published checksum manifest.

## Validation
Desktop Java/CLI tests; Android unit tests and APK/AAB compilation; NSIS setup+portable packaging and silent install/extraction smoke tests. Genuine Windows and Android screenshots are separately generated in the UI capture workflow and checked before they are shown as real product screenshots.

## Security, integrity and limitations
The Android inspector reads archive metadata locally, does not execute or extract package contents, and is not an antivirus or cryptographic signature checker. The full decompilation engine is a desktop feature; Android only provides the package metadata inspector. EXEs lack Authenticode signatures. Production AAB signing requires a private signing credential which is **not in the repository**.

## Attribution and licensing
DEXRIVE original art and product improvements © 2026 Brendigo. The included open-source JADX engine and prior contributors retain Apache 2.0 copyright/notice obligations in `LICENSE` and `NOTICE` with Brendigo's additions recorded in `BRAND_NOTICE.md`.
