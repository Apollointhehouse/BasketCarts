package me.apollointhehouse.basketcarts.utils

import me.apollointhehouse.basketcarts.duck.ObjectHolder
import me.apollointhehouse.basketcarts.mixin.accessors.EntityMinecartAccessor
import me.apollointhehouse.basketcarts.mixin.accessors.TileEntityBasketAccessor
import me.apollointhehouse.basketcarts.mixin.invokers.TileEntityBasketInvoker
import net.minecraft.core.block.entity.TileEntityBasket
import net.minecraft.core.entity.vehicle.EntityMinecart
import net.minecraft.core.item.ItemStack
import net.minecraft.core.world.ICarriable

var EntityMinecart.items: Array<ItemStack?>
	get() = (this as EntityMinecartAccessor).items
	set(value) { (this as EntityMinecartAccessor).items = value }

var EntityMinecart.heldObject: ICarriable?
	get() = (this as ObjectHolder).heldObject
	set(value) { (this as ObjectHolder).heldObject = value }

fun TileEntityBasket.calcUnitsInside(): Int =
	(this as TileEntityBasketInvoker).calcUnitsInside()

var TileEntityBasket.unitsInside: Int
	get() = (this as TileEntityBasketAccessor).numUnitsInside
	set(value) {  (this as TileEntityBasketAccessor).numUnitsInside = value }
