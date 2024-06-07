package com.github.reviversmc.microdurability;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class Renderer implements HudRenderCallback {
	private static final Identifier TEX = new Identifier("microdurability", "textures/gui/icons.png");
	private final MinecraftClient mc;
	private float time = 0;

	Renderer() {
		mc = MinecraftClient.getInstance();
	}

	@Override
	public void onHudRender(DrawContext context, float delta) {
		if (mc.options.hudHidden) return;
		int scaledWidth = mc.getWindow().getScaledWidth();
		int scaledHeight = mc.getWindow().getScaledHeight();
		time = (time + delta) % (MicroDurability.config.lowDurabilityWarning.blinkTime * 40f);

		// Render held item low durability warning
		if (MicroDurability.config.lowDurabilityWarning.displayWarningForTools) {
			for (ItemStack item : mc.player.getHandItems()) {
				if (MicroDurability.shouldWarn(item)) {
					if (MicroDurability.config.lowDurabilityWarning.blinkTime > 0
							&& time < MicroDurability.config.lowDurabilityWarning.blinkTime * 20f) {
						break;
					}

					renderWarning(context, scaledWidth/2 - 2, scaledHeight/2 - 18); // TODO: This doesn't align with the crosshair at some resolutions
					break;
				}
			}
		}

		int x = scaledWidth/2 - 7;
		int y = scaledHeight - 30;
		if (mc.player.experienceLevel > 0) y -= 6;

		if (FabricLoader.getInstance().getObjectShare().get("raised:distance") instanceof Integer distance) {
			y -= distance;
		}

		boolean canRenderArmorBar = true;

		// Render armor low durability warning
		if (MicroDurability.config.lowDurabilityWarning.displayWarningForArmor) {
			for (ItemStack armorPiece : mc.player.getArmorItems()) {
				if (MicroDurability.shouldWarn(armorPiece)) {
					if (MicroDurability.config.lowDurabilityWarning.blinkTime > 0
							&& time < MicroDurability.config.lowDurabilityWarning.blinkTime * 20f) {
						break;
					}

					renderWarning(context, x+5, y-12);
					canRenderArmorBar = false;
					break;
				}
			}
		}

		// Render the armor durability
		if (!canRenderArmorBar) return;

		if (MicroDurability.config.armorBars.displayArmorBars) {
			for (ItemStack armorPiece : mc.player.getArmorItems()) {
				renderBar(armorPiece, x, y -= 3);
			}
		}
	}

	private void renderWarning(DrawContext context, int x, int y) {
		context.drawTexture(TEX, x, y, 0, 0, 3, 11);
	}

	private void renderBar(ItemStack stack, int x, int y) {
		if (stack == null || stack.isEmpty()) return;
		if (!MicroDurability.config.armorBars.displayBarsForUndamagedArmor && !stack.isItemBarVisible()) return;
		if (!stack.isDamageable()) return;

		disableRenderSystems();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		int width = stack.getItemBarStep();
		this.renderGuiQuad(bufferBuilder, x, y, 13, 2, 0, 0, 0, 255);
		int red;
		int green;
		int blue;
		int alpha;

		if (!stack.isDamaged() && MicroDurability.config.armorBars.useCustomBarColorForUndamagedArmor) {
			int argb = MicroDurability.config.armorBars.customBarColorForUndamagedArmor;
			red = ((argb >> 16) & 0xFF) & 255;
			green = ((argb >> 8) & 0xFF) & 255;
			blue = (argb & 0xFF) & 255;
			alpha = ((argb >> 24) & 0xFF) & 255;
		} else {
			int color = stack.getItemBarColor();
			red = color >> 16 & 255;
			green = color >> 8 & 255;
			blue = color & 255;
			alpha = 255;
		}

		this.renderGuiQuad(bufferBuilder, x, y, width, 1, red, green, blue, alpha);
		enableRenderSystems();
	}

	private void enableRenderSystems() {
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
	}

	private void disableRenderSystems() {
		RenderSystem.disableDepthTest();
		RenderSystem.disableBlend();
	}

	@SuppressWarnings("checkstyle:SingleSpaceSeparator")
	private void renderGuiQuad(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		buffer.vertex(x,         y,          0.0D).color(red, green, blue, alpha).next();
		buffer.vertex(x,         y + height, 0.0D).color(red, green, blue, alpha).next();
		buffer.vertex(x + width, y + height, 0.0D).color(red, green, blue, alpha).next();
		buffer.vertex(x + width, y,          0.0D).color(red, green, blue, alpha).next();
		BufferRenderer.drawWithGlobalProgram(buffer.end());
	}
}
