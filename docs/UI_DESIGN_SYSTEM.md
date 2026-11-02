# DEXRIVA UI implementation guide

**Direction:** DEXRIVA original visual system; this is not an attempt to copy JADX or any third-party visual identity. No GitHub links appear in the standard application surfaces. Original engine namespaces are deliberately retained for compatibility. The Apache 2.0 LICENSE/NOTICE files remain in all distributions.

## Foundations

| Token | Value |
| --- | --- |
| Primary dark | `#101927` |
| Secondary dark | `#172A3F` |
| Brand aqua | `#34D9D0` |
| Secondary text | `#A7B7C9` |
| Display type | System Sans, bold |
| Spacing rhythm | 6 / 12 / 18 / 24 dp |
| Desktop corner radius | 12–18 dp |
| SVG actions | 24×24 viewBox; original DEXRIVA geometry |

Use the Windows display scale and FlatLaf font scaling; do not use hardcoded screen pixels for child layouts. Text labels remain keyboard accessible.

## Screen 01 — Splash (implemented)

- Original Java2D vector composition with aqua command glyph, DEXRIVA wordmark and tagline.
- Base geometry 560×312 logical units, scaled uniformly to the available window size.
- Window constrained to screen bounds and centered. Closes on successful main-window initialization or startup failure.
- Does **not** use a fake timer or delay.

## Screen 02 — Home / Workspace (implemented)

- Navigation: Open package, Open project, APK Inspector.
- Header: mark, title, caption.
- Body: recent project list; double-click opens project, right-click context menu, remove from recent.
- Narrow width (< 680 logical UI units): primary actions stack vertically; normal width: 3 columns.
- Scrollable container for smaller windows, accessible labels, responsive fonts.

## Remaining sequential screens (not yet pixel-validated)

03. Open-file and project chooser, including no-file / permission / invalid-archive states.
04. Decompiler Explorer window: files, resource tree, tabs and source editor.
05. APK Inspector reports and warnings.
06. Search, navigation, rename / deobfuscation and diff flows.
07. Settings, plugins and security reporting.
08. About, error and modal dialogs.
09. Windows installers, portability and titlebar / window chrome.
10. Android native APK Inspector: separate Compose responsive parity.

## Verification required before completion

- Native Windows screenshots at 1280×720, 1920×1080 and 2560×1440.
- Scale at 100%, 125%, 150% and 200%; Windows 10 and Windows 11, window minimum and maximized.
- Check text truncation, 24 px icons, contrast, keyboard focus, screen readers, modal blocking and high-DPI behavior.
- Verify that no old upstream logo or upstream link is visible. Avoid removing LICENSE/NOTICE or altering package namespaces.
- Validate major areas screen-by-screen against **user-approved reference screenshots**, which have not been provided for the new DEXRIVA UI yet.

This is a specification and implementation progress record, not a claim of full pixel-perfect completion.

## Windows packaging — original DEXRIVA wizard

Installer `setup.exe` uses a branded NSIS Modern UI 2 flow in this order:
welcome → Apache 2.0 license acceptance → selectable components (required application, optional shortcuts) → destination folder → installation progress → finish with optional launch. Current-user scope and uninstall registration; preserves user data.

Portable `portable.exe` is a silent self-extracting launcher without any wizard, shortcut creation, registry writes or admin request. On first launch it extracts alongside itself, subsequent launches reuse that folder. The CI smoke test passes `/S` to perform extraction without opening the GUI.

Original graphical assets for the NSIS header and welcome pane are generated from `.dexriva/make_setup_artwork.py` using Python's standard library and are **not** reproduced in chat.

Neither package replaces the Apache 2.0 license or legally required attribution. UI strings are branded DEXRIVA.

**Verification**: packaging CI on Windows must compile both binaries, inspect their PE signatures, validate bundle metadata and test silent install and extraction. Manual user testing of wizard layouts, screen readers and DPI scaling remains necessary.
