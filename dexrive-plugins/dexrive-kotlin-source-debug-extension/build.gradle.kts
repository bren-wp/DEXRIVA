plugins {
	id("dexrive-library")
	id("dexrive-kotlin")
}

dependencies {
	api(project(":dexrive-core"))

	testImplementation(
		project
			.project(":dexrive-core")
			.sourceSets
			.getByName("test")
			.output,
	)
	testImplementation(libs.commons.lang3)

	testRuntimeOnly(project(":dexrive-plugins:dexrive-smali-input"))
}
