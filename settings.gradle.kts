pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
	}
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
	repositories {
		mavenCentral()
		// required for: aapt-proto, R8, Smali
		google()
	}
	repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version ("1.0.0")
}

if (!JavaVersion.current().isJava11Compatible) {
	throw GradleException("Jadx requires at least Java 11 for build (current version is '${JavaVersion.current()}')")
}

rootProject.name = "jadx"

include("dexrive-core")
include("dexrive-cli")
include("dexrive-gui-api")
include("dexrive-gui")

include("dexrive-plugins-tools")

include("dexrive-commons:dexrive-app-commons")
include("dexrive-commons:dexrive-zip")
include("dexrive-commons:dexrive-analysis")

include("dexrive-plugins:dexrive-input-api")
include("dexrive-plugins:dexrive-dex-input")
include("dexrive-plugins:dexrive-java-input")
include("dexrive-plugins:dexrive-raung-input")
include("dexrive-plugins:dexrive-smali-input")
include("dexrive-plugins:dexrive-java-convert")
include("dexrive-plugins:dexrive-rename-mappings")
include("dexrive-plugins:dexrive-kotlin-metadata")
include("dexrive-plugins:dexrive-kotlin-source-debug-extension")
include("dexrive-plugins:dexrive-xapk-input")
include("dexrive-plugins:dexrive-aab-input")
include("dexrive-plugins:dexrive-apkm-input")
include("dexrive-plugins:dexrive-apks-input")
