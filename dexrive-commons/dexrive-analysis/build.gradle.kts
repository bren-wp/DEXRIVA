plugins {
	id("dexrive-library")
}

dependencies {
	implementation(project(":dexrive-core"))

	implementation(libs.gson)

	testRuntimeOnly(project(":dexrive-plugins:dexrive-dex-input"))
	testRuntimeOnly(project(":dexrive-plugins:dexrive-smali-input"))
}
