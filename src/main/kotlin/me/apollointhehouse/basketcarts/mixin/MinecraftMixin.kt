@file:Suppress("NonJavaMixin")

package me.apollointhehouse.basketcarts.mixin

import me.apollointhehouse.basketcarts.bus
import me.apollointhehouse.basketcarts.event.StartGameEvent
import net.minecraft.client.Minecraft
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(value = [Minecraft::class], remap = false)
class MinecraftMixin {
	@Inject(method = ["startGame"], at = [At("HEAD")])
	fun beforeStartGame(info: CallbackInfo) {
		val event = StartGameEvent.Before
		bus.post(event)
	}

	@Inject(method = ["startGame"], at = [At("TAIL")])
	fun afterStartGame(info: CallbackInfo) {
		val event = StartGameEvent.After
		bus.post(event)
	}
}
