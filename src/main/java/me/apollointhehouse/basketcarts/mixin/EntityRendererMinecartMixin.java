package me.apollointhehouse.basketcarts.mixin;

import me.apollointhehouse.basketcarts.event.CartEvent;
import net.minecraft.client.render.entity.EntityRendererMinecart;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.vehicle.EntityMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.apollointhehouse.basketcarts.BasketCartsKt.bus;

@Mixin(value = EntityRendererMinecart.class, remap = false)
public class EntityRendererMinecartMixin {
	@Inject(method = "render*", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/vehicle/EntityMinecart;getType()B", ordinal = 1, shift = At.Shift.BEFORE))
	public void render(
		Tessellator tessellator,
		EntityMinecart minecart,
		double x, double y, double z,
		float yaw,
		float partialTick,
		CallbackInfo info
	) {
		CartEvent.Render event = new CartEvent.Render(tessellator, x, y, z, yaw, partialTick, minecart);
		bus.post(event);
	}
}
