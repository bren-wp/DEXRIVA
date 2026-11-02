# DEXRIVA Roadmap

## Phase 1: safe upstream baseline (in progress)
- Pin a reviewed JADX snapshot and retain its Apache 2.0 LICENSE/NOTICE.
- Update app chrome, dark default, responsive welcome panel, icon assets, About attribution.
- Add a new offline APK archive metadata inspector in the Tools menu.
- Establish CI running GUI/core tests before releases.

## Phase 2: distinct desktop UX (planned)
- DEXRIVA unified workspace with quick actions, command palette, compact/comfortable layouts.
- Better scalable docking, accessible navigation, high-DPI/icon QA on Windows, Linux, macOS.
- Rework settings and workspace to separate application branding from upstream internals.
- Comprehensive keyboard-only accessibility and screen reader checks.

## Phase 3: enhanced analysis (planned)
- Permissions explorer with decoded binary manifest analysis.
- DEX/native-library inventory and dependency correlation.
- Diffing two APK releases with semantic class and resource changes.
- More targeted static analysis rules, exportable reports with false-positive tracking.

## Phase 4: hardening & release (planned)
- Threat modeling, zip/archive fuzz tests, dependency and license audits.
- Cross-platform packaging and signed artifacts where certificates are available.
- Performance benchmarks with large APKs, memory pressure and regression fixtures.

The checklist is aspirational until implemented, tested, and shipped. Keep jadx namespaces and API compatibility initially.

## Android APK Inspector (new scope)

Android edition is **not** a JVM desktop port. Build it as a separate, isolated Android project using Kotlin + Jetpack Compose, the Android Storage Access Framework and streaming ZIP header enumeration. Respect Android-scoped storage and minSdk >= 24. Never execute, unpack or upload unknown APK data. Implement manifest parsing, signing certificate status (using Android SDK facilities where appropriate) and hash reports only after unit and instrumented tests. Do not market inspection findings as malware verdicts. The Android binary is not included in DEXRIVA Windows v1.0.0.
