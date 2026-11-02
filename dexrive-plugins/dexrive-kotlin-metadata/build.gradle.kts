plugins {
	id("dexrive-library")
	id("dexrive-kotlin")
}

dependencies {
	api(project(":dexrive-core"))

	implementation(libs.kotlin.metadata.jvm)

	testImplementation(
		project
			.project(":dexrive-core")
			.sourceSets
			.getByName("test")
			.output,
	)
	testImplementation(libs.commons.lang3)

	testRuntimeOnly(project(":dexrive-plugins:dexrive-smali-input"))
	testRuntimeOnly(project(":dexrive-plugins:dexrive-java-input"))
}
