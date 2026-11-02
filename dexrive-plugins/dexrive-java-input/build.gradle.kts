plugins {
	id("dexrive-library")
}

dependencies {
	api(project(":dexrive-core"))

	// show bytecode disassemble
	implementation(libs.raung.disasm)

	testImplementation(project(":dexrive-core"))
}
