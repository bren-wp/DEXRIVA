"""DEXRIVE bootstrap smoke tests. Run after importing pinned JADX."""
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
        content = (ROOT / "jadx-gui/src/main/java/jadx/gui/ui/dialog/AboutDialog.java").read_text()
        self.assertIn("DEXRIVE", content)
        self.assertNotIn("Based on open-source JADX", content)
        self.assertNotIn("JADX logo", content)

    def test_splash_and_workspace_are_branded(self):
        splash = (ROOT / "jadx-gui/src/main/java/jadx/gui/ui/DexrivaSplash.java").read_text()
        home = (ROOT / "jadx-gui/src/main/java/jadx/gui/ui/startpage/StartPagePanel.java").read_text()
        self.assertIn("DEXRIVE", splash)
        self.assertIn("PREPARING WORKSPACE", splash)
        self.assertIn("Open package", home)
        self.assertIn("APK Inspector", home)
        self.assertIn("componentResized", home)

    def test_upstream_logo_assets_not_shipped(self):
        logos = ROOT / "jadx-gui/src/main/resources/logos"
        self.assertFalse(list(logos.glob("jadx-logo*")))
        self.assertFalse((ROOT / "jadx-gui/dist/windows/jadx-logo.ico").exists())
        self.assertFalse((ROOT / "jadx-gui/dist/macos/jadx-logo.icns").exists())
        self.assertTrue((ROOT / "jadx-gui/dist/macos/dexriva-logo.icns").exists())

    def test_no_auto_upstream_update_or_issue_links(self):
        main = (ROOT / "jadx-gui/src/main/java/jadx/gui/ui/MainWindow.java").read_text()
        error = (ROOT / "jadx-gui/src/main/java/jadx/gui/report/ExceptionDialog.java").read_text()
        self.assertNotIn("checkForUpdate();", main)
        self.assertNotIn("github.com/", error)
        self.assertNotIn("new JadxUpdate()", main)

    def test_native_inspector_in_tools_menu(self):
        content = (ROOT / "jadx-gui/src/main/java/jadx/gui/ui/MainWindow.java").read_text()
        self.assertIn("DexrivaInspector.open(this)", content)
        self.assertIn('DEFAULT_TITLE = "DEXRIVE"', content)

    def test_generated_png_icons(self):
        logos = ROOT / "jadx-gui/src/main/resources/logos"
        sizes = [(16, "dexriva-logo-16px.png"), (32, "dexriva-logo-32px.png"), (48, "dexriva-logo-48px.png"), (256, "dexriva-logo.png")]
        for size, filename in sizes:
            with self.subTest(filename=filename):
                data = (logos / filename).read_bytes()
                self.assertEqual(data[:8], b"\x89PNG\r\n\x1a\n")
                self.assertEqual(struct.unpack(">II", data[16:24]), (size, size))

if __name__ == "__main__":
    unittest.main()
