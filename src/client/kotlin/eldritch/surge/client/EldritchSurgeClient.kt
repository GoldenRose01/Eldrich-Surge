package eldritch.surge.client

import eldritch.surge.client.tooltip.EnchantmentTooltips
import net.fabricmc.api.ClientModInitializer
import eldritch.surge.config.EnchantmentCapsConfig
import eldritch.surge.network.WorldEnchantConfigPayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents

object EldritchSurgeClient : ClientModInitializer {
	override fun onInitializeClient() {
		EldritchScreens.initialize()
		EnchantmentTooltips.initialize()
		ClearmindZoom.initialize()
		ClientPlayNetworking.registerGlobalReceiver(WorldEnchantConfigPayload.TYPE) { payload, _ ->
			EnchantmentCapsConfig.applyClientWorldSnapshot(payload.json(), payload.editable())
		}
		ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
			EnchantmentCapsConfig.clearClientWorldSnapshot()
		}
	}
}
