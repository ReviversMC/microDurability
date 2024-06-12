package com.github.reviversmc.microdurability;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import com.github.reviversmc.microdurability.integration.DoubleHotbarCompat;
import com.github.reviversmc.microdurability.integration.RaisedCompat;

public abstract class RendererBase {
	private static final Identifier microdurabilityTexture = new Identifier("microdurability", "textures/gui/icons.png");
	private static final boolean doubleHotbarLoaded = FabricLoader.getInstance().isModLoaded("double_hotbar");
	private static boolean raisedLoaded = FabricLoader.getInstance().isModLoaded("raised");
	private final MinecraftClient mc;

	protected RendererBase() {
		mc = MinecraftClient.getInstance();

		if (!raisedLoaded) {
			return;
		}
	}

	private int getRaisedOffset() {
		if (!raisedLoaded) {
			return 0;
		}

		return RaisedCompat.getHotbarOffset();
	}

	private int getDoubleHotbarOffset() {
		if (!doubleHotbarLoaded) {
			return 0;
		}

		return DoubleHotbarCompat.getHotbarHeight();
	}

	private boolean isTimeToShowWarning(int tick) {
		if (MicroDurability.config.lowDurabilityWarning.blinkTime < 0.001) {
			return true;
		}

		return tick % (MicroDurability.config.lowDurabilityWarning.blinkTime * 40f)
				> (MicroDurability.config.lowDurabilityWarning.blinkTime * 20f);
	}

	public void renderHeldItemLowDurabilityWarning(Object context, int tick, float delta) {
		if (!MicroDurability.config.lowDurabilityWarning.displayWarningForTools || !isTimeToShowWarning(tick)) {
			return;
		}

		int scaledWidth = mc.getWindow().getScaledWidth();
		int scaledHeight = mc.getWindow().getScaledHeight();

		for (ItemStack item : getHandItems(mc.player)) {
			if (!MicroDurability.shouldWarn(item)) {
				continue;
			}

			// TODO: This doesn't align with the crosshair at some resolutions
			int warningX = scaledWidth/2 - 2;
			int warningY = scaledHeight/2 - 18;

			renderWarning(context, warningX, warningY);
			break;
		}
	}

	public void renderArmorArea(Object context, int tick, float delta) {
		int scaledWidth = mc.getWindow().getScaledWidth();
		int scaledHeight = mc.getWindow().getScaledHeight();

		int x = scaledWidth/2 - 7;
		int y = scaledHeight - 30 - MicroDurability.config.armorBars.yOffset;
		if (mc.player.experienceLevel > 0) y -= 6;

		boolean renderedWarning = MicroDurability.config.lowDurabilityWarning.displayWarningForArmor
				&& isTimeToShowWarning(tick)
				&& renderArmorLowDurabilityWarning(context, x+5, y-12);

		if (!renderedWarning && MicroDurability.config.armorBars.displayArmorBars) {
			renderArmorBars(context, x, y - getRaisedOffset() - getDoubleHotbarOffset());
		}
	}

	public boolean renderArmorLowDurabilityWarning(Object context, int x, int y) {
		for (ItemStack armorPiece : mc.player.getArmorItems()) {
			if (!MicroDurability.shouldWarn(armorPiece)) {
				continue;
			}

			renderWarning(context, x, y);
			return true;
		}

		return false;
	}

	private void renderArmorBars(Object context, int x, int y) {
		for (ItemStack armorPiece : mc.player.getArmorItems()) {
			renderBar(armorPiece, x, y -= 3);
		}
	}

	private void renderWarning(Object context, int x, int y) {
		drawWarningTexture(microdurabilityTexture, context, x, y, 0, 0, 3, 11);
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
	protected abstract void drawWarningTexture(Identifier texture, Object context, int x, int y, int u, int v, int width, int height);
	protected abstract Iterable<ItemStack> getHandItems(ClientPlayerEntity e);
	protected abstract void renderGuiQuad(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha);
}
