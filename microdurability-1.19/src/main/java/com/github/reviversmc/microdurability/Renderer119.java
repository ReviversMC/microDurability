package com.github.reviversmc.microdurability;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;

/**
 * See {@link ItemRenderer#renderGuiItemOverlay(TextRenderer, ItemStack, int, int, String)}.
 */
public class Renderer119 extends Renderer117 {
	@Override
	@SuppressWarnings("checkstyle:SingleSpaceSeparator")
	protected void renderGuiQuad(Object context, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
		y += getRaisedOffset();

		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		buffer.vertex(x,         y,          0.0D).color(red, green, blue, alpha).next();
		buffer.vertex(x,         y + height, 0.0D).color(red, green, blue, alpha).next();
		buffer.vertex(x + width, y + height, 0.0D).color(red, green, blue, alpha).next();
		buffer.vertex(x + width, y,          0.0D).color(red, green, blue, alpha).next();
		BufferRenderer.drawWithShader(buffer.end());
	}
}
