package me.apollointhehouse.basketcarts.mixin.invokers

import net.minecraft.core.block.entity.TileEntityBasket
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Invoker

@Mixin(value = [TileEntityBasket::class], remap = false)
interface TileEntityBasketInvoker {
	@Invoker("calcNumUnitsInside")
	fun calcUnitsInside(): Int
}
