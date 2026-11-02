#!/usr/bin/env python3
"""Apply deterministic, idempotent DEXRIVA modifications to the pinned JADX source."""
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


def change(path: str, original: str, replacement: str) -> None:
    target = ROOT / path
    src = target.read_text(encoding="utf-8")
    if replacement in src:
        return
    count = src.count(original)
    if count != 1:
        raise RuntimeError(f"Expected one patch location in {path}, found {count}: {original[:80]!r}")
    target.write_text(src.replace(original, replacement, 1), encoding="utf-8")


change(
    "jadx-gui/src/main/java/jadx/gui/ui/MainWindow.java",
    'private static final String DEFAULT_TITLE = "jadx-gui";',
    'private static final String DEFAULT_TITLE = "DEXRIVA";',
)
change(
    "jadx-gui/src/main/java/jadx/gui/ui/MainWindow.java",
    "import jadx.gui.ui.dialog.AboutDialog;",
    "import jadx.gui.ui.dialog.AboutDialog;\nimport jadx.gui.ui.dialog.DexrivaInspector;",
)
change(
    "jadx-gui/src/main/java/jadx/gui/ui/MainWindow.java",
    "\t\ttools.add(debuggerAction);",
    '\t\ttools.add(debuggerAction);\n'
    '\t\tJMenuItem inspectorItem = new JMenuItem("DEXRIVA APK Inspector");\n'
    '\t\tinspectorItem.addActionListener(event -> DexrivaInspector.open(this));\n'
    '\t\ttools.add(inspectorItem);',
)
change(
    "jadx-gui/src/main/java/jadx/gui/utils/LafManager.java",
    "import java.util.LinkedHashMap;",
    "import java.awt.Color;\nimport java.util.LinkedHashMap;",
)
change(
    "jadx-gui/src/main/java/jadx/gui/utils/LafManager.java",
    "public static final String INITIAL_THEME_NAME = FlatLightLaf.NAME;",
    "public static final String INITIAL_THEME_NAME = FlatDarkLaf.NAME;",
)
change(
    "jadx-gui/src/main/java/jadx/gui/utils/LafManager.java",
    "UIManager.setLookAndFeel(theme);",
    'UIManager.setLookAndFeel(theme);\n'
    '\t\t\tUIManager.put("Component.accentColor", new Color(0x34D9D0));\n'
    '\t\t\tUIManager.put("Button.arc", 12);\n'
    '\t\t\tUIManager.put("Component.arc", 12);',
)
change(
    "jadx-gui/src/main/java/jadx/gui/ui/startpage/StartPagePanel.java",
    "import javax.swing.JList;",
    "import javax.swing.JList;\nimport javax.swing.JLabel;",
)
change(
    "jadx-gui/src/main/java/jadx/gui/ui/startpage/StartPagePanel.java",
    "\t\tcenter.add(start, BorderLayout.PAGE_START);",
    '\t\tJPanel brandHeader = new JPanel();\n'
    '\t\tbrandHeader.setLayout(new BoxLayout(brandHeader, BoxLayout.PAGE_AXIS));\n'
    '\t\tJLabel brandTitle = new JLabel("DEXRIVA");\n'
    '\t\tbrandTitle.setAlignmentX(LEFT_ALIGNMENT);\n'
    '\t\tbrandTitle.setFont(baseFont.deriveFont(Font.BOLD, Math.max(22f, baseFont.getSize2D() * 2f)));\n'
    '\t\tJLabel brandSubtitle = new JLabel("Explore beyond the source.  |  Offline Android analysis");\n'
    '\t\tbrandSubtitle.setAlignmentX(LEFT_ALIGNMENT);\n'
    '\t\tbrandHeader.add(brandTitle);\n'
    '\t\tbrandHeader.add(Box.createVerticalStrut(6));\n'
    '\t\tbrandHeader.add(brandSubtitle);\n'
    '\t\tbrandHeader.add(Box.createVerticalStrut(24));\n'
    '\t\tbrandHeader.add(start);\n'
    '\t\tcenter.add(brandHeader, BorderLayout.PAGE_START);',
)
change(
    "jadx-gui/src/main/java/jadx/gui/ui/startpage/StartPagePanel.java",
    "setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));",
    "setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));",
)
change(
    "jadx-gui/src/main/java/jadx/gui/ui/dialog/AboutDialog.java",
    'new JLabel("JADX", logo, SwingConstants.CENTER)',
    'new JLabel("DEXRIVA", logo, SwingConstants.CENTER)',
)
change(
    "jadx-gui/src/main/java/jadx/gui/ui/dialog/AboutDialog.java",
    'new JLabel("Dex to Java decompiler")',
    'new JLabel("Android Code Explorer • Based on open-source JADX")',
)
change(
    "jadx-gui/src/main/java/jadx/gui/ui/dialog/AboutDialog.java",
    'new JLabel("JADX version: " + JadxDecompiler.getVersion())',
    'new JLabel("JADX engine version: " + JadxDecompiler.getVersion())',
)
change(
    "jadx-gui/src/main/java/jadx/gui/ui/dialog/AboutDialog.java",
    '"/logos/jadx-logo-48px.png"',
    '"/logos/dexriva-logo-48px.png"',
)
for pixel_size in ("16px", "32px", "48px"):
    change(
        "jadx-gui/src/main/java/jadx/gui/utils/UiUtils.java",
        f'"/logos/jadx-logo-{pixel_size}.png"',
        f'"/logos/dexriva-logo-{pixel_size}.png"',
    )
change(
    "jadx-gui/src/main/java/jadx/gui/utils/UiUtils.java",
    '"/logos/jadx-logo.png"',
    '"/logos/dexriva-logo.png"',
)
print("DEXRIVA source changes successfully applied.")
