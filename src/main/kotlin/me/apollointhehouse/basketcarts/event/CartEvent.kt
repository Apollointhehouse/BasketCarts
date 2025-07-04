package me.apollointhehouse.basketcarts.event

import com.mojang.nbt.tags.CompoundTag
import me.apollointhehouse.raywire.api.Cancellable
import me.apollointhehouse.raywire.api.Event
import net.minecraft.core.entity.player.Player
import net.minecraft.core.entity.vehicle.EntityMinecart

sealed class CartEvent(val cart: EntityMinecart) : Event {
	class Interact(cart: EntityMinecart, val player: Player) :
		CartEvent(cart),
		Cancellable by Cancellable()

	class Remove(cart: EntityMinecart, val player: Player) : Event

	sealed class SaveData(
		val tag: CompoundTag,
		cart: EntityMinecart
	) : CartEvent(cart) {
		class Add(
			tag: CompoundTag,
			cart: EntityMinecart
		) : SaveData(tag, cart)

		class Read(
			tag: CompoundTag,
			cart: EntityMinecart
		) : SaveData(tag, cart)
	}
}
