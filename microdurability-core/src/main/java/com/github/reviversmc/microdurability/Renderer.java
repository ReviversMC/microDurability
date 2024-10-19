package com.github.reviversmc.microdurability;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import com.github.reviversmc.microdurability.compat.mods.DoubleHotbarCompat;
import com.github.reviversmc.microdurability.compat.mods.RaisedCompat;

public abstract class Renderer {
	private static final Identifier microdurabilityTexture = Identifier.tryParse("microdurability:textures/gui/icons.png");
	protected final MinecraftClient mc;

	protected Renderer() {
		mc = MinecraftClient.getInstance();
	}

	protected final int getRaisedOffset() {
		if (!RaisedCompat.isInstalled()) {
			return 0;
		}

		return RaisedCompat.getHotbarOffset();
	}

	private int getDoubleHotbarOffset() {
		if (!DoubleHotbarCompat.isInstalled()) {
			return 0;
		}

		return -1 * DoubleHotbarCompat.getHotbarHeight();
	}

	private boolean isStatusAreaVisible() {
		return mc.interactionManager.hasStatusBars() && !mc.options.hudHidden;
	}

	private boolean isTimeToShowWarning(int tick) {
		if (MicroDurability.config.lowDurabilityWarning.blinkTime < 0.001) {
			return true;
		}

		return tick % (MicroDurability.config.lowDurabilityWarning.blinkTime * 40f)
				> (MicroDurability.config.lowDurabilityWarning.blinkTime * 20f);
	}

	public boolean shouldWarn(ItemStack stack) {
		if (stack == null || !stack.isDamageable()) {
			return false;
		}

		if (MicroDurability.config.lowDurabilityWarning.onlyOnMendingItems && !hasMending(stack)) {
			return false;
		}

		int durability = stack.getMaxDamage() - stack.getDamage();
		boolean damageAbsoluteValueEnough = durability < MicroDurability.config.lowDurabilityWarning.minDurabilityPointsBeforeWarning;
		boolean damagePercentageEnough = (durability * 100f / stack.getMaxDamage()) < MicroDurability.config.lowDurabilityWarning.minDurabilityPercentageBeforeWarning;

		return damageAbsoluteValueEnough && damagePercentageEnough;
	}

	public void renderHeldItemLowDurabilityWarning(Object context, int tick) {
		if (!MicroDurability.config.lowDurabilityWarning.displayWarningForTools
				|| !isStatusAreaVisible()
				|| !isTimeToShowWarning(tick)) {
			return;
		}

		int scaledWidth = mc.getWindow().getScaledWidth();
		int scaledHeight = mc.getWindow().getScaledHeight();

		for (ItemStack item : mc.player.getHandItems()) {
			if (!shouldWarn(item)) {
				continue;
			}

			// TODO: This doesn't align with the crosshair at some resolutions
			int warningX = scaledWidth/2 - 2;
			int warningY = scaledHeight/2 - 18;

			renderWarning(context, warningX, warningY);
			break;
		}
	}

	public void renderArmorArea(Object context, int tick) {
		if (!isStatusAreaVisible()) {
			return;
		}

		int scaledWidth = mc.getWindow().getScaledWidth();
		int scaledHeight = mc.getWindow().getScaledHeight();

		int x = scaledWidth/2 - 7;
		int y = scaledHeight - 30 + getDoubleHotbarOffset() - MicroDurability.config.armorBars.yOffset;
		if (mc.player.experienceLevel > 0) y -= 6;

		boolean renderedWarning = MicroDurability.config.lowDurabilityWarning.displayWarningForArmor
				&& isTimeToShowWarning(tick)
				&& renderArmorLowDurabilityWarning(context, x+5, y-12);

		if (!renderedWarning && MicroDurability.config.armorBars.displayArmorBars) {
			renderArmorBars(context, x, y);
		}
	}

	public boolean renderArmorLowDurabilityWarning(Object context, int x, int y) {
		for (ItemStack armorPiece : mc.player.getArmorItems()) {
			if (!shouldWarn(armorPiece)) {
				continue;
			}

			if (RaisedCompat.isInstalled() && !RaisedCompat.isV3OrLater()) {
				y += getRaisedOffset();
			}

			renderWarning(context, x, y);
			return true;
		}

		return false;
	}

	private void renderArmorBars(Object context, int x, int y) {
		for (ItemStack armorPiece : mc.player.getArmorItems()) {
			renderBar(context, armorPiece, x, y -= 3);
		}
	}

	private void renderWarning(Object context, int x, int y) {
		drawWarningTexture(microdurabilityTexture, context, x, y, 0, 0, 3, 11);
	}

	private void renderBar(Object context, ItemStack stack, int x, int y) {
		if (stack == null || stack.isEmpty()) return;
		if (!MicroDurability.config.armorBars.displayBarsForUndamagedArmor && !stack.isDamaged()) return;
		if (!stack.isDamageable()) return;

		preRenderGuiQuads();
		int width = getItemBarStep(stack);
		this.renderGuiQuad(context, x, y, 13, 2, 0, 0, 0, 255);
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
			int color = getItemBarColor(stack);
			red = color >> 16 & 255;
			green = color >> 8 & 255;
			blue = color & 255;
			alpha = 255;
		}

		this.renderGuiQuad(context, x, y, width, 1, red, green, blue, alpha);
		postRenderGuiQuads();
	}

	protected abstract boolean hasMending(ItemStack stack);
	protected abstract void drawWarningTexture(Identifier texture, Object context, int x, int y, int u, int v, int width, int height);
	protected abstract int getItemBarStep(ItemStack stack);
	protected abstract int getItemBarColor(ItemStack stack);
	protected abstract void preRenderGuiQuads();
	protected abstract void postRenderGuiQuads();
	protected abstract void renderGuiQuad(Object context, int x, int y, int width, int height, int red, int green, int blue, int alpha);
}
