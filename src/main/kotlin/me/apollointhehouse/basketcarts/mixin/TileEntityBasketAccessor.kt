@file:Suppress("NonJavaMixin")

package me.apollointhehouse.basketcarts.mixin

import net.minecraft.core.block.entity.TileEntityBasket
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(value = [TileEntityBasket::class], remap = false)
interface TileEntityBasketAccessor {
	@set:Accessor
	@get:Accessor
	var numUnitsInside: Int
}
