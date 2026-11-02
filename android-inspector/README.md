# DEXRIVE APK Inspector (Android)

Separate native Kotlin/Jetpack Compose app, preview version **0.3.0**, a separate Android application shipped alongside DEXRIVE desktop releases.

Choose APK/AAB/ZIP using Android's Storage Access Framework. Calculates a local SHA-256 hash, counts DEX files and native libraries, reports manifest presence and potential archive-path issues. ZIP metadata only: does not extract, upload or execute app code. No internet/storage permissions.

Limits: 512 MiB input and 100,000 entries. Metadata checks are not antivirus or APK-signature verification. Analyze untrusted files in a safe environment.

Build and verify: `./gradlew :app:testDebugUnitTest :app:assembleDebug :app:bundleRelease` from this directory (requires Android SDK and JDK 17). Debug APK is debug-signed; release AAB is unsigned without a configured production keystore. Neither is a production Play Store submission. The package identifier changed to `com.brendigo.dexrive.inspector`; older installs under a different identifier will not receive this build as an in-place update.
