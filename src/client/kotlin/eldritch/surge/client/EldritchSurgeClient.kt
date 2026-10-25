package eldritch.surge.client

import eldritch.surge.client.tooltip.EnchantmentTooltips
import net.fabricmc.api.ClientModInitializer
import eldritch.surge.config.EnchantmentCapsConfig
import eldritch.surge.network.WorldEnchantConfigPayload
import eldritch.surge.network.SoftFallingJumpPayload
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents

object EldritchSurgeClient : ClientModInitializer {
	override fun onInitializeClient() {
		EldritchScreens.initialize()
		EnchantmentTooltips.initialize()
		var jumpWasDown = false
		ClientTickEvents.END_CLIENT_TICK.register { client ->
			val jumpDown = client.options.keyJump.isDown
			val player = client.player
			if (player != null && jumpDown && !jumpWasDown && !player.onGround() && !player.isPassenger && !player.abilities.flying
				&& ClientPlayNetworking.canSend(SoftFallingJumpPayload.TYPE)) {
				ClientPlayNetworking.send(SoftFallingJumpPayload(true))
			}
			jumpWasDown = jumpDown
		}
		ClientPlayNetworking.registerGlobalReceiver(WorldEnchantConfigPayload.TYPE) { payload, _ ->
			EnchantmentCapsConfig.applyClientWorldSnapshot(payload.json(), payload.editable())
		}
		ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
			EnchantmentCapsConfig.clearClientWorldSnapshot()
		}
	}
}
