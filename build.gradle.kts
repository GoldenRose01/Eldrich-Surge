import java.io.File
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	id("net.fabricmc.fabric-loom")
	`maven-publish`
	id("org.jetbrains.kotlin.jvm") version "2.4.0"
}

version = providers.gradleProperty("mod_version").get()
group = providers.gradleProperty("maven_group").get()
val requestedMinecraftVersion = providers.gradleProperty("minecraft_version").get()
val minecraftVersion = Regex("^(\\d+\\.\\d+)\\.\\d+$")
	.matchEntire(requestedMinecraftVersion)
	?.groupValues?.get(1)
	?: requestedMinecraftVersion
val fabricApiVersions = mapOf(
	"26.1" to "0.145.1+26.1",
	"26.2" to "0.154.0+26.2",
	"26.3" to "0.161.0+26.3",
)
val configuredFabricApiVersion = providers.gradleProperty("fabric_api_version").get()
val fabricApiVersion = fabricApiVersions[minecraftVersion]
	?.takeIf { requestedMinecraftVersion != minecraftVersion && !configuredFabricApiVersion.endsWith("+$minecraftVersion") }
	?: if (configuredFabricApiVersion.endsWith("+$minecraftVersion")) configuredFabricApiVersion else fabricApiVersions[minecraftVersion] ?: configuredFabricApiVersion

base {
	archivesName.set("eldritch-surge-$minecraftVersion+")
}

repositories {
}

loom {
	splitEnvironmentSourceSets()

	mods {
		register("eldritch-surge") {
			sourceSet(sourceSets.main.get())
			sourceSet(sourceSets.getByName("client"))
		}
	}
}

fabricApi {
	configureDataGeneration {
		client = true
	}
}

val externalModDir = file(providers.gradleProperty("external_mod_dir").orElse("External_mod").get())
val externalRuntimeMods = fileTree(externalModDir) {
	include("*.jar")
	exclude("fabric-api-*.jar")
}
val enchlibProjectDir = file(providers.gradleProperty("enchlib_project_dir").orElse("../enchlib").get())
val enchlibVersion = providers.gradleProperty("enchlib_version").orElse("1.3.0").get()
val enchlibJar = enchlibProjectDir.resolve("jar/enchlib-mc${requestedMinecraftVersion}-$enchlibVersion.jar")

dependencies {
	// To change the versions see the gradle.properties file
	minecraft("com.mojang:minecraft:$minecraftVersion")
	implementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")

	// Fabric API must go through Loom so its access wideners are applied in dev.
	implementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

	compileOnly(fileTree(externalModDir) {
		include("*.jar")
		exclude("fabric-api-*.jar")
	})
	runtimeOnly(externalRuntimeMods)

	implementation("net.fabricmc:fabric-language-kotlin:${providers.gradleProperty("fabric_kotlin_version").get()}")
}

tasks.withType<JavaExec>().configureEach {
	if (name == "runClient" || name == "runServer") {
		doFirst {
			val mods = externalRuntimeMods.files
				.plus(enchlibJar.takeIf { it.isFile }?.let(::setOf).orEmpty())
				.sortedBy { it.name }
				.joinToString(File.pathSeparator) { it.absolutePath }

			if (mods.isNotBlank()) {
				jvmArgs("-Dfabric.addMods=$mods")
			}
		}
	}
}

tasks.processResources {
	val version = version
	inputs.property("version", version)
	inputs.property("minecraft_version", minecraftVersion)

	filesMatching("fabric.mod.json") {
		expand("version" to version, "minecraft_version" to minecraftVersion)
	}
}

tasks.withType<Jar>().configureEach {
	destinationDirectory.set(layout.projectDirectory.dir("Jar"))
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 25
}

kotlin {
	compilerOptions {
		jvmTarget = JvmTarget.JVM_25
	}
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

	from("LICENSE") {
		rename { "${it}_$projectName" }
	}
}

// configure the maven publication
publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}
