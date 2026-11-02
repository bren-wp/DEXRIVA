# DEXRIVE 1.2.0 — Brendigo module migration (Windows)

**Publisher / brand author:** Brendigo · https://brendigo.com

This release packages the DEXRIVE-branded graphical application with the established decompiler backend and the new native splash / home / APK Inspector experience.

## Windows downloads

- `DEXRIVE-1.2.0-setup.exe` — per-user NSIS Modern UI setup with original DEXRIVE artwork, Apache-2.0 license acceptance, selectable shortcuts, install directory, uninstall registration, optional launch and bundled Java runtime.
- `DEXRIVE-1.2.0-portable.exe` — no installer wizard or registry, extracts next to the file and launches the bundled executable. First launch requires a writable directory.

## Android builds in this release

- `DEXRIVE-APK-Inspector-0.3.0-debug.apk` — Android debug APK, development-key signed; intended for testing only.
- `DEXRIVE-APK-Inspector-0.3.0-unsigned.aab` — Android release bundle, **not signed** for Google Play. Upload requires production app-signing credentials and appropriate version identity.

Both Android builds must pass unit tests and come from the same reviewed source commit as the Windows installers; the release includes `SHA256SUMS.txt`.

## Branding

Original DEXRIVE dark/aqua logo, app splash, workspace layout, menu icon updates and Brendigo website/publisher details. Marketing assets and visual identity are located in `assets/`, `docs/BRAND_GUIDELINES.md` and the repository README.

## Safety and limitations

The archive inspector reads ZIP metadata locally and does not extract or execute unknown packages, but **is not a malware verdict or full signing verification**. Unknown apps may be dangerous to parse. This release is not Authenticode signed. Full Windows 10/11 DPI and accessibility pixel-level validation requires additional device testing.

## Attribution and license

DEXRIVE branding and original art © 2026 Brendigo. The bundled decompiler includes prior work from the open-source JADX project. LICENSE and NOTICE preserve required Apache-2.0 licensing and historical author credits; BRAND_NOTICE.md records Brendigo's distinct contributions.

## Gradle and repository migration

All core module directories and project coordinates now use `dexrive-*`, including CLI, GUI, core, plugin modules and shared components. Windows launcher resources, scripts and logo filenames have been migrated. Existing Java API packages remain intact for compatibility. The Android Inspector preview uses an updated application package namespace.

**Previous v1.1.0 release is not replaced.** New binaries are published only after Linux GUI/core, Android and Windows installation/portable checks succeed.
