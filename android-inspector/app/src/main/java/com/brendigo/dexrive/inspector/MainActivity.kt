package com.brendigo.dexrive.inspector

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            var report by remember { mutableStateOf<PackageReport?>(null) }
            var error by remember { mutableStateOf<String?>(null) }
            var loading by remember { mutableStateOf(false) }
            val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
                if (uri != null) {
                    loading = true
                    error = null
                    report = null
                    scope.launch {
                        val result = withContext(Dispatchers.IO) { runCatching { PackageScanner.inspect(this@MainActivity, uri) } }
                        result.onSuccess { report = it }.onFailure { error = "Unable to inspect this package. Choose a supported file and try again." }
                        loading = false
                    }
                }
            }
            val aqua = Color(0xFF34D9D0)
            MaterialTheme(colorScheme = darkColorScheme(primary = aqua, background = Color(0xFF101927), surface = Color(0xFF1A2B40))) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    Column(modifier = Modifier.widthIn(max = 720.dp).fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("DEXRIVE", fontSize = 30.sp, color = aqua)
                        Text("ANDROID APK INSPECTOR", fontSize = 19.sp)
                        Text("by Brendigo  •  brendigo.com", fontSize = 13.sp, color = Color(0xFFA7B7C9))
                        Text("Offline ZIP metadata inspection • No network or storage permissions.")
                        Button(onClick = { picker.launch(arrayOf("*/*")) }, enabled = !loading,
                            modifier = Modifier.fillMaxWidth()) { Text("Open Android package") }
                        if (loading) CircularProgressIndicator()
                        if (error != null) Text("Error: $error", color = Color(0xFFFFB39F))
                        report?.let { item ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(item.name, fontSize = 19.sp)
                                    Text("ZIP entries: ${item.entries}")
                                    Text("DEX files: ${item.dex}")
                                    Text("Native libraries: ${item.native}")
                                    Text("Manifest: ${item.manifest}")
                                    Text("Traversal-like ZIP paths: ${item.traversal}")
                                    Text("High compression ratio entries: ${item.highCompression}")
                                    if (item.truncated) Text("Entry limit reached; partial results")
                                    Text("SHA-256:", color = aqua)
                                    Text(item.sha256, fontSize = 12.sp)
                                }
                            }
                        }
                        Text("Only archive metadata is inspected. No files are extracted or executed. Not a malware or APK-signature verification tool.",
                            fontSize = 13.sp)
                    }
                    }
                }
            }
        }
    }
}
