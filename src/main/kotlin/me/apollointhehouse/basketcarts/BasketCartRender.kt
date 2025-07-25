package me.apollointhehouse.basketcarts

import me.apollointhehouse.basketcarts.BasketCarts.BASKET_CART
import me.apollointhehouse.basketcarts.event.CartEvent
import me.apollointhehouse.basketcarts.utils.heldObject
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
import net.minecraft.core.block.motion.CarriedBlock
import net.minecraft.core.util.helper.MathHelper
import net.minecraft.core.world.BlocksContainer
import org.lwjgl.opengl.GL11

class BasketCartRender {
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
}
