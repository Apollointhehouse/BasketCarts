package me.apollointhehouse.basketcarts

import me.apollointhehouse.raywire.api.Bus
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import turniplabs.halplibe.util.GameStartEntrypoint
import turniplabs.halplibe.util.RecipeEntrypoint

const val MOD_ID: String = "basketcarts"
@JvmField internal val logger: Logger = LoggerFactory.getLogger(MOD_ID)
@JvmField internal val bus = Bus()

object BasketCarts : ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
	override fun onInitialize() {
		logger.info("BasketCarts initialized.")
	}

	override fun onRecipesReady() {

	}

	override fun initNamespaces() {

	}

	override fun beforeGameStart() {

	}

	override fun afterGameStart() {
		bus.subscribe(BasketCartHandler())
	}
}
