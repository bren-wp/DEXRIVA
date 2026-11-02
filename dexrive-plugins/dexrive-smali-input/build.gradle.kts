plugins {
	id("dexrive-library")
}

dependencies {
	api(project(":dexrive-core"))

	implementation(project(":dexrive-plugins:dexrive-dex-input"))

	implementation(libs.smali) {
		exclude(group = "com.beust", module = "jcommander") // exclude old jcommander namespace
	}
	implementation(libs.guava.jre) // force the latest version for smali
}
