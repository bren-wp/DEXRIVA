"""DEXRIVE module, branding and legal retention smoke tests."""
import struct
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

class BrandIntegrationTest(unittest.TestCase):
    def test_original_license_and_notice(self):
        text = (ROOT / "LICENSE").read_text(encoding="utf-8")
        self.assertTrue(text.lstrip().startswith("Apache License"))
        self.assertTrue((ROOT / "NOTICE").is_file())

    def test_about_upstream_attribution(self):
        content = (ROOT / "dexrive-gui/src/main/java/jadx/gui/ui/dialog/AboutDialog.java").read_text()
        self.assertIn("DEXRIVE", content)
        self.assertNotIn("Based on open-source JADX", content)
        self.assertNotIn("JADX logo", content)

    def test_splash_and_workspace_are_branded(self):
        splash = (ROOT / "dexrive-gui/src/main/java/jadx/gui/ui/DexriveSplash.java").read_text()
        home = (ROOT / "dexrive-gui/src/main/java/jadx/gui/ui/startpage/StartPagePanel.java").read_text()
        self.assertIn("DEXRIVE", splash)
        self.assertIn("PREPARING WORKSPACE", splash)
        self.assertIn("Open package", home)
        self.assertIn("APK Inspector", home)
        self.assertIn("componentResized", home)

    def test_upstream_logo_assets_not_shipped(self):
        logos = ROOT / "dexrive-gui/src/main/resources/logos"
        self.assertFalse(list(logos.glob("jadx-logo*")))
        self.assertFalse((ROOT / "dexrive-gui/dist/windows/jadx-logo.ico").exists())
        self.assertFalse((ROOT / "dexrive-gui/dist/macos/jadx-logo.icns").exists())
        self.assertTrue((ROOT / "dexrive-gui/dist/macos/dexrive-logo.icns").exists())

    def test_no_auto_upstream_update_or_issue_links(self):
        main = (ROOT / "dexrive-gui/src/main/java/jadx/gui/ui/MainWindow.java").read_text()
        error = (ROOT / "dexrive-gui/src/main/java/jadx/gui/report/ExceptionDialog.java").read_text()
        self.assertNotIn("checkForUpdate();", main)
        self.assertNotIn("github.com/", error)
        self.assertNotIn("new JadxUpdate()", main)


    def test_product_assets_and_attribution(self):
        readme = (ROOT / "README.md").read_text(encoding="utf-8")
        self.assertIn("By Brendigo", readme)
        self.assertIn("brendigo.com", readme)
        for path in ["assets/dexrive-mark.svg", "assets/dexrive-banner.svg",
                     "assets/dexrive-workspace-concept.svg",
                     "assets/feature-inspect.svg", "assets/feature-code.svg",
                     "assets/feature-privacy.svg", "BRAND_NOTICE.md"]:
            self.assertTrue((ROOT / path).is_file(), path)

    def test_native_inspector_in_tools_menu(self):
        content = (ROOT / "dexrive-gui/src/main/java/jadx/gui/ui/MainWindow.java").read_text()
        self.assertIn("DexriveInspector.open(this)", content)
        self.assertIn('DEFAULT_TITLE = "DEXRIVE • Brendigo"', content)

    def test_all_locales_use_new_product_name(self):
        root = ROOT / "dexrive-gui/src/main/resources/i18n"
        for path in root.glob("Messages_*.properties"):
            data = path.read_text(encoding="utf-8")
            self.assertNotIn("DEXRIVA", data, str(path))
            self.assertNotIn("Jadx is running low", data, str(path))
            self.assertIn("start_page.title=DEXRIVE Workspace", data, str(path))


    def test_installer_artwork_matches_generated_names(self):
        import re
        script = (ROOT / ".dexrive/make_setup_artwork.py").read_text(encoding="utf-8")
        installer = (ROOT / "packaging/windows/dexrive-setup.nsi").read_text(encoding="utf-8")
        declared = ("dexrive-header.bmp", "dexrive-welcome.bmp")
        self.assertGreaterEqual(len(declared), 2)
        for name in declared:
            self.assertIn('"' + name + '"', script)
            self.assertIn(name, installer)
        self.assertNotIn("dexriva-", installer)

    def test_release_pipeline_has_all_platform_packages(self):
        workflow = (ROOT / ".github/workflows/windows-release.yml").read_text(encoding="utf-8")
        self.assertIn("needs: [windows, android]", workflow)
        self.assertIn("DEXRIVE-1.2.0-setup.exe", workflow)
        self.assertIn("DEXRIVE-1.2.0-portable.exe", workflow)
        self.assertIn("DEXRIVE-APK-Inspector-0.3.0-debug.apk", workflow)
        self.assertIn("DEXRIVE-APK-Inspector-0.3.0-unsigned.aab", workflow)
        self.assertIn("testDebugUnitTest :app:assembleDebug :app:bundleRelease", workflow)
        self.assertIn("SHA256SUMS.txt", workflow)

    def test_android_layout_is_width_constrained(self):
        activity = (ROOT / "android-inspector/app/src/main/java/com/brendigo/dexrive/inspector/MainActivity.kt").read_text(encoding="utf-8")
        self.assertIn("widthIn(max = 720.dp)", activity)
        self.assertIn("verticalScroll(rememberScrollState())", activity)

    def test_renamed_gradle_modules(self):
        settings = (ROOT / "settings.gradle.kts").read_text(encoding="utf-8")
        self.assertIn('rootProject.name = "dexrive"', settings)
        for module in ("dexrive-cli", "dexrive-core", "dexrive-gui",
                       "dexrive-gui-api", "dexrive-plugins-tools"):
            self.assertTrue((ROOT / module / "build.gradle.kts").is_file(), module)
            self.assertIn('include("' + module + '")', settings)
        for original in ("jadx-cli", "jadx-core", "jadx-gui", "jadx-gui-api",
                         "jadx-plugins", "jadx-commons", "jadx-plugins-tools"):
            self.assertFalse((ROOT / original).exists(), original)
        self.assertTrue((ROOT / "dexrive-gui/src/main/java/jadx/gui/ui/DexriveSplash.java").exists())
        self.assertTrue((ROOT / "dexrive-gui/src/main/java/jadx/gui/ui/dialog/DexriveInspector.java").exists())
        self.assertTrue((ROOT / ".dexrive/make_icons.py").exists())
        self.assertFalse((ROOT / ".dexriva").exists())

    def test_legal_provenance_and_brand_credit(self):
        brand_notice = (ROOT / "BRAND_NOTICE.md").read_text(encoding="utf-8")
        self.assertIn("Brendigo", brand_notice)
        self.assertTrue((ROOT / "LICENSE").exists())
        self.assertTrue((ROOT / "NOTICE").exists())

    def test_generated_png_icons(self):
        logos = ROOT / "dexrive-gui/src/main/resources/logos"
        sizes = [(16, "dexrive-logo-16px.png"), (32, "dexrive-logo-32px.png"), (48, "dexrive-logo-48px.png"), (256, "dexrive-logo.png")]
        for size, filename in sizes:
            with self.subTest(filename=filename):
                data = (logos / filename).read_bytes()
                self.assertEqual(data[:8], b"\x89PNG\r\n\x1a\n")
                self.assertEqual(struct.unpack(">II", data[16:24]), (size, size))

if __name__ == "__main__":
    unittest.main()
