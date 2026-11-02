# DEXRIVE Module Migration — Brendigo

## Status and intent

The repository's Gradle modules, primary module directories, CLI distribution naming and desktop packaging paths are renamed from their inherited project identity to **DEXRIVE**. Product design and ownership of brand assets are credited to **Brendigo** at https://brendigo.com.

This migration intentionally preserves existing compiled Java/Kotlin API namespaces (`jadx.*`), resource identifiers used by the decompiler, legacy project file extensions, and extension entry-point class names. These are **technical compatibility contracts**, not end-user branding, and cannot be changed globally without breaking plugins, user projects, persisted settings or externally compiled integrations. They are considered separately for gradual API migration.

## Gradle module mapping

| Previous directory/module | New directory/module |
| --- | --- |
| `jadx-cli` | `dexrive-cli` |
| `jadx-core` | `dexrive-core` |
| `jadx-gui` | `dexrive-gui` |
| `jadx-gui-api` | `dexrive-gui-api` |
| `jadx-commons/*` | `dexrive-commons/dexrive-*` |
| `jadx-plugins/*` | `dexrive-plugins/dexrive-*` |
| `jadx-plugins-tools` | `dexrive-plugins-tools` |
| `buildSrc/.../jadx-*.gradle.kts` | `buildSrc/.../dexrive-*.gradle.kts` |

**CLI application name:** `dexrive`. **GUI script:** `dexrive-gui`. **Windows application:** `DEXRIVE.exe`.

## Validation

```sh
./gradlew --no-daemon :dexrive-core:test :dexrive-gui:test
./gradlew --no-daemon :dexrive-cli:installShadowDist
./gradlew --no-daemon :dexrive-gui:run
python3 -m unittest discover -s tests -p 'test_*.py' -v
```

Windows uses `:dexrive-gui:copyDistWinWithJre` and tests both NSIS EXE packages. Android uses the separate `android-inspector/` Gradle build. Do not release until all jobs pass.

## Legal and ownership

Original DEXRIVE interface, artwork, packaging and Brendigo additions © 2026 Brendigo. Source components derived from the open-source JADX contributors remain under Apache 2.0, with copyright and attribution preserved in LICENSE, NOTICE and relevant source headers. The product brand does not transfer pre-existing copyright to Brendigo.

## Remaining work

1. Compatibility tests for Java plugin API and saved-project files after module renames.
2. Test Linux/macOS desktop packaging under new product names.
3. Inspect remaining historic `jadx` identifiers in service descriptors, runtime resources and IDE metadata before considering an API namespace migration.
4. Capture authentic desktop screenshots from Windows 10/11 at multiple DPI scales; use these in README only after visual QA.
