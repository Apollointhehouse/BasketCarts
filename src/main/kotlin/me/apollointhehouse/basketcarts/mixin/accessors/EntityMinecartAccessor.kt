@file:Suppress("NonJavaMixin")

package me.apollointhehouse.basketcarts.mixin.accessors

import net.minecraft.core.entity.vehicle.EntityMinecart
import net.minecraft.core.item.ItemStack
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(value = [EntityMinecart::class], remap = false)
interface EntityMinecartAccessor {
    @get:Accessor
    @set:Accessor
    var items: Array<ItemStack?>
}
