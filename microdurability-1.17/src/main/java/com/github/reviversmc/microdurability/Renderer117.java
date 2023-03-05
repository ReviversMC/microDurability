package com.github.reviversmc.microdurability;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.item.ItemStack;

public class Renderer117 extends RendererBase {
	@SuppressWarnings("checkstyle:SingleSpaceSeparatorCheck")
	@Override
	protected void renderGuiQuad(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		buffer.vertex(x,         y,          0.0D).color(red, green, blue, alpha).next();
		buffer.vertex(x,         y + height, 0.0D).color(red, green, blue, alpha).next();
		buffer.vertex(x + width, y + height, 0.0D).color(red, green, blue, alpha).next();
		buffer.vertex(x + width, y,          0.0D).color(red, green, blue, alpha).next();
		buffer.end();
		BufferRenderer.draw(buffer);
	}

	@Override
	public Iterable<ItemStack> getHandItems() {
		return mc.player.getItemsHand();
	}
}
