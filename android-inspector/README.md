# DEXRIVE APK Inspector (Android)

Separate native Kotlin/Jetpack Compose app, preview version **0.2.0**, independent of Windows 1.0.0.

Choose APK/AAB/ZIP using Android's Storage Access Framework. Calculates a local SHA-256 hash, counts DEX files and native libraries, reports manifest presence and potential archive-path issues. ZIP metadata only: does not extract, upload or execute app code. No internet/storage permissions.

Limits: 512 MiB input and 100,000 entries. Metadata checks are not antivirus or APK-signature verification. Analyze untrusted files in a safe environment.

Build: `./gradlew :app:assembleDebug :app:testDebugUnitTest` from this directory (requires Android SDK and JDK 17). APK is unsigned for production and only intended for testing.
