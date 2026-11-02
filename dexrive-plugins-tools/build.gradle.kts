plugins {
	id("dexrive-java")
	id("dexrive-library")
}

dependencies {
	api(project(":dexrive-core"))

	implementation(project(":dexrive-commons:dexrive-app-commons"))

	implementation(libs.gson)
	implementation(libs.commons.io)

	testImplementation(libs.mockwebserver)
}
