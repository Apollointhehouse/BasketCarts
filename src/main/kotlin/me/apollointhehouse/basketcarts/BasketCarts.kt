package me.apollointhehouse.basketcarts

import me.apollointhehouse.basketcarts.event.StartGameEvent
import me.apollointhehouse.raywire.api.Bus
import me.apollointhehouse.raywire.api.EventHandler
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

const val MOD_ID: String = "basketcarts"
@JvmField internal val logger: Logger = LoggerFactory.getLogger(MOD_ID)
@JvmField internal val bus = Bus()

object BasketCarts : ModInitializer {
	override fun onInitialize() {
		logger.info("BasketCarts initialized.")

		bus.subscribe(this)
	}

	@EventHandler
	fun afterGameStart(event: StartGameEvent.After) {
		bus.subscribe(BasketCartHandler())
	}
}
