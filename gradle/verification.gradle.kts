import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.testing.Test

// Standalone regression programs share the test runtime without a test framework.
val testSources = extensions.getByType<SourceSetContainer>().named("test")
val verificationPrograms = mapOf(
    "verifyWorldConfigCodec" to "eldritch.surge.network.WorldEnchantConfigPayloadTest",
    "verifyBloodyMaryLoot" to "eldritch.surge.network.BloodyMaryLootTest",
    "verifyBloodyMaryLootingChance" to "eldritch.surge.enchantment.mechanics.BloodyMaryLootingChanceTest",
    "verifyWeaponEnchantmentDamage" to "eldritch.surge.enchantment.WeaponEnchantmentDamageTest",
    "verifyEnchantmentResourceCatalog" to "eldritch.surge.enchantment.EnchantmentResourceCatalogTest",
    "verifyCreativeEnchantmentCategories" to "eldritch.surge.creative.CreativeEnchantmentCategoryTest",
    "verifyAdvancedEnchantingTableRecipe" to "eldritch.surge.enchantment.AdvancedEnchantingTableRecipeTest",
)

verificationPrograms.forEach { (taskName, entryPoint) ->
    val verification = tasks.register<JavaExec>(taskName) {
        group = "verification"
        dependsOn(tasks.named("testClasses"))
        classpath = testSources.get().runtimeClasspath
        mainClass.set(entryPoint)
    }
    tasks.named("check") { dependsOn(verification) }
}

tasks.withType<Test>().configureEach {
    failOnNoDiscoveredTests = false
}
