package me.apollointhehouse.basketcarts.duck

import net.minecraft.core.block.motion.CarriedBlock
import net.minecraft.core.world.ICarriable

interface Holder {
	var heldObject: ICarriable?
}
