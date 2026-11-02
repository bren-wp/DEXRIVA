package jadx.gui.ui.dialog;

import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;
import static java.nio.file.Files.newOutputStream;

class DexriveInspectorTest {
    @TempDir
    Path temp;

    @Test
    void inspectsZipEntriesLocally() throws IOException {
        Path archive = temp.resolve("fixture.apk");
        try (ZipOutputStream zip = new ZipOutputStream(newOutputStream(archive))) {
            add(zip, "classes.dex");
            add(zip, "classes2.dex");
            add(zip, "AndroidManifest.xml");
            add(zip, "resources.arsc");
            add(zip, "lib/arm64-v8a/libfixture.so");
            add(zip, "META-INF/CERT.RSA");
            add(zip, "../outside.txt");
        }
        String report = DexriveInspector.inspect(archive);
        assertThat(report).contains("DEX files: 2");
        assertThat(report).contains("Native libraries: 1");
        assertThat(report).contains("Android manifest found: true");
        assertThat(report).contains("resources.arsc found: true");
        assertThat(report).contains("Entries with traversal-like names: 1");
        assertThat(report).contains("NOT a malware verdict");
    }

    private static void add(ZipOutputStream zip, String name) throws IOException {
        zip.putNextEntry(new ZipEntry(name));
        zip.write(new byte[] { 1, 2, 3, 4 });
        zip.closeEntry();
    }
}
