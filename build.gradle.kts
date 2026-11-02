import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.LineEnding
import nl.littlerobots.vcu.plugin.resolver.VersionSelectors
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
	id("nl.littlerobots.version-catalog-update") version "1.1.1"
	id("com.diffplug.spotless") version "8.9.0"
}

val jadxEnv = loadEnv(file("$rootDir/.env"))

val jadxVersion = jadxEnv["DEXRIVE_VERSION"] ?: jadxEnv["JADX_VERSION"] ?: "dev"
extra.set("jadxVersion", jadxVersion)
println("DEXRIVE version: $jadxVersion")
version = jadxVersion

val jadxBuildJavaVersion = getBuildJavaVersion()
extra.set("jadxBuildJavaVersion", jadxBuildJavaVersion)

fun getBuildJavaVersion(): Int? {
	val envVarName = "DEXRIVE_BUILD_JAVA_VERSION"
	val buildJavaVer = (jadxEnv[envVarName] ?: jadxEnv["JADX_BUILD_JAVA_VERSION"])?.toInt() ?: return null
	if (buildJavaVer < 11) {
		throw GradleException("'$envVarName' can't be set to lower than 11")
	}
	println("Set Java toolchain for DEXRIVE build to version '$buildJavaVer'")
	return buildJavaVer
}

allprojects {
	apply(plugin = "java")
	apply(plugin = "checkstyle")
	apply(plugin = "com.diffplug.spotless")

	configure<SpotlessExtension> {
		java {
			importOrderFile("$rootDir/config/code-formatter/eclipse.importorder")
			eclipse().configFile("$rootDir/config/code-formatter/eclipse.xml")
			removeUnusedImports()
			commonFormatOptions()
		}
		kotlin {
			ktlint().editorConfigOverride(mapOf("indent_style" to "tab"))
			commonFormatOptions()
		}
		kotlinGradle {
			ktlint()
			commonFormatOptions()
		}
		format("misc") {
			target("**/*.gradle", "**/*.xml", "**/.gitignore", "**/.properties")
			targetExclude(".gradle/**", ".idea/**", "*/build/**")
			commonFormatOptions()
		}
	}
}

fun FormatExtension.commonFormatOptions() {
	lineEndings = LineEnding.UNIX
	encoding = Charsets.UTF_8
	trimTrailingWhitespace()
	endWithNewline()
}

versionCatalogUpdate {
	sortByKey = true
	versionSelector(VersionSelectors.STABLE)
	keep {
		keepUnusedVersions = true
	}
}

fun loadEnv(file: File): Map<String, String> {
	val envMap = HashMap<String, String>()
	System
		.getenv()
		.filter { it.key.startsWith("JADX_") || it.key.startsWith("DEXRIVE_") }
		.forEach { envMap[it.key] = it.value }
	if (file.exists()) {
		file
			.readLines()
			.map { it.trim() }
			.filter { it.isNotEmpty() && !it.startsWith("#") }
			.forEach {
				val (k, v) = it.split("=", limit = 2)
				envMap[k.trim()] = v.trim()
			}
	}
	println(
		"Loaded env vars (${envMap.size}):\n${
			envMap.toList().sortedBy { it.first }.joinToString(separator = "\n") { "${it.first}=${it.second}" }
		}\n",
	)
	return envMap
}

val distWinConfiguration =
	configurations.create("distWinConfiguration") {
		isCanBeConsumed = false
	}
val distWinWithJreConfiguration =
	configurations.create("distWinWithJreConfiguration") {
		isCanBeConsumed = false
	}
val distMacConfiguration =
	configurations.create("distMacConfiguration") {
		isCanBeConsumed = false
	}
dependencies {
	distWinConfiguration(project(":dexrive-gui", "distWinConfiguration"))
	distWinWithJreConfiguration(project(":dexrive-gui", "distWinWithJreConfiguration"))
	distMacConfiguration(project(":dexrive-gui", "distMacConfiguration"))
}

val copyArtifacts =
	tasks.register<Copy>("copyArtifacts") {
		val jarCliPattern = "dexrive-cli-(.*)-all.jar".toPattern()
		from(tasks.getByPath(":dexrive-cli:installShadowDist")) {
			exclude("**/*.jar")
			filter { line ->
				jarCliPattern
					.matcher(line)
					.replaceAll("dexrive-$1-all.jar")
					.replace("-jar \"\\\"\$CLASSPATH\\\"\"", "-cp \"\\\"\$CLASSPATH\\\"\" jadx.cli.JadxCLI")
					.replace("-jar \"%CLASSPATH%\"", "-cp \"%CLASSPATH%\" jadx.cli.JadxCLI")
			}
		}
		val jarGuiPattern = "dexrive-gui-(.*)-all.jar".toPattern()
		from(tasks.getByPath(":dexrive-gui:installShadowDist")) {
			exclude("**/*.jar")
			filter { line -> jarGuiPattern.matcher(line).replaceAll("dexrive-$1-all.jar") }
		}
		from(tasks.getByPath(":dexrive-gui:installShadowDist")) {
			include("**/*.jar")
			rename("dexrive-gui-(.*)-all.jar", "dexrive-$1-all.jar")
		}
		from(layout.projectDirectory) {
			include("README.md")
			include("LICENSE")
		}
		into(layout.buildDirectory.dir("dexrive"))
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	}

val pack =
	tasks.register<Zip>("pack") {
		from(copyArtifacts)
		archiveFileName.set("dexrive-$jadxVersion.zip")
		destinationDirectory.set(layout.buildDirectory)
		eachFile {
			if (path == "bin/jadx" || path == "bin/dexrive-gui") {
				permissions {
					unix("rwxr-xr-x")
				}
			}
		}
	}

val distWin =
	tasks.register<Zip>("distWin") {
		group = "dexrive"
		description = "Build Windows bundle"

		from(distWinConfiguration)

		destinationDirectory.set(layout.buildDirectory.dir("distWin"))
		archiveFileName.set("dexrive-gui-$jadxVersion-win.zip")
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	}

val distWinWithJre =
	tasks.register<Zip>("distWinWithJre") {
		description = "Build Windows with JRE bundle"

		from(distWinWithJreConfiguration)

		destinationDirectory.set(layout.buildDirectory.dir("distWinWithJre"))
		archiveFileName.set("dexrive-gui-$jadxVersion-with-jre-win.zip")
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	}

val distMac =
	tasks.register<Copy>("distMac") {
		group = "jadx"
		description = "Build macOS DMG bundle (with bundled JRE)"

		from(distMacConfiguration)

		into(layout.buildDirectory.dir("distMac"))
	}

val dist =
	tasks.register("dist") {
		group = "jadx"
		description = "Build jadx distribution zip bundles"

		dependsOn(pack)

		val os = DefaultNativePlatform.getCurrentOperatingSystem()
		if (os.isWindows) {
			if (project.hasProperty("bundleJRE")) {
				println("Build win bundle with JRE")
				dependsOn(distWinWithJre)
			} else {
				dependsOn(distWin)
			}
		} else if (os.isMacOsX) {
			dependsOn(distMac)
		}
	}

val cleanBuildDir =
	tasks.register<Delete>("cleanBuildDir") {
		delete(layout.buildDirectory)
	}
tasks.getByName("clean").dependsOn(cleanBuildDir)
