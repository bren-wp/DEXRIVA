"""DEXRIVA bootstrap smoke tests. Run after importing pinned JADX."""
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
        self.assertIn("DEXRIVA", content)
        self.assertIn("Based on open-source JADX", content)

    def test_native_inspector_in_tools_menu(self):
        content = (ROOT / "jadx-gui/src/main/java/jadx/gui/ui/MainWindow.java").read_text()
        self.assertIn("DexrivaInspector.open(this)", content)
        self.assertIn('DEFAULT_TITLE = "DEXRIVA"', content)

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
