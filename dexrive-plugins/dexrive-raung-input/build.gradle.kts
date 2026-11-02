plugins {
	id("dexrive-library")
}

dependencies {
	api(project(":dexrive-core"))

	implementation(libs.raung.asm)
}
