@file:Suppress("NonJavaMixin")

package me.apollointhehouse.basketcarts.mixin

import com.mojang.nbt.tags.CompoundTag
import me.apollointhehouse.basketcarts.bus
import me.apollointhehouse.basketcarts.event.CartEvent
import net.minecraft.core.entity.player.Player
import net.minecraft.core.entity.vehicle.EntityMinecart
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Suppress("KotlinConstantConditions")
@Mixin(value = [EntityMinecart::class], remap = false)
class EntityMinecartMixin {
    @Inject(method = ["interact"], at = [At("HEAD")], cancellable = true)
    fun interact(player: Player, info: CallbackInfoReturnable<Boolean>) {
        val event = CartEvent.Interact(this as Any as EntityMinecart, player)
        bus.post(event)

        if (event.isCancelled()) {
            info.setReturnValue(true)
        }
    }

	@Inject(method = ["addAdditionalSaveData"], at = [At("RETURN")])
	fun addAdditionalSaveData(tag: CompoundTag, info: CallbackInfo) {
		val event = CartEvent.SaveData.Add(tag, this as Any as EntityMinecart)
		bus.post(event)
	}

	@Inject(method = ["readAdditionalSaveData"], at = [At("RETURN")])
	fun readAdditionalSaveData(tag: CompoundTag, info: CallbackInfo) {
		val event = CartEvent.SaveData.Read(tag, this as Any as EntityMinecart)
		bus.post(event)
	}
}
