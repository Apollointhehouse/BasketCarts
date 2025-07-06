@file:Suppress("NonJavaMixin")

package me.apollointhehouse.basketcarts.mixin.mixins

import me.apollointhehouse.basketcarts.bus
import me.apollointhehouse.basketcarts.event.CartEvent.Render
import net.minecraft.client.render.entity.EntityRendererMinecart
import net.minecraft.client.render.tessellator.Tessellator
import net.minecraft.core.entity.vehicle.EntityMinecart
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(value = [EntityRendererMinecart::class], remap = false)
class EntityRendererMinecartMixin {
    @Inject(
        method = ["render*"],
        at = [At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/entity/vehicle/EntityMinecart;getType()B",
            ordinal = 1,
            shift = At.Shift.BEFORE
        )]
    )
    fun render(
        tessellator: Tessellator,
        minecart: EntityMinecart,
        x: Double, y: Double, z: Double,
        yaw: Float,
        partialTick: Float,
        info: CallbackInfo
    ) {
        val event = Render(tessellator, x, y, z, yaw, partialTick, minecart)
        bus.post(event)
    }
}
