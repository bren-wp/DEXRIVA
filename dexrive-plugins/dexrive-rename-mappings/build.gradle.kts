plugins {
	id("dexrive-library")
}

dependencies {
	api(project(":dexrive-core"))

	api(libs.mapping.io) {
		exclude("org.ow2.asm:asm")
		exclude("net.fabricmc:tiny-remapper")
	}

	testRuntimeOnly(project(":dexrive-plugins:dexrive-dex-input"))
	testRuntimeOnly(project(":dexrive-plugins:dexrive-smali-input"))
}
