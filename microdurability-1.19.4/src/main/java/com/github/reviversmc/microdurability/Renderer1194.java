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
import net.minecraft.util.Identifier;

public class Renderer1194 extends RendererBase {
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
	protected void drawWarningTexture(Identifier texture, Object context, int x, int y, int u, int v, int width, int height) {
		RenderSystem.setShaderTexture(0, texture);
		DrawableHelper.drawTexture((MatrixStack) context, x, y, u, v, width, height);
		RenderSystem.setShaderTexture(0, DrawableHelper.GUI_ICONS_TEXTURE);
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
