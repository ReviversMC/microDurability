package com.github.reviversmc.microdurability;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

/**
 * See {@link DrawContext#drawItemInSlot(TextRenderer, ItemStack, int, int, String)}.
 */
public class Renderer120 extends Renderer1194 {
	@Override
	protected void drawWarningTexture(Identifier texture, Object context, int x, int y, int u, int v, int width, int height) {
		((DrawContext) context).drawTexture(texture, x, y, u, v, width, height);
	}

	@Override
	protected void preRenderGuiQuads() {
	}

	@Override
	protected void postRenderGuiQuads() {
	}

	@Override
	protected void renderGuiQuad(Object context, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
		((DrawContext) context).fill(RenderLayer.getGuiOverlay(), x, y, x + width, y + height, ColorHelper.Argb.getArgb(alpha, red, green, blue));
	}
}
