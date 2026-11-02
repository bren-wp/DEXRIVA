<img src="assets/dexriva-mark.svg" width="80" align="left" alt="DEXRIVA logo">

# DEXRIVA

**Explore beyond the source.**

DEXRIVA is a community-driven desktop workspace for exploring Android bytecode and packages. It is based on the open-source [jadx](https://github.com/skylot/jadx) decompiler. We preserve jadx functionality and add DEXRIVA-specific UI and inspection tools.

> **Development status:** Pre-release. The upstream source import and bootstrap smoke tests succeeded. JVM/Gradle compilation passed CI on 2026-11-02. Windows 1.0.0 binaries are only published if their dedicated build, package and smoke-test workflow succeeds.

## Phase 1

- [x] Import jadx source and original NOTICE / LICENSE, pinned to upstream commit `55982e0af3114a3942f6b5e289d553ff70c1ab9a`.
- [x] Brand the desktop title, welcome screen, window icons, and About dialog.
- [x] Default to a dark FlatLaf look-and-feel with an aqua accent.
- [x] Add **Tools → DEXRIVA APK Inspector**: local ZIP metadata checks for APK/AAB/APKS/APKM/XAPK/ZIP archives, with limits and safety warnings.
- [x] Validate the GUI and core with Gradle tests on CI.

The first four items were committed and passed import smoke checks; they still require full Java compilation and UI testing.

## Quick start (after source import)

Requires **JDK 21+** (for this project's CI baseline).

```bash
./gradlew :jadx-gui:run
./gradlew :jadx-core:test :jadx-gui:test
```

The original jadx command-line and plugin interfaces remain intact in phase 1 to avoid breaking compatibility.

## Security & privacy

The APK Inspector reads ZIP directory metadata locally. It does **not** extract archive entries or upload files, and its indicators are **not** a malware verdict or a cryptographic APK signature verification. Decompilation is performed on untrusted input: use a sandbox when inspecting unknown packages.

## Roadmap

See [docs/ROADMAP.md](docs/ROADMAP.md) for the next UX, analysis, Android and QA phases. Windows release automation builds two self-contained executables when all tests pass. See [releases](https://github.com/bren-wp/DEXRIVA/releases).

## License and upstream attribution

DEXRIVA reuses JADX, by the JADX contributors, under the [Apache License 2.0](LICENSE), retaining upstream [NOTICE](NOTICE) and copyright statements. The original jadx repository is https://github.com/skylot/jadx. The upstream implementation remains credited independently of the DEXRIVA brand. Do not imply that this is an official jadx release.

The initial empty DEXRIVA repository previously contained an MIT license for its initial content; that text is preserved under `docs/INITIAL-REPO-LICENSE.md`. This fork's imported jadx code remains Apache-2.0 licensed.

## Windows 1.0.0 packaging

The Windows workflow builds the Java application with a bundled JRE using the original JADX Gradle tasks. It generates [portable](packaging/windows/dexriva-portable.nsi) and [setup](packaging/windows/dexriva-setup.nsi) EXEs using NSIS. The portable EXE extracts a directory beside itself; it is not an in-memory single file. No code signing certificate is configured. See [release notes](docs/RELEASE_1.0.0.md).
