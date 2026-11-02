# DEXRIVA 1.0.0 — Windows

DEXRIVA is a separate community distribution derived from open-source jadx and is **not** an official jadx release.

- Branded desktop interface with dark FlatLaf, scalable welcome screen and window icons.
- JADX engine for decompilation, Java source inspection, searching and deobfuscation.
- Offline APK Inspector for archive metadata, DEX count, native libraries, manifest indicators and archive safety heuristics.
- Windows **setup.exe**: per-user installer with uninstall support.
- Windows **portable.exe**: self-extracting launcher. The first run extracts a self-contained program folder beside the EXE and then launches it. No elevated rights required. Keep the extracted folder for subsequent launches.
- Bundled Java runtime: a separate Java installation is not required.

**Known limitations:** Not a malware detector, APK signature validator or guarantee of full Java-source recovery. Unknown files should be analyzed in a sandbox. Current binaries are not Authenticode signed; Windows SmartScreen may display a warning. Responsive layout is implemented for the welcome screen, but comprehensive device/DPI matrix testing is still outstanding. Android APK Inspector is a separate future deliverable and is not bundled into this Windows release.

## Source and credit
Based on JADX by Skylot and contributors, licensed Apache-2.0. Retains LICENSE and NOTICE. Sources at https://github.com/bren-wp/DEXRIVA.
