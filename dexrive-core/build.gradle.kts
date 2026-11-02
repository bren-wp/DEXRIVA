plugins {
	id("dexrive-library")
}

dependencies {
	api(project(":dexrive-plugins:dexrive-input-api"))
	api(project(":dexrive-commons:dexrive-zip"))

	implementation(libs.gson)

	testImplementation(project(":dexrive-plugins:dexrive-dex-input"))
	testImplementation(project(":dexrive-plugins:dexrive-smali-input"))
	testImplementation(project(":dexrive-plugins:dexrive-java-convert"))
	testImplementation(project(":dexrive-plugins:dexrive-java-input"))
	testImplementation(project(":dexrive-plugins:dexrive-raung-input"))

	testImplementation(libs.commons.lang3)
	testImplementation(libs.eclipse.jdt.ecj)
	testImplementation(libs.async.profiler)
}

val jadxTestJavaVersion = getTestJavaVersion()

fun getTestJavaVersion(): Int? {
	val envVarName = "DEXRIVE_TEST_JAVA_VERSION"
	val testJavaVer = (System.getenv(envVarName) ?: System.getenv("JADX_TEST_JAVA_VERSION"))?.toInt() ?: return null
	val currentJavaVer =
		java.toolchain.languageVersion
			.get()
			.asInt()
	if (testJavaVer < currentJavaVer) {
		throw GradleException("'$envVarName' can't be set to lower version than $currentJavaVer")
	}
	println("Set Java toolchain for core tests to version '$testJavaVer'")
	return testJavaVer
}

tasks.named<Test>("test") {
	jadxTestJavaVersion?.let { testJavaVer ->
		javaLauncher =
			javaToolchains.launcherFor {
				languageVersion = JavaLanguageVersion.of(testJavaVer)
			}
	}

	// disable cache to allow test's rerun,
	// because most tests are integration and depends on plugins and environment
	outputs.cacheIf { false }

	// exclude temp tests
	exclude("**/tmp/*")

	// maxHeapSize = "4g"
}

tasks.processResources {
	val jadxVersion = rootProject.extra["jadxVersion"] as String
	val jadxBundleType = project.findProperty("jadxBundleType") as String? ?: ""

	inputs.property("jadxVersion", jadxVersion)
	inputs.property("jadxBundleType", jadxBundleType)

	filesMatching("jadx-build-info.properties") {
		expand(
			mapOf(
				"jadxVersion" to jadxVersion,
				"jadxBundleType" to jadxBundleType,
			),
		)
	}
}
