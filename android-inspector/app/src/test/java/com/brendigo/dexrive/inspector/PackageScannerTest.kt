package com.brendigo.dexrive.inspector

import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PackageScannerTest {
    @Test fun sanitizeUntrustedDisplayName() {
        assertEquals("Android package", PackageScanner.sanitizeDisplayName("  "))
        assertEquals("sample.apk", PackageScanner.sanitizeDisplayName("  sample.apk  "))
        assertEquals(160, PackageScanner.sanitizeDisplayName("x".repeat(250)).length)
        assertEquals("demo.apk", PackageScanner.sanitizeDisplayName("demo.apk\n\t"))
    }

    @Test fun inspectSyntheticApk() {
        val file = File.createTempFile("dexrive-test", ".apk")
        try {
            ZipOutputStream(file.outputStream()).use { zip ->
                for (name in listOf("classes.dex", "AndroidManifest.xml", "lib/arm64-v8a/libx.so", "../escape")) {
                    zip.putNextEntry(ZipEntry(name))
                    zip.write(1)
                    zip.closeEntry()
                }
            }
            val r = PackageScanner.inspectZip(file)
            assertEquals(4, r.entries)
            assertEquals(1, r.dex)
            assertEquals(1, r.native)
            assertEquals(1, r.traversal)
            assertTrue(r.manifest)
        } finally { file.delete() }
    }
}
