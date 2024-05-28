package com.github.reviversmc.microdurability;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public abstract class RendererBase extends DrawableHelper implements HudRenderCallback {
	protected static final Identifier TEX = new Identifier("microdurability", "textures/gui/icons.png");
	private final MinecraftClient mc;
	private float time = 0;

	protected RendererBase() {
		mc = MinecraftClient.getInstance();
	}

	@Override
	public void onHudRender(MatrixStack matrixStack, float delta) {
		int scaledWidth = mc.getWindow().getScaledWidth();
		int scaledHeight = mc.getWindow().getScaledHeight();
		time = (time + delta) % (MicroDurability.config.lowDurabilityWarning.blinkTime * 40f);

		// Render held item low durability warning
		if (MicroDurability.config.lowDurabilityWarning.displayWarningForTools) {
			for (ItemStack item : getHandItems(mc.player)) {
				if (MicroDurability.shouldWarn(item)) {
					if (MicroDurability.config.lowDurabilityWarning.blinkTime > 0
							&& time < MicroDurability.config.lowDurabilityWarning.blinkTime * 20f) {
						break;
					}

					renderWarning(matrixStack, scaledWidth/2 - 2, scaledHeight/2 - 18); // TODO: This doesn't align with the crosshair at some resolutions
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

					renderWarning(matrixStack, x+5, y-12);
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

	private void renderWarning(MatrixStack matrixStack, int x, int y) {
		RenderSystem.setShaderTexture(0, TEX);
		drawTexture(matrixStack, x, y, 0, 0, 3, 11);
		RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
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

	protected abstract void enableRenderSystems();
	protected abstract void disableRenderSystems();
	protected abstract Iterable<ItemStack> getHandItems(ClientPlayerEntity e);
	protected abstract void renderGuiQuad(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha);
}
