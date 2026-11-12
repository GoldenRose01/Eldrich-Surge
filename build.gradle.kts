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

// Snapshot 26.4 adds an explicit section to inventory slot constructors.
if (minecraftVersion.startsWith("26.4")) {
	val snapshotSources = tasks.register<Sync>("prepareSnapshotJava") {
		from("src/main/java")
		into(layout.buildDirectory.dir("generated/snapshotJava"))
		filter { line: String ->
			line.replace("new Slot(inputs, 0, 15, 40)", "new Slot(inputs, 0, 15, 40, Slot.Section.INPUT)")
				.replace("new Slot(inputs, 1, 15, 76)", "new Slot(inputs, 1, 15, 76, Slot.Section.SECOND_INPUT)")
		}
	}
	sourceSets.main { java.setSrcDirs(listOf(snapshotSources)) }
}

base {
	archivesName.set("eldrich-surge-$minecraftVersion+")
}

loom {
	splitEnvironmentSourceSets()

	mods {
		register("eldrich-surge") {
			sourceSet(sourceSets.main.get())
			sourceSet(sourceSets.getByName("client"))
		}
	}
}

if (minecraftVersion.startsWith("26.4")) {
	val snapshotClientSources = tasks.register<Sync>("prepareSnapshotClientJava") {
		from("src/client/java")
		into(layout.buildDirectory.dir("generated/snapshotClientJava"))
		filter { line: String ->
			line.replace("button.visible = index < filtered.size();", "button.setVisible(index < filtered.size());")
				.replace("@Shadow protected Slot hoveredSlot;", "@Shadow protected abstract net.minecraft.client.gui.components.inventory.SlotWidget getHoveredSlot();")
				.replace("return hoveredSlot;", "return getHoveredSlot() == null ? null : getHoveredSlot().slot();")
				.replace("!button.visible", "!button.isVisible()")
				.replace("GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick", "GuiGraphicsExtractor graphics")
				.replace("GuiGraphicsExtractor graphics, int mouseX, int mouseY)", "GuiGraphicsExtractor graphics)")
				.replace("super.extractContents(graphics, mouseX, mouseY, partialTick)", "super.extractContents(graphics)")
				.replace("extractContents(GuiGraphicsExtractor graphics) {", "extractContents(GuiGraphicsExtractor graphics) { int mouseX = graphics.mouseX(), mouseY = graphics.mouseY();")
				.replace("GuiGraphicsExtractor graphics, CallbackInfo ci) {", "GuiGraphicsExtractor graphics, CallbackInfo ci) { int mouseX = graphics.mouseX(), mouseY = graphics.mouseY();")
				.replace("import net.minecraft.client.renderer.RenderPipelines;", "import net.minecraft.client.gui.GuiPipeline;")
				.replace("RenderPipelines.GUI_TEXTURED", "GuiPipeline.TEXTURED")
				.replace("MenuScreens.register(", "MenuScreens.register((net.minecraft.core.WritableRegistry) net.minecraft.client.core.registries.BuiltInClientRegistries.MENU_SCREEN, ")
		}
	}
	sourceSets.named("client") { java.setSrcDirs(listOf(snapshotClientSources)) }
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
val enchlibVersion = providers.gradleProperty("enchlib_version").orElse("1.4.0").get()
val enchlibJar = enchlibProjectDir.resolve("jar/enchlib-mc${requestedMinecraftVersion}-$enchlibVersion.jar")

dependencies {
	// To change the versions see the gradle.properties file
	minecraft("com.mojang:minecraft:$minecraftVersion")
	implementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")

	// Fabric API must go through Loom so its access wideners are applied in dev.
	implementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

	compileOnly(externalRuntimeMods)
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
	// Include sources in the build artifacts.
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

publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

}

apply(from = "gradle/verification.gradle.kts")
