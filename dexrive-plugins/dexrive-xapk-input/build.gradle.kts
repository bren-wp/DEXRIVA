plugins {
	id("dexrive-library")
}

dependencies {
	api(project(":dexrive-core"))

	implementation(project(":dexrive-plugins:dexrive-dex-input"))
	implementation(libs.gson)
}
