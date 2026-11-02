# DEXRIVE 1.1.0 — Brendigo edition (Windows)

**Publisher / brand author:** Brendigo · https://brendigo.com

This release packages the DEXRIVE-branded graphical application with the established decompiler backend and the new native splash / home / APK Inspector experience.

## Windows downloads

- `DEXRIVE-1.1.0-setup.exe` — per-user NSIS Modern UI setup with original DEXRIVE artwork, Apache-2.0 license acceptance, selectable shortcuts, install directory, uninstall registration, optional launch and bundled Java runtime.
- `DEXRIVE-1.1.0-portable.exe` — no installer wizard or registry, extracts next to the file and launches the bundled executable. First launch requires a writable directory.

## Branding

Original DEXRIVE dark/aqua logo, app splash, workspace layout, menu icon updates and Brendigo website/publisher details. Marketing assets and visual identity are located in `assets/`, `docs/BRAND_GUIDELINES.md` and the repository README.

## Safety and limitations

The archive inspector reads ZIP metadata locally and does not extract or execute unknown packages, but **is not a malware verdict or full signing verification**. Unknown apps may be dangerous to parse. This release is not Authenticode signed. Full Windows 10/11 DPI and accessibility pixel-level validation requires additional device testing.

## Attribution and license

DEXRIVE branding and original art © 2026 Brendigo. The bundled decompiler includes prior work from the open-source JADX project. LICENSE and NOTICE preserve required Apache-2.0 licensing and historical author credits; BRAND_NOTICE.md records Brendigo's distinct contributions.
