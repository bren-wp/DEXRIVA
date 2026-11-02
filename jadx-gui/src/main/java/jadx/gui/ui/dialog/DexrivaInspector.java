package jadx.gui.ui.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Offline archive metadata inspection. Does not extract, load, or execute APK contents.
 * Not a malware scanner or cryptographic signature verifier.
 */
public final class DexrivaInspector {
    private static final int MAX_ENTRIES = 100_000;
    private static final long LARGE_ENTRY = 512L * 1024 * 1024;
    private static final double HIGH_RATIO = 100.0;

    private DexrivaInspector() {
    }

    public static void open(Component parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("DEXRIVA | Inspect Android package");
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Android archives (*.apk, *.aab, *.apks, *.apkm, *.xapk, *.zip)",
                "apk", "aab", "apks", "apkm", "xapk", "zip"));
        if (chooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            String report = inspect(chooser.getSelectedFile().toPath());
            JTextArea area = new JTextArea(report);
            area.setEditable(false);
            area.setCaretPosition(0);
            area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(680, 440));
            JOptionPane.showMessageDialog(parent, scroll, "DEXRIVA APK Inspector",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | SecurityException e) {
            JOptionPane.showMessageDialog(parent, "Cannot inspect archive: " + e.getMessage(),
                    "DEXRIVA APK Inspector", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static String inspect(Path path) throws IOException {
        if (!Files.isRegularFile(path)) {
            throw new IOException("Not a regular file: " + path);
        }
        int entries = 0;
        int dexFiles = 0;
        int nativeLibraries = 0;
        int pathTraversalNames = 0;
        int largeEntries = 0;
        int highCompressionRatios = 0;
        boolean manifest = false;
        boolean resources = false;
        boolean v1SignatureFile = false;
        boolean limitReached = false;
        long uncompressed = 0;
        long compressed = 0;
        try (ZipFile zip = new ZipFile(path.toFile())) {
            Enumeration<? extends ZipEntry> iterator = zip.entries();
            while (iterator.hasMoreElements()) {
                if (entries == MAX_ENTRIES) {
                    limitReached = true;
                    break;
                }
                ZipEntry entry = iterator.nextElement();
                entries++;
                String entryName = entry.getName().replace('\\', '/');
                String lower = entryName.toLowerCase(Locale.ROOT);
                if (lower.endsWith(".dex")) {
                    dexFiles++;
                }
                if (lower.endsWith(".so") && (lower.startsWith("lib/") || lower.contains("/lib/"))) {
                    nativeLibraries++;
                }
                if (lower.equals("androidmanifest.xml") || lower.endsWith("/manifest/androidmanifest.xml")) {
                    manifest = true;
                }
                if (lower.equals("resources.arsc")) {
                    resources = true;
                }
                if (lower.startsWith("meta-inf/")
                        && (lower.endsWith(".rsa") || lower.endsWith(".dsa") || lower.endsWith(".ec"))) {
                    v1SignatureFile = true;
                }
                if (entryName.startsWith("/") || entryName.matches("^[a-zA-Z]:/.*")) {
                    pathTraversalNames++;
                } else {
                    for (String part : entryName.split("/")) {
                        if ("..".equals(part)) {
                            pathTraversalNames++;
                            break;
                        }
                    }
                }
                long raw = entry.getSize();
                long packed = entry.getCompressedSize();
                if (raw >= 0) {
                    uncompressed = saturatedAdd(uncompressed, raw);
                    if (raw > LARGE_ENTRY) {
                        largeEntries++;
                    }
                    if (packed > 0 && ((double) raw / packed) > HIGH_RATIO) {
                        highCompressionRatios++;
                    }
                    if (packed == 0 && raw > 0) {
                        highCompressionRatios++;
                    }
                }
                if (packed >= 0) {
                    compressed = saturatedAdd(compressed, packed);
                }
            }
        }
        StringBuilder out = new StringBuilder();
        out.append("DEXRIVA | LOCAL ARCHIVE INSPECTION\n");
        out.append("=================================\n");
        out.append("File: ").append(path.getFileName()).append('\n');
        out.append("Archive bytes: ").append(Files.size(path)).append('\n');
        out.append("ZIP entries inspected: ").append(entries).append('\n');
        out.append("DEX files: ").append(dexFiles).append('\n');
        out.append("Native libraries: ").append(nativeLibraries).append('\n');
        out.append("Android manifest found: ").append(manifest).append('\n');
        out.append("resources.arsc found: ").append(resources).append('\n');
        out.append("META-INF legacy signature files present: ").append(v1SignatureFile).append('\n');
        out.append("Known uncompressed bytes: ").append(uncompressed).append('\n');
        out.append("Known compressed entry bytes: ").append(compressed).append("\n\n");
        out.append("HEURISTIC WARNINGS (not proof of maliciousness)\n");
        out.append("Entries with traversal-like names: ").append(pathTraversalNames).append('\n');
        out.append("Entries larger than 512 MiB: ").append(largeEntries).append('\n');
        out.append("Entries with compression ratio over 100:1: ").append(highCompressionRatios).append('\n');
        if (limitReached) {
            out.append("WARNING: Entry count limit reached; summary is incomplete.\n");
        }
        out.append("\nNo archive entries were extracted or executed.\n");
        out.append("APK Signing Block v2/v3/v4 is NOT verified.\n");
        out.append("This report is NOT a malware verdict or a signature verification.\n");
        return out.toString();
    }

    private static long saturatedAdd(long a, long b) {
        return Long.MAX_VALUE - a < b ? Long.MAX_VALUE : a + b;
    }
}
