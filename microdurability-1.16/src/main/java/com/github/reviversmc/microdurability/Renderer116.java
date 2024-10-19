package com.github.reviversmc.microdurability;

import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;

import com.github.reviversmc.microdurability.compat.mods.RaisedCompat;

/**
 * See {@link ItemRenderer#renderGuiItemOverlay(TextRenderer, ItemStack, int, int, String)}.
 */
public class Renderer116 extends Renderer {
	// Of type Object to prevent crashes on later MC versions where DrawableHelper doesn't exist
	private Object drawableHelper;

	void init() {
		drawableHelper = new DrawableHelper() { };
	}

	@Override
	protected boolean hasMending(ItemStack stack) {
		return EnchantmentHelper.getLevel(Enchantments.MENDING, stack) > 0;
	}

	@Override
	protected void drawWarningTexture(Identifier texture, Object context, int x, int y, int u, int v, int width, int height) {
		mc.getTextureManager().bindTexture(texture);
		((DrawableHelper) drawableHelper).drawTexture((MatrixStack) context, x, y, u, v, width, height);
		mc.getTextureManager().bindTexture(DrawableHelper.GUI_ICONS_TEXTURE);
	}

	@Override
	protected int getItemBarStep(ItemStack stack) {
		float damage = stack.getDamage();
		float maxDamage = stack.getMaxDamage();

		return Math.round(13F - damage * 13F / maxDamage);
	}

	@Override
	protected int getItemBarColor(ItemStack stack) {
		float damage = stack.getDamage();
		float maxDamage = stack.getMaxDamage();
		float fraction = Math.max(0.0F, maxDamage - damage) / maxDamage;

		return MathHelper.hsvToRgb(fraction / 3.0F, 1.0F, 1.0F);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void preRenderGuiQuads() {
		RenderSystem.disableDepthTest();
		RenderSystem.disableTexture();
		RenderSystem.disableAlphaTest();
		RenderSystem.disableBlend();
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void postRenderGuiQuads() {
		RenderSystem.enableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableTexture();
		RenderSystem.enableDepthTest();
	}

	/**
	 * See {@link DrawableHelper#drawTexturedQuad(Matrix4f, int, int, int, int, int, float, float, float, float)}.
	 */
	@Override
	@SuppressWarnings("checkstyle:SingleSpaceSeparator")
	protected void renderGuiQuad(Object context, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
		if (!RaisedCompat.isV4OrLater()) {
			y += getRaisedOffset();
		}

		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
		buffer.vertex(x,         y,          0.0D).color(red, green, blue, alpha).next();
		buffer.vertex(x,         y + height, 0.0D).color(red, green, blue, alpha).next();
		buffer.vertex(x + width, y + height, 0.0D).color(red, green, blue, alpha).next();
		buffer.vertex(x + width, y,          0.0D).color(red, green, blue, alpha).next();
		buffer.end();
		BufferRenderer.draw(buffer);
	}
}
