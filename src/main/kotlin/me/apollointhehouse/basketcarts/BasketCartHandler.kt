package me.apollointhehouse.basketcarts

import com.mojang.nbt.tags.CompoundTag
import com.mojang.nbt.tags.ListTag
import me.apollointhehouse.basketcarts.event.CartEvent
import me.apollointhehouse.basketcarts.utils.items
import me.apollointhehouse.raywire.api.EventHandler
import net.minecraft.core.block.Blocks
import net.minecraft.core.block.entity.TileEntityBasket
import net.minecraft.core.block.motion.CarriedBlock
import net.minecraft.core.item.ItemStack
import net.minecraft.core.player.inventory.container.Container

class BasketCartHandler {
	@EventHandler
	fun onInteract(event: CartEvent.Interact) {
		logger.info("Cart Interaction!")

		val cart = event.cart

		when (cart.type) {
			PASSENGER_CART -> passengerCartInteract(event)
			BASKET_CART -> basketCartInteract(event)
		}
	}

	fun basketCartInteract(event: CartEvent.Interact) {
		val cart = event.cart
		val player = event.player
		val world = cart.world

		if (cart.world?.isClientSide != false) return

		if (!player.isSneaking || player.inventory.getCurrentItem() != null || player.getHeldObject() != null) {
//					TODO("Deposit items in player inv")
			return
		}

		val entity = TileEntityBasket()
		if (entity !is Container) return

		for (slot in 0..<BASKET_CONTAINER_SIZE) {
			entity.setItem(slot, cart.getItem(slot))
			cart.setItem(slot, null)
		}

		entity.worldObj = null
		entity.carriedBlock = entity.getCarriedEntry(world, player, Blocks.BASKET, 0)
		player.setHeldObject(entity.carriedBlock)

		cart.type = 0.toByte()
		cart.meta = 0

		event.cancel()
	}

	fun passengerCartInteract(event: CartEvent.Interact) {
		val cart = event.cart
		val player = event.player
		val world = cart.world

		if (world?.isClientSide != false) return
		if (cart.passenger != null) return
		if (!player.isSneaking) return
		if (player.getHeldObject() !is CarriedBlock) return

		val carried = player.getHeldObject() as CarriedBlock
		val entity = carried.entity

		if (entity !is TileEntityBasket) return
		if (entity !is Container) return

		cart.type = BASKET_CART
		cart.meta = 0

		cart.items = arrayOfNulls(BASKET_CONTAINER_SIZE)

		for (i in 0..<BASKET_CONTAINER_SIZE) {
			val item = entity.getItem(i)
			if (item != null) logger.info("item: $item")
			cart.setItem(i, item)
			entity.setItem(i, null)
		}

		player.setHeldObject(null)
		event.cancel()
	}

	@EventHandler
	fun addSaveData(event: CartEvent.SaveData.Add) {
		val cart = event.cart
		val type = cart.type
		val items = cart.items
		val tag = event.tag

		if (type != BASKET_CART) return

		val list = ListTag()

		for (slot in items.indices) {
			val item = items[slot] ?: continue

			val tag1 = CompoundTag()
			tag1.putByte("Slot", slot.toByte())
			item.writeToNBT(tag1)
			list.addTag(tag1)
		}

		tag.put("Items", list)
	}

	@EventHandler
	fun readSaveData(event: CartEvent.SaveData.Read) {
		val cart = event.cart
		val type = cart.type
		val items = cart.items
		val tag = event.tag

		if (type != BASKET_CART) return

		val list = tag.getList("Items")
		cart.items = arrayOfNulls(BASKET_CONTAINER_SIZE)

		for (i in 0..<list.tagCount()) {
			val tag1 = list.tagAt(i) as CompoundTag
			val slot = tag1.getByte("Slot").toInt() and 255

			if (slot < items.size) {
				items[slot] = ItemStack.readItemStackFromNbt(tag1)
			}
		}
	}

	companion object {
		private const val BASKET_CART = 3.toByte()
		private const val PASSENGER_CART = 0.toByte()
		private const val BASKET_CONTAINER_SIZE = 1728
	}
}
