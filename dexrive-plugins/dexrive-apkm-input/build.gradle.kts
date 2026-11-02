plugins {
	id("dexrive-library")
	id("dexrive-kotlin")
}

dependencies {
	api(project(":dexrive-core"))

	implementation(project(":dexrive-plugins:dexrive-dex-input"))
	implementation(libs.gson)
}
