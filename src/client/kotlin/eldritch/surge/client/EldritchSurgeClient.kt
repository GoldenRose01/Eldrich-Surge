package eldritch.surge.client

import eldritch.surge.client.tooltip.EnchantmentTooltips
import net.fabricmc.api.ClientModInitializer

object EldritchSurgeClient : ClientModInitializer {
	override fun onInitializeClient() {
		EldritchScreens.initialize()
		EnchantmentTooltips.initialize()
	}
}
