<div align="center">
  <img src="assets/dexrive-banner.svg" width="100%" alt="DEXRIVE — Android code explorer by Brendigo" />
</div>

<div align="center">
  <a href="https://brendigo.com"><strong>brendigo.com</strong></a> ·
  <a href="https://github.com/bren-wp/DEXRIVA/releases">Windows downloads</a> ·
  <a href="docs/UI_DESIGN_SYSTEM.md">Design system</a> ·
  <a href="docs/ROADMAP.md">Roadmap</a>
</div>

# DEXRIVE — Explore beyond the source.

**By Brendigo.** Modern desktop tools for exploring Android application packages, resources, and bytecode. Built around a powerful existing open-source decompilation engine, with an original DEXRIVE interface, product identity and packaging.

> **Release transparency:** The last published release is `v1.0.0`, which uses the earlier DEXRIVA filename and appearance. The **DEXRIVE** visual refresh is being developed and tested in [PR #2](https://github.com/bren-wp/DEXRIVA/pull/2). Do not mistake this design presentation for screenshots of the current downloadable release.

<table><tr>
<td width="33%" align="center"><img src="assets/feature-inspect.svg" width="220" alt="APK inspection icon"/><br/><b>APK Inspector</b><br/><sub>ZIP metadata, DEX inventory, safety indicators</sub></td>
<td width="33%" align="center"><img src="assets/feature-code.svg" width="220" alt="Source explorer icon"/><br/><b>Code Explorer</b><br/><sub>Classes, packages, references, search</sub></td>
<td width="33%" align="center"><img src="assets/feature-privacy.svg" width="220" alt="Offline analysis icon"/><br/><b>Local-first</b><br/><sub>Inspect locally; no external barcode service</sub></td>
</tr></table>

## Design preview

<img src="assets/dexrive-workspace-concept.svg" width="100%" alt="Illustrated design concept for the DEXRIVE workspace; not an actual screenshot"/>

*Design illustration, not a screenshot or evidence that every illustrated detail is already implemented. Real build screenshots will be added only after capture and visual review.*

## What it does

- Decompiles Android DEX/APK bytecode for source exploration using the integrated engine.
- Navigates packages, classes, references, Android resources and manifests.
- Offers full-text and class search, annotations, rename and deobfuscation tools.
- Includes an **offline APK archive inspector** for quick metadata and ZIP structure diagnostics; it is **not** an antivirus scanner or a signature validator.
- Offers a dark desktop interface with DEXRIVE branding and a responsive starting workspace.
- Includes a separate **Android APK Inspector preview** in `android-inspector/`; the Android preview is **not** the Windows production app.

## Windows editions

| Package | User experience |
| --- | --- |
| `setup.exe` | Branded installer, license acceptance, directory selection, optional shortcuts, uninstall support |
| `portable.exe` | No installation wizard; extracts the bundled app beside the executable on first launch, then opens it |

Both packages include a compatible Java runtime. Windows files are currently distributed via [GitHub Releases](https://github.com/bren-wp/DEXRIVA/releases). **The DEXRIVE-named installers require a new successful release build and are not yet available for download.** Windows executables currently have no Authenticode code-signing certificate.

## Brand identity

| Field | Value |
| --- | --- |
| Product | **DEXRIVE** |
| Product author / brand creator | **Brendigo** |
| Website | **https://brendigo.com** |
| Product slogan | **Explore beyond the source.** |
| Visual identity | Midnight navy, aqua accent, distinctive command glyph |
| Brand assets | [Logo](assets/dexrive-mark.svg) · [Banner](assets/dexrive-banner.svg) · [Workspace illustration](assets/dexrive-workspace-concept.svg) |

This branding identifies the **DEXRIVE product, the Brendigo design work and product-specific additions**. It does not supersede copyright held by other contributors to incorporated source code.

## Building from source

Use JDK 21 and the Gradle wrapper. Native packaging additionally requires Windows and NSIS:

```sh
./gradlew :jadx-gui:test :jadx-core:test
./gradlew :jadx-gui:run
```

The existing `jadx.*` namespaces and certain module paths deliberately remain unchanged for binary compatibility with the decompiler engine and plugin ecosystem; users see the DEXRIVE name. Changing all internal identifiers without migration would break integrations and stored data.

## Security and limitations

Unknown APKs and archives are untrusted input; analyze them on an isolated machine where appropriate. ZIP warning heuristics do **not** establish whether an APK is malicious. DEXRIVE cannot guarantee reconstruction of original application source code. UI scaling, installer behavior, accessibility and offline claims are verified per feature and release, not assumed from concept art.

## Licensing and acknowledgments

**DEXRIVE product branding and newly authored visual assets: © 2026 Brendigo.**

Some underlying software is derived from [JADX by skylot and contributors](https://github.com/skylot/jadx), under the **Apache License 2.0**. Original copyright and required notices remain intact in [LICENSE](LICENSE) and [NOTICE](NOTICE). Brendigo's additional copyright statement is in [BRAND_NOTICE.md](BRAND_NOTICE.md). The original third-party authorship and software provenance are not misrepresented.

**DEXRIVE is an independent product presentation, not an official JADX release.**
