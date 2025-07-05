package me.apollointhehouse.basketcarts.utils

import me.apollointhehouse.basketcarts.duck.Holder
import me.apollointhehouse.basketcarts.mixin.EntityMinecartAccessor
import me.apollointhehouse.basketcarts.mixin.TileEntityBasketAccessor
import me.apollointhehouse.basketcarts.mixin.TileEntityBasketInvoker
import net.minecraft.core.block.entity.TileEntityBasket
import net.minecraft.core.entity.vehicle.EntityMinecart
import net.minecraft.core.item.ItemStack
import net.minecraft.core.world.ICarriable

var EntityMinecart.items: Array<ItemStack?>
	get() = (this as EntityMinecartAccessor).items
	set(value) { (this as EntityMinecartAccessor).items = value }

var EntityMinecart.heldObject: ICarriable?
	get() = (this as Holder).heldObject
	set(value) { (this as Holder).heldObject = value }

fun TileEntityBasket.calcUnitsInside(): Int =
	(this as TileEntityBasketInvoker).calcUnitsInside()

var TileEntityBasket.unitsInside: Int
	get() = (this as TileEntityBasketAccessor).numUnitsInside
	set(value) {  (this as TileEntityBasketAccessor).numUnitsInside = value }
