package com.github.reviversmc.microdurability;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import com.github.reviversmc.microdurability.compat.mods.RaisedCompat;

/**
 * See {@link ItemRenderer#renderGuiItemOverlay(MatrixStack, TextRenderer, ItemStack, int, int, String)}.
 */
public class Renderer1194 extends Renderer119 {
	@Override
	protected void drawWarningTexture(Identifier texture, Object context, int x, int y, int u, int v, int width, int height) {
		RenderSystem.setShaderTexture(0, texture);
		DrawableHelper.drawTexture((MatrixStack) context, x, y, u, v, width, height);
		RenderSystem.setShaderTexture(0, DrawableHelper.GUI_ICONS_TEXTURE);
	}

	@Override
	protected void preRenderGuiQuads() {
		RenderSystem.disableDepthTest();
		RenderSystem.disableBlend();
	}

	@Override
	protected void postRenderGuiQuads() {
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
	}

	@Override
	@SuppressWarnings("checkstyle:SingleSpaceSeparator")
	protected void renderGuiQuad(Object context, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
		if (RaisedCompat.isInstalled() && !RaisedCompat.isV3OrLater()) {
			y += getRaisedOffset();
		}

		DrawableHelper.fill((MatrixStack) context, x, y, x + width, y + height, ColorHelper.Argb.getArgb(alpha, red, green, blue));
	}
}
