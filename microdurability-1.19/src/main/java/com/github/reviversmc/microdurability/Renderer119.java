package com.github.reviversmc.microdurability;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class Renderer119 extends RendererBase {
	@Override
	public void renderWarning(MatrixStack matrixStack, int x, int y) {
		RenderSystem.setShaderTexture(0, TEX);
		DrawableHelper.drawTexture(matrixStack, x, y, 0, 0, 3, 11);
		RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
	}

	@Override
	protected void disableRenderSystems() {
		RenderSystem.disableDepthTest();
		RenderSystem.disableBlend();
	}

	@Override
	protected void enableRenderSystems() {
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
	}

	@Override
	protected Iterable<ItemStack> getHandItems(ClientPlayerEntity player) {
		return player.getHandItems();
	}

	@Override
	@SuppressWarnings("checkstyle:SingleSpaceSeparator")
	protected void renderGuiQuad(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		buffer.vertex(x,         y,          0.0D).color(red, green, blue, alpha).next();
		buffer.vertex(x,         y + height, 0.0D).color(red, green, blue, alpha).next();
		buffer.vertex(x + width, y + height, 0.0D).color(red, green, blue, alpha).next();
		buffer.vertex(x + width, y,          0.0D).color(red, green, blue, alpha).next();
		BufferRenderer.drawWithGlobalProgram(buffer.end());
	}
}
