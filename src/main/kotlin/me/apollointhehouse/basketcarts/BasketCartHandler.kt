package me.apollointhehouse.basketcarts

import com.mojang.nbt.tags.CompoundTag
import me.apollointhehouse.basketcarts.event.CartEvent
import me.apollointhehouse.basketcarts.utils.calcUnitsInside
import me.apollointhehouse.basketcarts.utils.heldObject
import me.apollointhehouse.basketcarts.utils.items
import me.apollointhehouse.basketcarts.utils.unitsInside
import me.apollointhehouse.raywire.api.EventHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.render.Lighting
import net.minecraft.client.render.RenderBlocks
import net.minecraft.client.render.TileEntityRenderDispatcher
import net.minecraft.client.render.block.model.BlockModel
import net.minecraft.client.render.block.model.BlockModelDispatcher
import net.minecraft.client.render.tessellator.Tessellator
import net.minecraft.client.render.texture.stitcher.TextureRegistry
import net.minecraft.core.block.Blocks
import net.minecraft.core.block.entity.TileEntityBasket
import net.minecraft.core.block.motion.CarriedBlock
import net.minecraft.core.util.helper.MathHelper
import net.minecraft.core.world.BlocksContainer
import net.minecraft.core.world.ICarriable
import org.lwjgl.opengl.GL11

class BasketCartHandler {
	@EventHandler
	fun onTick(event: CartEvent.Tick.Post) {
		val cart = event.cart
		val world = cart.world ?: return
		cart.heldObject?.heldTick(world, cart)

		val carried = cart.heldObject as? CarriedBlock ?: return
		val entity = carried.entity as? TileEntityBasket ?: return

		entity.unitsInside = entity.calcUnitsInside()
	}

	@EventHandler
	fun onInteract(event: CartEvent.Interact) {
		logger.info("Cart Interaction!")

		val cart = event.cart

		when (cart.type) {
			PASSENGER_CART -> passengerCartInteract(event)
			BASKET_CART -> basketCartInteract(event)
		}
	}

	fun basketCartInteract(event: CartEvent.Interact) {
		val cart = event.cart
		val player = event.player

		if (cart.world?.isClientSide != false) return

		if (!player.isSneaking || player.inventory.getCurrentItem() != null || player.getHeldObject() != null) {
//					TODO("Deposit items in player inv")
			return
		}

		val carried = cart.heldObject as? CarriedBlock ?: return
		val entity = carried.entity as? TileEntityBasket ?: return
		entity.unitsInside = entity.calcUnitsInside()

		player.setHeldObject(carried)
		carried.holder = player
		cart.heldObject = null

		cart.type = 0.toByte()
		cart.meta = 0
		cart.items = arrayOfNulls(cart.containerSize)

		event.cancel()
	}

	fun passengerCartInteract(event: CartEvent.Interact) {
		val cart = event.cart
		val player = event.player
		val world = cart.world

		if (world?.isClientSide != false) return
		if (cart.passenger != null) return
		if (!player.isSneaking) return

		val carried = player.getHeldObject() as? CarriedBlock ?: return
		if (carried.entity !is TileEntityBasket) return

		cart.type = BASKET_CART
		cart.meta = 0
		cart.items = arrayOfNulls(cart.containerSize)

		cart.heldObject = carried
		carried.holder = cart

		player.setHeldObject(null)
		event.cancel()
	}

	@EventHandler
	fun render(event: CartEvent.Render) {
		val cart = event.cart
		val tessellator = event.tessellator
		val delta = event.delta
		val mc = Minecraft.getMinecraft()
		val world = mc.currentWorld ?: return

		if (cart.type != BASKET_CART) return
		val carriedBlock = cart.heldObject as? CarriedBlock ?: return

		val entity = carriedBlock.entity ?: return

		val container = BlocksContainer(world)
		val containerRenderBlock = RenderBlocks(container)

		val block = Blocks.BASKET

		TextureRegistry.blockAtlas.bind()
		Lighting.disable()
		GL11.glPushMatrix()
		GL11.glBlendFunc(770, 771)
		GL11.glEnable(3042)
		GL11.glDisable(2884)
		if (mc.isAmbientOcclusionEnabled) {
			GL11.glShadeModel(7425)
		} else {
			GL11.glShadeModel(7424)
		}

		GL11.glScalef(1f, 1f, 1f)
		GL11.glTranslatef(0.0f, -0.75f, -0.75f)
		val blockX = MathHelper.floor(cart.x)
		val blockY = MathHelper.floor(cart.y) + 1
		val blockZ = MathHelper.floor(cart.z)
		BlockModel.setRenderBlocks(containerRenderBlock)
		tessellator.startDrawingQuads()
		tessellator.setTranslation((-blockX).toDouble() - 0.5, (-blockY).toDouble() + 0.25, (-blockZ).toDouble() + 0.25)
		container.setLightReferenceEntity(cart)
		container.setBlock(
			blockX,
			blockY,
			blockZ,
			block.id(),
			0,
			entity
		)
		BlockModelDispatcher.getInstance().getDispatch(block).renderNoCulling(
			Tessellator.instance,
			blockX,
			blockY,
			blockZ
		)
		tessellator.draw()
		tessellator.setTranslation(0.0, 0.0, 0.0)
		container.setLightReferenceEntity(cart)
		container.clear()
		val renderer = TileEntityRenderDispatcher.instance.getRenderer(entity)
		if (renderer != null) {
			entity.worldObj = world
			renderer.doRender(tessellator, entity, -0.5, -0.5, -0.5, delta)
			entity.worldObj = null
		}

		GL11.glPopMatrix()
		GL11.glEnable(2896)
		GL11.glEnable(16384)
		GL11.glEnable(16385)
		GL11.glEnable(2903)
	}

	@EventHandler
	fun addSaveData(event: CartEvent.SaveData.Add) {
		val cart = event.cart
		val type = cart.type
		val tag = event.tag

		if (type != BASKET_CART) return

		val heldObject = cart.heldObject ?: return

		val heldTag = CompoundTag()
		heldObject.writeToNBT(heldTag)
		tag.put("HeldObject", heldTag)
	}

	@EventHandler
	fun readSaveData(event: CartEvent.SaveData.Read) {
		val cart = event.cart
		val type = cart.type
		val tag = event.tag

		if (type != BASKET_CART) return

		if (tag.containsKey("HeldObject")) {
			val heldTag = tag.getCompound("HeldObject")
			cart.heldObject = ICarriable.createAndLoadCarriable(cart, heldTag)
		}
	}

	companion object {
		private const val BASKET_CART = 3.toByte()
		private const val PASSENGER_CART = 0.toByte()
	}
}
