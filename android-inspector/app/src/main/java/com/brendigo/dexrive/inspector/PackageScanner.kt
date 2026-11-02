package com.brendigo.dexrive.inspector

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.IOException
import java.security.MessageDigest
import java.util.Locale
import java.util.zip.ZipFile

data class PackageReport(val name: String, val entries: Int, val dex: Int, val native: Int,
    val manifest: Boolean, val traversal: Int, val highCompression: Int, val truncated: Boolean, val sha256: String)
internal data class ZipStats(val entries: Int, val dex: Int, val native: Int,
    val manifest: Boolean, val traversal: Int, val highCompression: Int, val truncated: Boolean)
object PackageScanner {
    private const val LIMIT_BYTES = 512L * 1024 * 1024
    internal fun inspect(context: Context, uri: Uri): PackageReport {
        val name = runCatching {
            context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use {
                if (it.moveToFirst()) it.getString(0) else null
            }
        }.getOrNull() ?: "Android package"
        val tmp = File.createTempFile("dexrive-", ".zip", context.cacheDir)
        try {
            val hash = MessageDigest.getInstance("SHA-256")
            var size = 0L
            context.contentResolver.openInputStream(uri).use { input ->
                if (input == null) throw IOException("Cannot open document")
                tmp.outputStream().use { output ->
                    val buf = ByteArray(65536)
                    while (true) {
                        val read = input.read(buf)
                        if (read < 0) break
                        if (read == 0) continue
                        size += read
                        if (size > LIMIT_BYTES) throw IOException("File exceeds 512 MiB safety limit")
                        output.write(buf, 0, read)
                        hash.update(buf, 0, read)
                    }
                }
            }
            val stat = inspectZip(tmp)
            return PackageReport(name, stat.entries, stat.dex, stat.native, stat.manifest,
                stat.traversal, stat.highCompression, stat.truncated,
                hash.digest().joinToString("") { "%02x".format(it) })
        } finally {
            tmp.delete()
        }
    }
    internal fun inspectZip(file: File): ZipStats {
        var entries = 0
        var dex = 0
        var native = 0
        var manifest = false
        var traversal = 0
        var ratios = 0
        var truncated = false
        ZipFile(file).use { zip ->
            val iter = zip.entries()
            while (iter.hasMoreElements()) {
                if (entries >= 100_000) { truncated = true; break }
                val e = iter.nextElement()
                entries++
                val n = e.name.replace('\\', '/')
                val lower = n.lowercase(Locale.ROOT)
                if (lower.endsWith(".dex")) dex++
                if (lower.endsWith(".so") && (lower.startsWith("lib/") || lower.contains("/lib/"))) native++
                if (lower == "androidmanifest.xml" || lower.endsWith("/manifest/androidmanifest.xml")) manifest = true
                if (n.startsWith("/") || Regex("^[a-zA-Z]:/").containsMatchIn(n) || n.split('/').contains("..")) traversal++
                if (e.size > 0 && (e.compressedSize == 0L || (e.compressedSize > 0L && e.size.toDouble() / e.compressedSize > 100))) ratios++
            }
        }
        return ZipStats(entries, dex, native, manifest, traversal, ratios, truncated)
    }
}
