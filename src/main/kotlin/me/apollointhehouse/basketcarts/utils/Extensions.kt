package me.apollointhehouse.basketcarts.utils

import me.apollointhehouse.basketcarts.mixin.EntityMinecartAccessor
import net.minecraft.core.entity.vehicle.EntityMinecart
import net.minecraft.core.item.ItemStack

var EntityMinecart.items: Array<ItemStack?>
	get() = (this as EntityMinecartAccessor).items
	set(value) { (this as EntityMinecartAccessor).items = value }
