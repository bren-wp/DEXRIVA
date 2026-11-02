plugins {
	id("dexrive-java")
	id("dexrive-library")
	id("application")

	// use shadow only for application scripts, jar will be copied from dexrive-gui
	alias(libs.plugins.shadow)
}

dependencies {
	implementation(project(":dexrive-core"))
	implementation(project(":dexrive-plugins-tools"))
	implementation(project(":dexrive-commons:dexrive-app-commons"))
	implementation(project(":dexrive-commons:dexrive-analysis"))

	runtimeOnly(project(":dexrive-plugins:dexrive-dex-input"))
	runtimeOnly(project(":dexrive-plugins:dexrive-java-input"))
	runtimeOnly(project(":dexrive-plugins:dexrive-java-convert"))
	runtimeOnly(project(":dexrive-plugins:dexrive-smali-input"))
	runtimeOnly(project(":dexrive-plugins:dexrive-rename-mappings"))
	runtimeOnly(project(":dexrive-plugins:dexrive-kotlin-metadata"))
	runtimeOnly(project(":dexrive-plugins:dexrive-kotlin-source-debug-extension"))
	runtimeOnly(project(":dexrive-plugins:dexrive-xapk-input"))
	runtimeOnly(project(":dexrive-plugins:dexrive-aab-input"))
	runtimeOnly(project(":dexrive-plugins:dexrive-apkm-input"))
	runtimeOnly(project(":dexrive-plugins:dexrive-apks-input"))

	implementation(libs.jcommander)
	implementation(libs.logback.classic)
	implementation(libs.gson)
}

application {
	applicationName = "dexrive"
	mainClass.set("jadx.cli.JadxCLI")
	applicationDefaultJvmArgs =
		listOf(
			"-XX:+IgnoreUnrecognizedVMOptions",
			"-Xms256M",
			"-XX:MaxRAMPercentage=70.0",
			"-XX:ParallelGCThreads=3",
			// disable zip checks (#1962)
			"-Djdk.util.zip.disableZip64ExtraFieldValidation=true",
			// Foreign API access for 'directories' library (Windows only)
			"--enable-native-access=ALL-UNNAMED",
		)
	applicationDistribution.from("$rootDir") {
		include("README.md")
		include("NOTICE")
		include("LICENSE")
	}
}

tasks.shadowJar {
	// shadow jar not needed
	configurations = listOf()
}
